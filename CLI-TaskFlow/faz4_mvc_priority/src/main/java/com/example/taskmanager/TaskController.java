package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * TaskController - Controller class baraye management task ha
 * 
 * in class daroniri bizorati nadarad (System.out, Scanner)
 * va hame amaliat ha ba file sync hastand
 * 
 * MVC Pattern: Controller layer
 */
public class TaskController {

    // ========== FIELD HA ==========
    private DatabaseManager dbManager;    // reference be DatabaseManager (Singleton)

    // ========== CONSTRUCTOR ==========

    /**
     * sazande - creates controller ba reference be DatabaseManager
     */
    public TaskController() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * sazande ba DatabaseManager makhsoos
     */
    public TaskController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // ========== CRUD - CREATE ==========

    /**
     * sakhtan task jadid baraye yek karbar
     */
    public Task createTask(String nameKarbari, String onvan, int payvand, String mokalefZaman) 
            throws InvalidTaskException, UserNotFoundException {
        
        // create task jadid - exception palkhand mishad agar ghalat bashad
        Task taskJadid = new Task(onvan, payvand, mokalefZaman);
        
        // ezafe kon be data store
        dbManager.getDataStore().addTask(nameKarbari, taskJadid);
        
        // auto save be file
        dbManager.saveData();
        
        return taskJadid;
    }

    // ========== CRUD - READ ==========

    /**
     * gereftan hame task haye yek karbar
     */
    public ArrayList<Task> readAllTasks(String nameKarbari) throws UserNotFoundException {
        return dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
    }

    /**
     * gereftan yek task ba onvan
     */
    public Task readTask(String nameKarbari, String onvanTask) throws UserNotFoundException {
        ArrayList<Task> tasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        if (tasks != null) {
            for (Task task : tasks) {
                if (task.getOnvan().equals(onvanTask)) {
                    return task;
                }
            }
        }
        return null;
    }

    /**
     * gereftan task haye anjam nashode yek karbar
     */
    public ArrayList<Task> readPendingTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        ArrayList<Task> pendingTasks = new ArrayList<>();
        
        if (hameTasks != null) {
            for (Task task : hameTasks) {
                if (!task.checkAnjamShode()) {
                    pendingTasks.add(task);
                }
            }
        }
        
        return pendingTasks;
    }

    /**
     * gereftan task haye anjam shode yek karbar
     */
    public ArrayList<Task> readCompletedTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        ArrayList<Task> completedTasks = new ArrayList<>();
        
        if (hameTasks != null) {
            for (Task task : hameTasks) {
                if (task.checkAnjamShode()) {
                    completedTasks.add(task);
                }
            }
        }
        
        return completedTasks;
    }

    // ========== CRUD - UPDATE ==========

    /**
     * update onvan task
     */
    public void updateTaskOnvan(String nameKarbari, String onvanQabli, String onvanJadid) 
            throws InvalidTaskException, UserNotFoundException {
        
        Task task = readTask(nameKarbari, onvanQabli);
        if (task != null) {
            task.setOnvan(onvanJadid);
            dbManager.saveData();
        }
    }

    /**
     * update payvand task
     */
    public void updateTaskPayvand(String nameKarbari, String onvanTask, int payvandJadid) 
            throws InvalidTaskException, UserNotFoundException {
        
        Task task = readTask(nameKarbari, onvanTask);
        if (task != null) {
            task.setPayvand(payvandJadid);
            dbManager.saveData();
        }
    }

    /**
     * update deadline task
     */
    public void updateTaskDeadline(String nameKarbari, String onvanTask, String mokalefZamanJadid) 
            throws UserNotFoundException {
        
        Task task = readTask(nameKarbari, onvanTask);
        if (task != null) {
            task.setMokalefZaman(mokalefZamanJadid);
            dbManager.saveData();
        }
    }

    /**
     * anjam shodane task
     */
    public void completeTask(String nameKarbari, String onvanTask) throws UserNotFoundException {
        Task task = readTask(nameKarbari, onvanTask);
        if (task != null) {
            task.anjamKon();
            dbManager.saveData();
        }
    }

    /**
     * be dast avordane task (anjam nashode)
     */
    public void uncompleteTask(String nameKarbari, String onvanTask) throws UserNotFoundException {
        Task task = readTask(nameKarbari, onvanTask);
        if (task != null) {
            task.beDastAvord();
            dbManager.saveData();
        }
    }

    // ========== CRUD - DELETE ==========

    /**
     * hazf task
     */
    public void deleteTask(String nameKarbari, String onvanTask) throws UserNotFoundException {
        dbManager.getDataStore().removeTask(nameKarbari, onvanTask);
        dbManager.saveData();
    }

    /**
     * hazf hame task haye anjam shode yek karbar
     */
    public void deleteCompletedTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> tasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        if (tasks != null) {
            // estefade az iterator baraye hazf saat anjam shod
            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.checkAnjamShode()) {
                    iterator.remove();
                }
            }
            dbManager.saveData();
        }
    }

    // ========== PRIORITY QUEUE ==========

    /**
     * namayesh task ha bar asase priority
     * task haye ba payvand bala tar avval namayesh dade mishavand
     */
    public PriorityQueue<Task> showPrioritizedTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> tasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // PriorityQueue khodash sort mikone bar asase compareTo
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>(tasks);
        
        return priorityQueue;
    }

    /**
     * gereftan task haye sorted bar asase priority
     */
    public ArrayList<Task> getPrioritizedTasksList(String nameKarbari) throws UserNotFoundException {
        PriorityQueue<Task> pq = showPrioritizedTasks(nameKarbari);
        ArrayList<Task> sortedTasks = new ArrayList<>();
        
        while (!pq.isEmpty()) {
            sortedTasks.add(pq.poll());
        }
        
        return sortedTasks;
    }

    // ========== STATISTICS ==========

    /**
     * tedad kol task haye yek karbar
     */
    public int countAllTasks(String nameKarbari) throws UserNotFoundException {
        return dbManager.getDataStore().getTedadTask(nameKarbari);
    }

    /**
     * tedad task haye anjam nashode
     */
    public int countPendingTasks(String nameKarbari) throws UserNotFoundException {
        return readPendingTasks(nameKarbari).size();
    }

    /**
     * tedad task haye anjam shode
     */
    public int countCompletedTasks(String nameKarbari) throws UserNotFoundException {
        return readCompletedTasks(nameKarbari).size();
    }

    /**
     * darsad anjam shodane task ha
     */
    public double getCompletionPercentage(String nameKarbari) throws UserNotFoundException {
        int kol = countAllTasks(nameKarbari);
        if (kol == 0) {
            return 0.0;
        }
        int completed = countCompletedTasks(nameKarbari);
        return (completed * 100.0) / kol;
    }

    // ========== GETTER ==========

    /**
     * get DatabaseManager
     */
    public DatabaseManager getDbManager() {
        return dbManager;
    }
}