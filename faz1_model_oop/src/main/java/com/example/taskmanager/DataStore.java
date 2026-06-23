package com.example.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataStore class - simulate yek database ba estefade az HashMap
 * key = nameKarbari (username)
 * value = ArrayList<Task> (list task haye an karbar)
 * 
 * in class az Heap baraye zakhire mوقت استفاده می mikone
 */
public class DataStore {

    // ========== FIELD HA ==========
    // HashMap dar Heap zakhire mishe - ref ba in HashMap dar Stack gharar migire
    private HashMap<String, ArrayList<Task>> taskHayeKarbar;

    // ========== CONSTRUCTOR ==========

    /**
     * sazande khali - creates DataStore jadid
     */
    public DataStore() {
        this.taskHayeKarbar = new HashMap<>();
    }

    // ========== METHOD HA ==========

    /**
     * task jadid baraye yek karbar ezafe mikone
     * @param nameKarbari username
     * @param task task jadid
     */
    public void addTask(String nameKarbari, Task task) {
        // check kon agar karbar exist nadare, yek ArrayList jadid besaz
        if (!taskHayeKarbar.containsKey(nameKarbari)) {
            taskHayeKarbar.put(nameKarbari, new ArrayList<>());
        }
        // task ezafe kon be list
        taskHayeKarbar.get(nameKarbari).add(task);
    }

    /**
     * hame task haye yek karbar ro pas mide
     * @param nameKarbari username
     * @return list task haye karbar (null agar notfound)
     */
    public ArrayList<Task> getTaskHayeKarbar(String nameKarbari) {
        return taskHayeKarbar.get(nameKarbari);
    }

    /**
     * check mikone akah karbar exist dare ya na
     * @param nameKarbari username
     * @return true agar exist bashe
     */
    public boolean karbarExist(String nameKarbari) {
        return taskHayeKarbar.containsKey(nameKarbari);
    }

    /**
     * tedade kol task haye yek karbar ro pas mide
     * @param nameKarbari username
     * @return tedad task
     */
    public int getTedadTask(String nameKarbari) {
        ArrayList<Task> tasks = taskHayeKarbar.get(nameKarbari);
        return tasks != null ? tasks.size() : 0;
    }

    /**
     * hame karbar ha ro pas mide
     * @return set of usernames
     */
    public ArrayList<String> getHameKarbarHa() {
        return new ArrayList<>(taskHayeKarbar.keySet());
    }

    /**
     * print hame task haye yek karbar
     * @param nameKarbari username
     */
    public void printTaskHayeKarbar(String nameKarbari) {
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
        return "📦 DataStore: " + taskHayeKarbar.size() + " karbar dar jahat zakhire hastand";
    }
}