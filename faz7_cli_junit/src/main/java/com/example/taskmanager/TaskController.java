package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

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
     * @param dbManager database manager
     */
    public TaskController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // ========== CRUD - CREATE ==========

    /**
     * sakhtan task jadid baraye yek karbar
     * @param nameKarbari username
     * @param onvan title task
     * @param payvand priority (1-5)
     * @param mokalefZaman deadline
     * @return task jadid
     * @throws InvalidTaskException agar voridi ghalat bashad
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @return list task haye karbar
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public ArrayList<Task> readAllTasks(String nameKarbari) throws UserNotFoundException {
        return readAllTasksSync(nameKarbari);
    }

    /**
     * gereftan hame task haye yek karbar - SYNCHRONIZED
     * in method baraye estefade dar thread ha design shode
     * 
     * @param nameKarbari username
     * @return list task haye karbar
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public synchronized ArrayList<Task> readAllTasksSync(String nameKarbari) throws UserNotFoundException {
        return dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
    }

    /**
     * gereftan task haye anjam nashode - SYNCHRONIZED
     * baraye estefade dar thread
     * 
     * @param nameKarbari username
     * @return list task haye anjam nashode
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public synchronized ArrayList<Task> getPendingTasksSync(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = readAllTasksSync(nameKarbari);
        
        List<Task> pendingTasks = hameTasks.stream()
            .filter(task -> !task.checkAnjamShode())
            .collect(Collectors.toList());
        
        return new ArrayList<>(pendingTasks);
    }

    /**
     * gereftan task haye ba payvand bala (4 ya 5) - SYNCHRONIZED
     * baraye estefade dar ReminderThread
     * 
     * @param nameKarbari username
     * @return list task haye ba payvand bala
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public synchronized ArrayList<Task> getHighPriorityPendingTasksSync(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = readAllTasksSync(nameKarbari);
        
        List<Task> highPending = hameTasks.stream()
            .filter(task -> !task.checkAnjamShode())
            .filter(task -> task.getPayvand() >= 4)
            .collect(Collectors.toList());
        
        return new ArrayList<>(highPending);
    }

    /**
     * gereftan yek task ba onvan
     * @param nameKarbari username
     * @param onvanTask title task
     * @return task ya null agar peyda nashod
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @return list task haye anjam nashode
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @return list task haye anjam shode
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @param onvanQabli title ghablesi
     * @param onvanJadid title jadid
     * @throws InvalidTaskException agar voridi ghalat bashad
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @param onvanTask title task
     * @param payvandJadid priority jadid
     * @throws InvalidTaskException agar payvand ghalat bashad
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @param onvanTask title task
     * @param mokalefZamanJadid deadline jadid
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @param onvanTask title task
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @param onvanTask title task
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @param onvanTask title task
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public void deleteTask(String nameKarbari, String onvanTask) throws UserNotFoundException {
        dbManager.getDataStore().removeTask(nameKarbari, onvanTask);
        dbManager.saveData();
    }

    /**
     * hazf hame task haye anjam shode yek karbar
     * @param nameKarbari username
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * 
     * @param nameKarbari username
     * @return PriorityQueue sorted az task ha
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public PriorityQueue<Task> showPrioritizedTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> tasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // PriorityQueue khodash sort mikone bar asase compareTo
        PriorityQueue<Task> priorityQueue = new PriorityQueue<>(tasks);
        
        return priorityQueue;
    }

    /**
     * gereftan task haye sorted bar asase priority
     * @param nameKarbari username
     * @return ArrayList az task ha sorted
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @param nameKarbari username
     * @return tedad kol
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public int countAllTasks(String nameKarbari) throws UserNotFoundException {
        return dbManager.getDataStore().getTedadTask(nameKarbari);
    }

    /**
     * tedad task haye anjam nashode
     * @param nameKarbari username
     * @return tedad pending
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public int countPendingTasks(String nameKarbari) throws UserNotFoundException {
        return readPendingTasks(nameKarbari).size();
    }

    /**
     * tedad task haye anjam shode
     * @param nameKarbari username
     * @return tedad completed
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public int countCompletedTasks(String nameKarbari) throws UserNotFoundException {
        return readCompletedTasks(nameKarbari).size();
    }

    /**
     * darsad anjam shodane task ha
     * @param nameKarbari username
     * @return darsad (0-100)
     * @throws UserNotFoundException agar karbar peyda nashavad
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
     * @return dbManager
     */
    public DatabaseManager getDbManager() {
        return dbManager;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ═══════════ FAZ 5: STREAM API & FUNCTIONAL PROGRAMMING ═══════════════
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * gereftan task haye anjam nashode ba Stream API
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return list task haye anjam nashode
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public ArrayList<Task> getPendingTasksStream(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - filter + collect
        List<Task> pendingTasks = hameTasks.stream()
            .filter(task -> !task.checkAnjamShode())
            .collect(Collectors.toList());
        
        return new ArrayList<>(pendingTasks);
    }

    /**
     * gereftan task haye anjam shode ba Stream API
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return list task haye anjam shode
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public ArrayList<Task> getCompletedTasksStream(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - filter + collect
        List<Task> completedTasks = hameTasks.stream()
            .filter(Task::checkAnjamShode)
            .collect(Collectors.toList());
        
        return new ArrayList<>(completedTasks);
    }

    /**
     * filtaring task haye overdue (mokalef ghadam shode va anjam nashode)
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return list task haye overdue
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public ArrayList<Task> getOverdueTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        LocalDate emroz = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Stream API - filter ba lambda jadvari
        List<Task> overdueTasks = hameTasks.stream()
            .filter(task -> {
                // faghad task haye anjam nashode
                if (task.checkAnjamShode()) {
                    return false;
                }
                // check deadline
                try {
                    LocalDate deadline = LocalDate.parse(task.getMokalefZaman(), formatter);
                    return deadline.isBefore(emroz);
                } catch (DateTimeParseException e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
        
        return new ArrayList<>(overdueTasks);
    }

    /**
     * groupBy task ha bar asase vaziat anjam (completed/pending)
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return Map ba key = vaziat, value = list task
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public Map<Boolean, List<Task>> getTasksGroupedByStatus(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - groupingBy
        Map<Boolean, List<Task>> grouped = hameTasks.stream()
            .collect(Collectors.groupingBy(Task::checkAnjamShode));
        
        return grouped;
    }

    /**
     * groupBy task ha bar asase payvand
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return Map ba key = payvand, value = list task
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public Map<Integer, List<Task>> getTasksGroupedByPriority(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - groupingBy
        Map<Integer, List<Task>> grouped = hameTasks.stream()
            .collect(Collectors.groupingBy(Task::getPayvand));
        
        return grouped;
    }

    /**
     * mohasebe darsad anjam shodane ba Stream API
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return darsad (0.0 ta 100.0)
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public double getCompletionPercentageStream(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        if (hameTasks.isEmpty()) {
            return 0.0;
        }
        
        // Stream API - count + map
        long tedadKol = hameTasks.stream().count();
        long tedadCompleted = hameTasks.stream()
            .filter(Task::checkAnjamShode)
            .count();
        
        return (tedadCompleted * 100.0) / tedadKol;
    }

    /**
     * tedad task haye har payvand
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return Map ba key = payvand, value = tedad
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public Map<Integer, Long> getTaskCountByPriority(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - groupingBy + counting
        Map<Integer, Long> countByPriority = hameTasks.stream()
            .collect(Collectors.groupingBy(Task::getPayvand, Collectors.counting()));
        
        return countByPriority;
    }

    /**
     * mohasebe miane payvand task ha
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return miane payvand ya 0.0 agar task nabashad
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public double getAveragePriority(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        if (hameTasks.isEmpty()) {
            return 0.0;
        }
        
        // Stream API - mapToInt + average
        return hameTasks.stream()
            .mapToInt(Task::getPayvand)
            .average()
            .orElse(0.0);
    }

    /**
     * gereftan task haye ba payvand bala tar az 3
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return list task haye ba payvand bala az 3
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public ArrayList<Task> getHighPriorityTasks(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - filter + collect
        List<Task> highPriority = hameTasks.stream()
            .filter(task -> task.getPayvand() >= 4)
            .sorted((t1, t2) -> t2.getPayvand() - t1.getPayvand())
            .collect(Collectors.toList());
        
        return new ArrayList<>(highPriority);
    }

    /**
     * gereftan onvan task ha ba Stream API
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return list onvan task ha
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public List<String> getTaskTitles(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - map + collect
        return hameTasks.stream()
            .map(Task::getOnvan)
            .collect(Collectors.toList());
    }

    /**
     * check mikone aya hame task ha anjam shode and ya na
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return true agar hame anjam shode bashand
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public boolean areAllTasksCompleted(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - allMatch
        return hameTasks.stream()
            .allMatch(Task::checkAnjamShode);
    }

    /**
     * check mikone aya yeki az task ha anjam nashode ya na
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return true agar yeki anjam nashode bashad
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public boolean hasAnyPendingTask(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - anyMatch
        return hameTasks.stream()
            .anyMatch(task -> !task.checkAnjamShode());
    }

    /**
     * sort task ha bar asase mokalefZaman (az ghadim tar be jadid tar)
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return list task haye sorted
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public ArrayList<Task> getTasksSortedByDeadline(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - sorted ba Comparator jadvari
        List<Task> sorted = hameTasks.stream()
            .sorted((t1, t2) -> t1.getMokalefZaman().compareTo(t2.getMokalefZaman()))
            .collect(Collectors.toList());
        
        return new ArrayList<>(sorted);
    }

    /**
     * gereftan task akharin deadline
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return task ba akharin deadline ya null
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public Task getTaskWithLatestDeadline(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - max
        return hameTasks.stream()
            .max((t1, t2) -> t1.getMokalefZaman().compareTo(t2.getMokalefZaman()))
            .orElse(null);
    }

    /**
     * gereftan task ba bala tarin payvand
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return task ba bala tarin payvand ya null
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public Task getMostUrgentTask(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        // Stream API - max
        return hameTasks.stream()
            .max((t1, t2) -> t1.getPayvand() - t2.getPayvand())
            .orElse(null);
    }

    /**
     * summary report bar asase Stream API
     * for loop ESTEFADE NASHODE
     * 
     * @param nameKarbari username
     * @return String ba summary report
     * @throws UserNotFoundException agar karbar peyda nashavad
     */
    public String getSummaryReport(String nameKarbari) throws UserNotFoundException {
        ArrayList<Task> hameTasks = dbManager.getDataStore().getTaskHayeKarbar(nameKarbari);
        
        long kol = hameTasks.stream().count();
        long completed = hameTasks.stream().filter(Task::checkAnjamShode).count();
        long pending = kol - completed;
        double darsad = getCompletionPercentageStream(nameKarbari);
        double mianePayvand = getAveragePriority(nameKarbari);
        
        // Stream API - max/min
        Task balaTarinPayvand = hameTasks.stream()
            .max((t1, t2) -> t1.getPayvand() - t2.getPayvand())
            .orElse(null);
        
        Task akharinDeadline = hameTasks.stream()
            .max((t1, t2) -> t1.getMokalefZaman().compareTo(t2.getMokalefZaman()))
            .orElse(null);
        
        StringBuilder report = new StringBuilder();
        report.append("═══════════════════════════════════════════\n");
        report.append("📊 SUMMARY REPORT - ").append(nameKarbari).append("\n");
        report.append("═══════════════════════════════════════════\n");
        report.append("📌 Kol Task Ha: ").append(kol).append("\n");
        report.append("✅ Anjam Shode: ").append(completed).append("\n");
        report.append("❌ Anjam Nashode: ").append(pending).append("\n");
        report.append("📈 Darsad Anjam: ").append(String.format("%.1f", darsad)).append("%\n");
        report.append("📊 Miane Payvand: ").append(String.format("%.1f", mianePayvand)).append("\n");
        
        if (balaTarinPayvand != null) {
            report.append("🔥 Bala Tarin Payvand: ").append(balaTarinPayvand.getOnvan())
                  .append(" (").append(balaTarinPayvand.getPayvand()).append(")\n");
        }
        
        if (akharinDeadline != null) {
            report.append("⏰ Akharin Deadline: ").append(akharinDeadline.getOnvan())
                  .append(" (").append(akharinDeadline.getMokalefZaman()).append(")\n");
        }
        report.append("═══════════════════════════════════════════");
        
        return report.toString();
    }
}