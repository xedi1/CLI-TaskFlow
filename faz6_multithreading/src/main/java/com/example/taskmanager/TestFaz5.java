package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TestFaz5 class - test baraye phase 5
 * Stream API & Functional Programming
 * 
 * hame method ha ba Stream API (for loop ESTEFADE NASHODE)
 */
public class TestFaz5 {

    public static void main(String[] args) {
        
        // ===== PRINT SERI ======
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚀 FAZ 5: STREAM API & FUNCTIONAL PROGRAMMING           ║");
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

        // sakhtan task ha ba tarikh ghadam shode baraye test overdue
        try {
            // Task haye Ali
            controller.createTask("Ali", "Yaddash avordan dars", 5, "2024-01-01");      // ghadam shode
            controller.createTask("Ali", "Tamiz karḋan otagh", 1, "2024-01-15");         // ghadam shode
            controller.createTask("Ali", "Code neveshtan", 4, "2025-12-01");             // jadid
            controller.createTask("Ali", "Kharid mahsolat", 3, "2026-06-01");            // jadid
            controller.createTask("Ali", "Saḱhtan web site", 5, "2025-03-15");          // jadid
            
            // Task haye Sara
            controller.createTask("Sara", "Pezeshki raftan", 4, "2024-02-01");          // ghadam shode
            controller.createTask("Sara", "Sport", 2, "2026-01-01");                     // jadid
            
            // complete kardan bazi task ha
            controller.completeTask("Ali", "Yaddash avordan dars");
            controller.completeTask("Sara", "Pezeshki raftan");
            
            System.out.println("✅ Data sakhte shod!");
        } catch (InvalidTaskException | UserNotFoundException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 1: getPendingTasksStream ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 1: PENDING TASKS (STREAM) ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> pendingTasks = controller.getPendingTasksStream("Ali");
            System.out.println("  Task haye pending Ali (Stream): " + pendingTasks.size());
            pendingTasks.forEach(task -> System.out.println("    - " + task.getOnvan()));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 2: getCompletedTasksStream ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 2: COMPLETED TASKS (STREAM) ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> completedTasks = controller.getCompletedTasksStream("Ali");
            System.out.println("  Task haye completed Ali (Stream): " + completedTasks.size());
            completedTasks.forEach(task -> System.out.println("    - " + task.getOnvan()));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 3: getOverdueTasks ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 3: OVERDUE TASKS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> overdueTasks = controller.getOverdueTasks("Ali");
            System.out.println("  Task haye overdue Ali: " + overdueTasks.size());
            overdueTasks.forEach(task -> System.out.println("    - " + task.getOnvan() + " (Deadline: " + task.getMokalefZaman() + ")"));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 4: getTasksGroupedByStatus ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 4: GROUP BY STATUS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Map<Boolean, List<Task>> grouped = controller.getTasksGroupedByStatus("Ali");
            System.out.println("  Group by Status:");
            grouped.forEach((status, tasks) -> {
                String vaziat = status ? "Anjam Shode" : "Anjam Nashode";
                System.out.println("    " + vaziat + " (" + tasks.size() + "):");
                tasks.forEach(task -> System.out.println("      - " + task.getOnvan()));
            });
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 5: getTasksGroupedByPriority ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 5: GROUP BY PRIORITY ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Map<Integer, List<Task>> groupedByPriority = controller.getTasksGroupedByPriority("Ali");
            System.out.println("  Group by Priority:");
            groupedByPriority.entrySet().stream()
                .sorted((e1, e2) -> e2.getKey() - e1.getKey())
                .forEach(entry -> {
                    System.out.println("    Payvand " + entry.getKey() + " (" + entry.getValue().size() + " task):");
                    entry.getValue().forEach(task -> System.out.println("      - " + task.getOnvan()));
                });
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 6: getCompletionPercentageStream ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 6: COMPLETION PERCENTAGE (STREAM) ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            double darsad = controller.getCompletionPercentageStream("Ali");
            System.out.println("  Darsad anjam shodane Ali: " + String.format("%.1f", darsad) + "%");
            
            darsad = controller.getCompletionPercentageStream("Sara");
            System.out.println("  Darsad anjam shodane Sara: " + String.format("%.1f", darsad) + "%");
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 7: getTaskCountByPriority ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 7: COUNT BY PRIORITY ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Map<Integer, Long> countByPriority = controller.getTaskCountByPriority("Ali");
            System.out.println("  Tedad task bar asase payvand:");
            countByPriority.entrySet().stream()
                .sorted((e1, e2) -> e2.getKey() - e1.getKey())
                .forEach(entry -> System.out.println("    Payvand " + entry.getKey() + ": " + entry.getValue() + " task"));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 8: getAveragePriority ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 8: AVERAGE PRIORITY ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            double miane = controller.getAveragePriority("Ali");
            System.out.println("  Miane payvand task haye Ali: " + String.format("%.2f", miane));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 9: getHighPriorityTasks ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 9: HIGH PRIORITY TASKS (>=4) ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> highPriority = controller.getHighPriorityTasks("Ali");
            System.out.println("  Task haye ba payvand bala az 3:");
            highPriority.forEach(task -> System.out.println("    - " + task.getOnvan() + " (Payvand: " + task.getPayvand() + ")"));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 10: getTaskTitles ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 10: TASK TITLES (MAP) ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            List<String> titles = controller.getTaskTitles("Ali");
            System.out.println("  Onvan hame task haye Ali:");
            titles.forEach(title -> System.out.println("    - " + title));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 11: areAllTasksCompleted & hasAnyPendingTask ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 11: ALL MATCH / ANY MATCH ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            boolean hameAnjam = controller.areAllTasksCompleted("Ali");
            boolean pendiExist = controller.hasAnyPendingTask("Ali");
            System.out.println("  Ali - hame anjam shode? " + hameAnjam);
            System.out.println("  Ali - pending exist? " + pendiExist);
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 12: getTasksSortedByDeadline ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 12: SORTED BY DEADLINE ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> sortedByDeadline = controller.getTasksSortedByDeadline("Ali");
            System.out.println("  Task haye Ali sorted az ghadim tar be jadid tar:");
            sortedByDeadline.forEach(task -> System.out.println("    - " + task.getOnvan() + " (" + task.getMokalefZaman() + ")"));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 13: getTaskWithLatestDeadline ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 13: LATEST DEADLINE ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Task latestTask = controller.getTaskWithLatestDeadline("Ali");
            if (latestTask != null) {
                System.out.println("  Task ba akharin deadline: " + latestTask.getOnvan() + " (" + latestTask.getMokalefZaman() + ")");
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 14: getMostUrgentTask ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 14: MOST URGENT TASK ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Task urgentTask = controller.getMostUrgentTask("Ali");
            if (urgentTask != null) {
                System.out.println("  Task ba bala tarin payvand: " + urgentTask.getOnvan() + " (Payvand: " + urgentTask.getPayvand() + ")");
            }
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 15: SUMMARY REPORT ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 15: SUMMARY REPORT ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            String report = controller.getSummaryReport("Ali");
            System.out.println("\n" + report);
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 16: CHAINED OPERATIONS ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 16: CHAINED OPERATIONS ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            System.out.println("  Task haye pending ba payvand 4 ya 5:");
            ArrayList<Task> hameTasks = controller.readAllTasks("Ali");
            hameTasks.stream()
                .filter(task -> !task.checkAnjamShode())
                .filter(task -> task.getPayvand() >= 4)
                .sorted((t1, t2) -> t2.getPayvand() - t1.getPayvand())
                .forEach(task -> System.out.println("    - " + task.getOnvan() + " (Payvand: " + task.getPayvand() + ")"));
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== FINISH =====
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ FAZ 5 BA OMID MOVAFAGH PAYAN YAFT!                  ║");
        System.out.println("║     Stream API + Lambda + Functional Programming           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}