package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * TestFaz4 class - test baraye phase 4
 * MVC Architecture & PriorityQueue
 * 
 * in class yek test class hast va daroniri nadard
 */
public class TestFaz4 {

    public static void main(String[] args) {
        
        // ===== PRINT SERI ======
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚀 FAZ 4: MVC & PRIORITY QUEUE - TEST                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // hazf file ghadimi
        DatabaseManager.getInstance().deleteFile();

        // ===== SETUP ======
        System.out.println("━━━━━━━━━━━━━━━━━━━━ SETUP ━━━━━━━━━━━━━━━━━━━━");
        
        TaskController controller = new TaskController();
        DatabaseManager db = controller.getDbManager();
        
        // sabt karbar ha
        db.getDataStore().addKarbar(new User("Ali", "12345"));
        db.getDataStore().addKarbar(new User("Sara", "abcdef"));
        db.saveData();

        // ===== TEST 1: CREATE TASKS (CRUD - CREATE) ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 1: CREATE TASKS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            controller.createTask("Ali", "Sakhtan web site", 3, "2025-02-15");
            controller.createTask("Ali", "season2.life", 5, "2025-01-20");
            controller.createTask("Ali", "Tamiz karḋan otagh", 1, "2025-01-25");
            controller.createTask("Ali", "Kharid mahsolat", 2, "2025-02-01");
            controller.createTask("Ali", "Code neveshtan", 4, "2025-02-10");
            
            controller.createTask("Sara", "Pezeshki raftan", 4, "2025-01-30");
            controller.createTask("Sara", "Sport", 2, "2025-02-05");
            
            System.out.println("✅ Hame task ha ba movafaghiyat sakhte shodand!");
        } catch (InvalidTaskException | UserNotFoundException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 2: READ TASKS (CRUD - READ) ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 2: READ ALL TASKS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> aliTasks = controller.readAllTasks("Ali");
            System.out.println("  Task haye Ali: " + aliTasks.size());
            for (Task task : aliTasks) {
                System.out.println("    - " + task.toStringPretty());
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 3: READ PENDING TASKS ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 3: READ PENDING TASKS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> pendingTasks = controller.readPendingTasks("Ali");
            System.out.println("  Task haye pending Ali: " + pendingTasks.size());
            for (Task task : pendingTasks) {
                System.out.println("    - " + task.getOnvan());
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 4: UPDATE TASK (CRUD - UPDATE) ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 4: UPDATE TASKS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            // update onvan
            controller.updateTaskOnvan("Ali", "Tamiz karḋan otagh", "Tamiz karḋan tamam otagh ha");
            System.out.println("  ✅ Onvan update shod");
            
            // update payvand
            controller.updateTaskPayvand("Ali", "Kharid mahsolat", 5);
            System.out.println("  ✅ Payvand update shod");
            
            // anjam shodane task
            controller.completeTask("Ali", "Yaddash avordan dars");
            System.out.println("  ✅ Task anjam shod");
        } catch (InvalidTaskException | UserNotFoundException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 5: READ COMPLETED TASKS ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 5: READ COMPLETED TASKS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> completedTasks = controller.readCompletedTasks("Ali");
            System.out.println("  Task haye completed Ali: " + completedTasks.size());
            for (Task task : completedTasks) {
                System.out.println("    - " + task.getOnvan());
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 6: PRIORITY QUEUE (MAIN FEATURE) ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 6: PRIORITY QUEUE ━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  Task haye Ali sorted bar asase priority (5=bala, 1=paiin):");
        
        try {
            PriorityQueue<Task> pq = controller.showPrioritizedTasks("Ali");
            
            // namayesh ba sorat sorted
            int rank = 1;
            System.out.println("\n  🎯 Priority Order:");
            while (!pq.isEmpty()) {
                Task task = pq.poll();
                System.out.println("    " + rank + ". " + task.getOnvan() + 
                                   " (Payvand: " + task.getPayvand() + ")");
                rank++;
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 7: STATISTICS ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 7: STATISTICS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            int kol = controller.countAllTasks("Ali");
            int pending = controller.countPendingTasks("Ali");
            int completed = controller.countCompletedTasks("Ali");
            double darsad = controller.getCompletionPercentage("Ali");
            
            System.out.println("  📊 Amar task haye Ali:");
            System.out.println("    Kol: " + kol);
            System.out.println("    Pending: " + pending);
            System.out.println("    Completed: " + completed);
            System.out.println("    Darsad anjam: " + String.format("%.1f", darsad) + "%");
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 8: DELETE TASK (CRUD - DELETE) ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 8: DELETE TASK ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            int ghabl = controller.countAllTasks("Ali");
            controller.deleteTask("Ali", "Kharid mahsolat");
            int bad = controller.countAllTasks("Ali");
            System.out.println("  Tedad ghabl: " + ghabl + " | Tedad bad: " + bad);
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 9: UNCOMPLETE TASK ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 9: UNCOMPLETE TASK ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            controller.uncompleteTask("Ali", "Yaddash avordan dars");
            ArrayList<Task> completed = controller.readCompletedTasks("Ali");
            System.out.println("  Task haye completed bad az uncomplete: " + completed.size());
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 10: FILE PERSISTENCE ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 10: FILE PERSISTENCE ━━━━━━━━━━━━━━━━━━━━");
        
        db.printFileContent();
        
        // simulate restart
        System.out.println("\n  Simulare restart...");
        DatabaseManager dbRestart = DatabaseManager.getInstance();
        dbRestart.getDataStore().getDataStoreKarbarHa().clear();
        dbRestart.loadData();
        
        try {
            ArrayList<Task> tasks = controller.readAllTasks("Ali");
            System.out.println("  Task haye Ali bad az load: " + tasks.size());
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 11: GET PRIORITIZED LIST ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 11: GET PRIORITIZED LIST ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> sortedList = controller.getPrioritizedTasksList("Ali");
            System.out.println("  Task haye sorted (ArrayList):");
            for (int i = 0; i < sortedList.size(); i++) {
                Task task = sortedList.get(i);
                System.out.println("    " + (i+1) + ". " + task.getOnvan() + 
                                   " (Payvand: " + task.getPayvand() + ")");
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== FINISH =====
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ FAZ 4 BA OMID MOVAFAGH PAYAN YAFT!                  ║");
        System.out.println("║     MVC Pattern + PriorityQueue + CRUD                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}