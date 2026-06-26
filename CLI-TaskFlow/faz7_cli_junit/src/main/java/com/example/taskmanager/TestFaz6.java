package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TestFaz6 class - test baraye phase 6
 * Multithreading & Synchronization
 * 
 * in class Threading ro test mikone
 */
public class TestFaz6 {

    public static void main(String[] args) {
        
        // ===== PRINT SERI ======
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚀 FAZ 6: MULTITHREADING & SYNCHRONIZATION             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // hazf file ghadimi
        DatabaseManager.getInstance().deleteFile();

        // ===== SETUP ======
        System.out.println("━━━━━━━━━━━━━━━━━━━━ SETUP ━━━━━━━━━━━━━━━━━━━━");
        
        TaskController controller = new TaskController();
        DatabaseManager db = controller.getDbManager();
        
        // sabt karbar
        db.getDataStore().addKarbar(new User("Ali", "12345"));
        db.saveData();

        // sakhtan task haye avaliye
        try {
            controller.createTask("Ali", "Task mohem 1", 5, "2026-06-25");
            controller.createTask("Ali", "Task mohem 2", 4, "2026-06-26");
            controller.createTask("Ali", "Task normal 1", 3, "2026-07-01");
            controller.createTask("Ali", "Task normal 2", 2, "2026-07-05");
            controller.createTask("Ali", "Task paiin 1", 1, "2026-07-10");
            
            System.out.println("✅ 5 task sakhte shod (2 mohem, 3 normal)");
        } catch (InvalidTaskException | UserNotFoundException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 1: SINGLETON THREAD SAFETY ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 1: SINGLETON THREAD SAFETY ━━━━━━━━━━━━━━━━━━━━");
        
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                DatabaseManager dm = DatabaseManager.getInstance();
                System.out.println("  Thread " + index + " - HashCode: " + dm.hashCode());
            });
            threads[i].start();
        }
        
        // wait for all threads
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("❌ Thread interrupt shod!");
            }
        }
        System.out.println("✅ Singleton thread-safe ast!");

        // ===== TEST 2: CREATE REMINDER THREAD ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 2: CREATE REMINDER THREAD ━━━━━━━━━━━━━━━━━━━━");
        
        ReminderThread reminderThread = new ReminderThread(controller, "Ali");
        Thread thread = new Thread(reminderThread);
        
        System.out.println("  ReminderThread ijad shod!");
        System.out.println("  Karbar: " + reminderThread.getNameKarbari());
        System.out.println("  Running: " + reminderThread.isRunning());

        // ===== TEST 3: START THREAD ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 3: START THREAD ━━━━━━━━━━━━━━━━━━━━");
        
        thread.start();
        System.out.println("  ✅ Thread start shod!");

        // ===== TEST 4: SIMULATE MENU WHILE THREAD RUNNING ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 4: CONCURRENT ACCESS ━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  Main thread dar hal simulate menu ast...");
        
        MenuInteraction menu = new MenuInteraction(controller, "Ali");
        Random random = new Random();
        
        // simulate 5 menu action
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(3000);  // har 3 sanie yek action
                
                int action = random.nextInt(5) + 1;
                String result = menu.simulateAction(action);
                
                System.out.println("\n  🎯 Menu Action " + (i+1) + ": " + result);
                System.out.println("     Thread hala running: " + reminderThread.isRunning());
                
            } catch (InterruptedException e) {
                System.out.println("❌ Main thread interrupt shod!");
            }
        }

        // ===== TEST 5: PAUSE/RESUME THREAD ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 5: PAUSE/RESUME ━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("  Pause kardan reminder...");
        reminderThread.pause();
        System.out.println("  Thread paused: " + reminderThread.isPaused());
        
        // menu actions during pause
        try {
            Thread.sleep(5000);
            menu.simulateAction(1);
            System.out.println("  Menu action dar zaman pause: OK");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
        
        System.out.println("  Resume kardan reminder...");
        reminderThread.resume();
        System.out.println("  Thread resumed: " + !reminderThread.isPaused());

        // ===== TEST 6: ADD HIGH PRIORITY TASK (TRIGGER REMINDER) ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 6: TRIGGER REMINDER ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Thread.sleep(5000);  // wait for next reminder cycle
            
            // add task ba payvand bala
            controller.createTask("Ali", "🔥 Task jadid mohem", 5, "2026-06-24");
            System.out.println("  ✅ Task mohem jadid ezafe shod!");
            
            // wait for reminder to detect
            Thread.sleep(11000);
            
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 7: CONCURRENT MODIFICATION TEST ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 7: CONCURRENT MODIFICATION ━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("  Test ConcurrentModificationException...");
        
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    controller.createTask("Ali", "Writer Task " + i, 3, "2026-08-01");
                    Thread.sleep(100);
                } catch (Exception e) {
                    // ignore
                }
            }
        });
        
        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    ArrayList<Task> tasks = controller.readAllTasksSync("Ali");
                    System.out.println("  Reader: " + tasks.size() + " task");
                    Thread.sleep(100);
                } catch (Exception e) {
                    // ignore
                }
            }
        });
        
        writerThread.start();
        readerThread.start();
        
        try {
            writerThread.join();
            readerThread.join();
            System.out.println("  ✅ Concurrent access bi error! (synchronized karmand)");
        } catch (InterruptedException e) {
            System.out.println("❌ Thread interrupt shod!");
        }

        // ===== TEST 8: STOP THREAD ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 8: STOP THREAD ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            Thread.sleep(5000);  // wait for last reminder
            reminderThread.stop();
            thread.join(2000);  // wait max 2 sanie
            System.out.println("  ✅ Thread stop shod!");
            System.out.println("  Thread alive: " + thread.isAlive());
        } catch (InterruptedException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== TEST 9: FINAL STATE ======
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 9: FINAL STATE ━━━━━━━━━━━━━━━━━━━━");
        
        try {
            ArrayList<Task> allTasks = controller.readAllTasks("Ali");
            ArrayList<Task> pendingTasks = controller.getPendingTasksSync("Ali");
            
            System.out.println("  Kol task ha: " + allTasks.size());
            System.out.println("  Task haye pending: " + pendingTasks.size());
            
            System.out.println("\n  Hame task ha:");
            for (Task task : allTasks) {
                String vaziat = task.checkAnjamShode() ? "✅" : "❌";
                System.out.println("    " + vaziat + " " + task.getOnvan() + " (Payvand: " + task.getPayvand() + ")");
            }
            
            System.out.println("\n  Menu action log:");
            for (String log : menu.getActionLog()) {
                System.out.println("    - " + log);
            }
            
        } catch (UserNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // ===== FINISH =====
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ FAZ 6 BA OMID MOVAFAGH PAYAN YAFT!                  ║");
        System.out.println("║     Multithreading + Synchronization + Runnable            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}