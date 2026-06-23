package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;

/**
 * TestFaz3 class - test baraye phase 3
 * File I/O va Singleton Pattern
 */
public class TestFaz3 {

    public static void main(String[] args) {
        
        // ===== PRINT SERI ======
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚀 FAZ 3: FILE I/O & SINGLETON PATTERN - TEST          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // ===== TEST 1: SINGLETON PATTERN ======
        System.out.println("━━━━━━━━━━━━━━━━━━━━ TEST 1: SINGLETON PATTERN ━━━━━━━━━━━━━━━━━━━━");
        
        DatabaseManager db1 = DatabaseManager.getInstance();
        DatabaseManager db2 = DatabaseManager.getInstance();
        
        System.out.println("  db1 hashcode: " + db1.hashCode());
        System.out.println("  db2 hashcode: " + db2.hashCode());
        System.out.println("  db1 == db2 ? " + (db1 == db2));
        
        if (db1 == db2) {
            System.out.println("✅ Singleton pattern sahih ast! Yekta instance.");
        } else {
            System.out.println("❌ Singleton pattern nakham ast!");
        }

        // ===== TEST 2: ADD DATA =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 2: ADD DATA ━━━━━━━━━━━━━━━━━━━━");
        
        DataStore dataStore = db1.getDataStore();
        dataStore.addKarbar(new User("Ali", "12345"));
        dataStore.addKarbar(new User("Sara", "abcdef"));
        dataStore.addKarbar(new User("Reza", "pass123"));

        try {
            dataStore.addTask("Ali", "Saḱhtan web site", 3, "2025-02-15");
            dataStore.addTask("Ali", "Yaddash avordan dars", 5, "2025-01-20");
            dataStore.addTask("Ali", "Tamiz karḋan otagh", 1, "2025-01-25");
            dataStore.addTask("Ali", "Kharid mahsolat", 2, "2025-02-01");
            
            dataStore.addTask("Sara", "Pezeshki raftan", 4, "2025-01-30");
            dataStore.addTask("Sara", "Dars amadane code", 5, "2025-02-10");
            
            dataStore.addTask("Reza", "Bazary raftan", 2, "2025-02-05");
            
            System.out.println("✅ Hame data ba movafaghiyat ezafe shod!");
        } catch (InvalidTaskException | UserNotFoundException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 3: DISPLAY DATA =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 3: DISPLAY DATA ━━━━━━━━━━━━━━━━━━━━");
        
        for (String nameKarbari : dataStore.getHameKarbarHa()) {
            try {
                dataStore.printTaskHayeKarbar(nameKarbari);
            } catch (UserNotFoundException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }

        // ===== TEST 4: SAVE DATA TO FILE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 4: SAVE DATA TO FILE ━━━━━━━━━━━━━━━━━━━━");
        
        db1.saveData();
        System.out.println("  File size: " + db1.getFileSize() + " bytes");

        // ===== TEST 5: VIEW FILE CONTENT =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 5: VIEW FILE CONTENT ━━━━━━━━━━━━━━━━━━━━");
        
        db1.printFileContent();

        // ===== TEST 6: SIMULATE PROGRAM RESTART - CREATE NEW INSTANCE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 6: SIMULATE RESTART ━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  Simulare program restart... (jadge object jadid)");
        
        // get new instance - hamoon instance ghadimi hast (singleton)
        DatabaseManager dbRestart = DatabaseManager.getInstance();
        
        // clear data store
        dbRestart.getDataStore().getDataStoreKarbarHa().clear();
        
        // load data az file
        dbRestart.loadData();

        // ===== TEST 7: DISPLAY LOADED DATA =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 7: DISPLAY LOADED DATA ━━━━━━━━━━━━━━━━━━━━");
        
        for (String nameKarbari : dbRestart.getDataStore().getHameKarbarHa()) {
            try {
                dbRestart.getDataStore().printTaskHayeKarbar(nameKarbari);
            } catch (UserNotFoundException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }

        // ===== TEST 8: MODIFY DATA AND RESAVE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 8: MODIFY AND RESAVE ━━━━━━━━━━━━━━━━━━━━");
        
        // anjam shodane yek task
        try {
            DataStore ds = dbRestart.getDataStore();
            ArrayList<Task> aliTasks = ds.getTaskHayeKarbar("Ali");
            if (aliTasks != null && !aliTasks.isEmpty()) {
                aliTasks.get(0).anjamKon();
                System.out.println("  Task 1 Ali anjam shod!");
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // save again
        dbRestart.saveData();
        System.out.println("  Data bazsave shod!");

        // ===== TEST 9: FILE AFTER MODIFICATION =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 9: FILE AFTER MODIFICATION ━━━━━━━━━━━━━━━━━━━━");
        
        dbRestart.printFileContent();

        // ===== TEST 10: TRY LOAD FROM MODIFIED FILE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 10: LOAD FROM MODIFIED FILE ━━━━━━━━━━━━━━━━━━━━");
        
        // create db jadid
        DatabaseManager dbFinal = DatabaseManager.getInstance();
        dbFinal.getDataStore().getDataStoreKarbarHa().clear();
        dbFinal.loadData();

        // check vaziat task
        try {
            ArrayList<Task> aliTasks = dbFinal.getDataStore().getTaskHayeKarbar("Ali");
            if (aliTasks != null && !aliTasks.isEmpty()) {
                Task firstTask = aliTasks.get(0);
                System.out.println("  Task 1 Ali: " + firstTask.getOnvan());
                System.out.println("  Vaziat: " + (firstTask.isVaziatAnjam() ? "Anjam Shode ✅" : "Anjam Nashode ❌"));
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 11: FILE NOT EXIST SCENARIO =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 11: FILE NOT EXIST ━━━━━━━━━━━━━━━━━━━━");
        
        DatabaseManager dbNew = DatabaseManager.getInstance();
        dbNew.deleteFile();
        System.out.println("  File hazf shod. trying load...");
        dbNew.loadData();

        // ===== FINISH =====
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ FAZ 3 BA OMID MOVAFAGH PAYAN YAFT!                  ║");
        System.out.println("║     Singleton + File I/O + try-with-resources              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}