package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.io.*;
import java.util.ArrayList;

/**
 * DatabaseManager - singleton class baraye zakhire data dar file
 * 
 * Pattern: Initialization-on-demand holder (Thread-Safe Lazy Init)
 * in pattern az Inner Static Class baraye thread-safety estefade mikone
 */
public class DatabaseManager {

    // ========== CONSTANTS ==========
    private static final String FILE_NAME = "tasks.txt";
    private static final String SEPARATOR = ";";  // separator baraye CSV custom

    // ========== FIELD HA ==========
    private DataStore dataStore;    // reference be data store dar memory

    // ========== SINGLETON ==========
    
    /**
     * Inner static class baraye lazy initialization
     * in class faghad dakhele method getInstance load mishavad
     */
    private static class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    /**
     * getInstance - singleton instance ro pas mide
     */
    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * private constructor - kasi nemitoane instantiate kone
     */
    private DatabaseManager() {
        this.dataStore = new DataStore();
    }

    // ========== DATASTORE METHODS ==========

    /**
     * dataStore ro pas mide
     */
    public DataStore getDataStore() {
        return dataStore;
    }

    /**
     * dataStore jadid set mikone
     */
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // ========== FILE I/O METHODS ==========

    /**
     * saveData - save hame data be file
     * format: USER:nameKarbari|ramzObur
     *         TASK:nameKarbari:onvan:payvand:mokalefZaman:vaziatAnjam
     * 
     * try-with-resources estefade mishavad baraye close kardan
     */
    public void saveData() {
        // try-with-resources - auto close
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            
            // save karbar ha
            for (String nameKarbari : dataStore.getHameKarbarHa()) {
                User karbar = dataStore.getDataStoreKarbarHa().get(nameKarbari);
                if (karbar != null) {
                    String line = "USER" + SEPARATOR + karbar.getNameKarbari() + SEPARATOR + karbar.getRamzObur();
                    writer.write(line);
                    writer.newLine();
                }
            }

            // save task ha
            for (String nameKarbari : dataStore.getHameKarbarHa()) {
                ArrayList<Task> tasks = dataStore.getTaskHayeKarbar(nameKarbari);
                if (tasks != null) {
                    for (Task task : tasks) {
                        String line = "TASK" + SEPARATOR + 
                                      nameKarbari + SEPARATOR + 
                                      task.getOnvan() + SEPARATOR + 
                                      task.getPayvand() + SEPARATOR + 
                                      task.getMokalefZaman() + SEPARATOR + 
                                      task.isVaziatAnjam();
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            System.out.println("✅ Data dar file " + FILE_NAME + " zakhire shod!");

        } catch (IOException e) {
            System.out.println("❌ Ghalat dar zakhire data: " + e.getMessage());
        }
    }

    /**
     * loadData - load data az file
     * try-with-resources estefade mishavad baraye close kardan
     */
    public void loadData() {
        File file = new File(FILE_NAME);
        
        // check kon agar file exist nadare
        if (!file.exists()) {
            System.out.println("📁 File " + FILE_NAME + " exist nadarad. DataStore khali ast.");
            return;
        }

        // try-with-resources - auto close
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            
            String line;
            int counterUser = 0;
            int counterTask = 0;

            while ((line = reader.readLine()) != null) {
                // skip khali lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // parse line
                String[] parts = line.split(SEPARATOR);
                
                if (parts.length < 1) {
                    continue;
                }

                String noe = parts[0];

                if (noe.equals("USER") && parts.length >= 3) {
                    // USER;nameKarbari;ramzObur
                    String nameKarbari = parts[1];
                    String ramzObur = parts[2];
                    
                    User karbar = new User(nameKarbari, ramzObur);
                    dataStore.addKarbar(karbar);
                    counterUser++;

                } else if (noe.equals("TASK") && parts.length >= 6) {
                    // TASK;nameKarbari;onvan;payvand;mokalefZaman;vaziatAnjam
                    String nameKarbari = parts[1];
                    String onvan = parts[2];
                    int payvand = Integer.parseInt(parts[3]);
                    String mokalefZaman = parts[4];
                    boolean vaziatAnjam = Boolean.parseBoolean(parts[5]);

                    try {
                        Task task = new Task(onvan, payvand, mokalefZaman);
                        task.setVaziatAnjam(vaziatAnjam);
                        dataStore.addTask(nameKarbari, task);
                        counterTask++;
                    } catch (InvalidTaskException e) {
                        System.out.println("⚠️  Task load nashod: " + e.getMessage());
                    }
                }
            }

            System.out.println("✅ Data az file " + FILE_NAME + " load shod!");
            System.out.println("   " + counterUser + " karbar va " + counterTask + " task load shod.");

        } catch (IOException e) {
            System.out.println("❌ Error dar khandan data: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Error dar parse kardan adad: " + e.getMessage());
        }
    }

    // ========== UTILITY METHODS ==========

    /**
     * check mikone file exist dare ya na
     */
    public boolean fileExist() {
        return new File(FILE_NAME).exists();
    }

    /**
     * size file ro pas mide
     */
    public long getFileSize() {
        File file = new File(FILE_NAME);
        return file.exists() ? file.length() : 0;
    }

    /**
     * print content file (baraye debug)
     */
    public void printFileContent() {
        if (!fileExist()) {
            System.out.println("📁 File " + FILE_NAME + " exist nadarad.");
            return;
        }

        // try-with-resources
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            System.out.println("\n📄 Content file " + FILE_NAME + ":");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
        } catch (IOException e) {
            System.out.println("❌ Ghalat dar khandan file: " + e.getMessage());
        }
    }

    /**
     * delete file
     */
    public boolean deleteFile() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("🗑️  File " + FILE_NAME + " hazf shod!");
            }
            return deleted;
        }
        return false;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        return "💾 DatabaseManager: " + dataStore.toString() + " | File: " + FILE_NAME;
    }
}