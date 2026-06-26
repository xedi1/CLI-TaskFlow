package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataStore class - simulate yek database ba estefade az HashMap
 * key = nameKarbari (username)
 * value = ArrayList<Task> (list task haye an karbar)
 * 
 * in class az Heap baraye zakhire movaghat estefade میکنه
 * hame method ha exception palkhand mikond
 */
public class DataStore {

    // ========== FIELD HA ==========
    private HashMap<String, ArrayList<Task>> taskHayeKarbar;
    private HashMap<String, User> karbarHa;    // zakhire karbar ha baraye login

    // ========== CONSTRUCTOR ==========

    /**
     * sazande khali - creates DataStore jadid
     */
    public DataStore() {
        this.taskHayeKarbar = new HashMap<>();
        this.karbarHa = new HashMap<>();
    }

    // ========== USER METHODS ==========

    /**
     * sabt karbar jadid
     */
    public void addKarbar(User karbar) {
        if (karbarHa.containsKey(karbar.getNameKarbari())) {
            System.out.println("⚠️  Karbar " + karbar.getNameKarbari() + " ghablan exist dare!");
            return;
        }
        karbarHa.put(karbar.getNameKarbari(), karbar);
        taskHayeKarbar.put(karbar.getNameKarbari(), new ArrayList<>());
        System.out.println("✅ Karbar " + karbar.getNameKarbari() + " ba moafagh sabt shod!");
    }

    /**
     * hazf karbar
     */
    public void removeKarbar(String nameKarbari) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar peyda nashod!", nameKarbari);
        }
        karbarHa.remove(nameKarbari);
        taskHayeKarbar.remove(nameKarbari);
        System.out.println("✅ Karbar " + nameKarbari + " hazf shod!");
    }

    /**
     * login karbar
     */
    public void login(String nameKarbari, String ramzObur) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar ba in name peyda nashod!", nameKarbari);
        }
        karbarHa.get(nameKarbari).login(ramzObur);
    }

    /**
     * check mikone akah karbar exist dare ya na
     */
    public boolean karbarExist(String nameKarbari) {
        return karbarHa.containsKey(nameKarbari);
    }

    /**
     * get karbar ha hashmap - baraye estefade dar DatabaseManager
     */
    public HashMap<String, User> getDataStoreKarbarHa() {
        return karbarHa;
    }

    // ========== TASK METHODS ==========

    /**
     * task jadid baraye yek karbar ezafe mikone
     */
    public void addTask(String nameKarbari, Task task) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Nemitoane task ezafe kone! Karbar peyda nashod!", nameKarbari);
        }
        taskHayeKarbar.get(nameKarbari).add(task);
    }

    /**
     * task jadid baraye yek karbar ezafe mikone (ba exception handling dar method)
     */
    public void addTask(String nameKarbari, String onvan, int payvand, String mokalefZaman) 
            throws UserNotFoundException, InvalidTaskException {
        
        // check karbar exist
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar peyda nashod!", nameKarbari);
        }
        
        // بساز task jadid - agar ghalat bashe exception palkhand mishe
        Task taskJadid = new Task(onvan, payvand, mokalefZaman);
        
        // ezafe kon be list
        taskHayeKarbar.get(nameKarbari).add(taskJadid);
    }

    /**
     * hazf yek task az list karbar
     */
    public void removeTask(String nameKarbari, String onvanTask) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar peyda nashod!", nameKarbari);
        }
        
        ArrayList<Task> tasks = taskHayeKarbar.get(nameKarbari);
        boolean peydaShod = false;
        
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getOnvan().equals(onvanTask)) {
                tasks.remove(i);
                peydaShod = true;
                System.out.println("✅ Task '" + onvanTask + "' hazf shod!");
                break;
            }
        }
        
        if (!peydaShod) {
            System.out.println("⚠️  Task '" + onvanTask + "' peyda nashod!");
        }
    }

    /**
     * hame task haye yek karbar ro pas mide
     */
    public ArrayList<Task> getTaskHayeKarbar(String nameKarbari) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar peyda nashod!", nameKarbari);
        }
        return taskHayeKarbar.get(nameKarbari);
    }

    /**
     * tedade kol task haye yek karbar ro pas mide
     */
    public int getTedadTask(String nameKarbari) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar peyda nashod!", nameKarbari);
        }
        return taskHayeKarbar.get(nameKarbari).size();
    }

    /**
     * hame karbar ha ro pas mide
     */
    public ArrayList<String> getHameKarbarHa() {
        return new ArrayList<>(karbarHa.keySet());
    }

    /**
     * print hame task haye yek karbar
     */
    public void printTaskHayeKarbar(String nameKarbari) throws UserNotFoundException {
        if (!karbarHa.containsKey(nameKarbari)) {
            throw new UserNotFoundException("Karbar peyda nashod!", nameKarbari);
        }
        
        ArrayList<Task> tasks = taskHayeKarbar.get(nameKarbari);
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("❌ Hich taski baraye karbar " + nameKarbari + " peyda nashod!");
            return;
        }
        
        System.out.println("\n📋 Task haye karbar " + nameKarbari + ":");
        for (Task task : tasks) {
            System.out.println("  " + task.toString());
        }
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        return "📦 DataStore: " + karbarHa.size() + " karbar dar jahat zakhire hastand";
    }
}