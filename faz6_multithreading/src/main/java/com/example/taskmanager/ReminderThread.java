package com.example.taskmanager;

import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;

/**
 * ReminderThread - Background thread baraye yad avordan task haye mohem
 * 
 * in class Runnable ra implement mikone
 * har 10 sanie task haye pending ba payvand 4 ya 5 ro check mikone
 * va agar peyda kone, warning michapad
 */
public class ReminderThread implements Runnable {

    // ========== CONSTANTS ==========
    private static final int SLEEP_TIME_MS = 10000;  // 10 sanie
    private static final int MIN_PRIORITY = 4;       // payvand 4 ya 5

    // ========== FIELD HA ==========
    private TaskController controller;    // reference be controller
    private String nameKarbari;           // karbari ke bayad check beshe
    private volatile boolean running;     // flag baraye stop kardan thread
    private volatile boolean paused;      // flag baraye pause/resume

    // ========== CONSTRUCTOR ==========

    /**
     * sazande - creates ReminderThread jadid
     * @param controller task controller
     * @param nameKarbari username baraye check
     */
    public ReminderThread(TaskController controller, String nameKarbari) {
        this.controller = controller;
        this.nameKarbari = nameKarbari;
        this.running = true;
        this.paused = false;
    }

    // ========== RUN METHOD ==========

    /**
     * run - main loop thread
     * in method dar yek thread joda ejra mishavad
     */
    @Override
    public void run() {
        printReminderStart();

        // loop until running = false
        while (running) {
            try {
                // check pause
                while (paused) {
                    Thread.sleep(1000);  // wait 1 sanie vaghti paused
                }

                // check task ha
                checkTasksAndRemind();

                // sleep 10 sanie
                Thread.sleep(SLEEP_TIME_MS);

            } catch (InterruptedException e) {
                // thread interrupt shod
                System.out.println("⚠️  ReminderThread interrupt shod!");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // har ghalat digari
                System.err.println("❌ Ghalat dar ReminderThread: " + e.getMessage());
                try {
                    Thread.sleep(SLEEP_TIME_MS);  // dobare try kon
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        printReminderStop();
    }

    // ========== HELPER METHODS ==========

    /**
     * check task ha va print reminder
     */
    private void checkTasksAndRemind() {
        try {
            // SYNCHRONIZED - baraye thread safety
            ArrayList<Task> highPriorityTasks = controller.getHighPriorityPendingTasksSync(nameKarbari);

            if (highPriorityTasks != null && !highPriorityTasks.isEmpty()) {
                printReminderHeader();
                
                for (Task task : highPriorityTasks) {
                    printTaskWarning(task);
                }
                
                printReminderFooter(highPriorityTasks.size());
            }

        } catch (UserNotFoundException e) {
            // karbar peyda nashod - chizi natoonim check konim
        }
    }

    /**
     * print header reminder
     */
    private void printReminderStart() {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("🔔 REMINDER THREAD SHOROU SHOD");
        System.out.println("   Karbar: " + nameKarbari);
        System.out.println("   Check har " + (SLEEP_TIME_MS / 1000) + " sanie");
        System.out.println("   Payvand Payvand: " + MIN_PRIORITY + "+");
        System.out.println("════════════════════════════════════════════════════════════");
    }

    /**
     * print header baraye har check
     */
    private void printReminderHeader() {
        System.out.println("");
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  🔔 YAD AVORDAN - " + getCurrentTime() + "                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("⚠️  Task haye Mohem (Payvand " + MIN_PRIORITY + "+) baraye " + nameKarbari + ":");
    }

    /**
     * print warning baraye yek task
     */
    private void printTaskWarning(Task task) {
        String payvandEmoji = getPayvandEmoji(task.getPayvand());
        System.out.println("   " + payvandEmoji + " " + task.getOnvan());
        System.out.println("      📅 Deadline: " + task.getMokalefZaman() + 
                           " | Payvand: " + task.getPayvand());
    }

    /**
     * print footer reminder
     */
    private void printReminderFooter(int tedad) {
        System.out.println("");
        System.out.println("   💡 " + tedad + " task pending ba payvand bala!");
        System.out.println("────────────────────────────────────────────────────────────");
    }

    /**
     * print stop message
     */
    private void printReminderStop() {
        System.out.println("");
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("🔕 REMINDER THREAD TAVAN SHOD");
        System.out.println("════════════════════════════════════════════════════════════");
    }

    /**
     * get emoji baraye payvand
     */
    private String getPayvandEmoji(int payvand) {
        switch (payvand) {
            case 5: return "🔥";
            case 4: return "⚡";
            case 3: return "📌";
            case 2: return "📝";
            default: return "📋";
        }
    }

    /**
     * get zaman jari
     */
    private String getCurrentTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = 
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }

    // ========== CONTROL METHODS ==========

    /**
     * stop thread - baraye raftan az loop
     */
    public void stop() {
        this.running = false;
        System.out.println("🛑 ReminderThread dar hal stop shodan...");
    }

    /**
     * pause thread
     */
    public void pause() {
        this.paused = true;
        System.out.println("⏸️  ReminderThread pause shod!");
    }

    /**
     * resume thread
     */
    public void resume() {
        this.paused = false;
        System.out.println("▶️  ReminderThread edame darad!");
    }

    /**
     * check mikone thread running hast ya na
     * @return true agar running bashad
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * check mikone thread pause shode hast ya na
     * @return true agar pause bashad
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * get name karbari
     * @return nameKarbari
     */
    public String getNameKarbari() {
        return nameKarbari;
    }
}