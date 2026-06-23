package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * ConsoleView - CLI View baraye user interaction
 * 
 * in class yekta layer estefade az ConsoleColors baraye rang
 * hame error ha ra ba try-catch handle mikone
 */
public class ConsoleView {

    // ========== CONSTANTS ==========
    private static final String PROJECT_NAME = "Task Manager Pro";
    private static final String VERSION = "1.0.0";

    // ========== FIELD HA ==========
    private TaskController controller;
    private Scanner scanner;
    private String loggedInUser;
    private boolean running;

    // ========== CONSTRUCTOR ==========

    /**
     * sazande
     */
    public ConsoleView() {
        this.controller = new TaskController();
        this.scanner = new Scanner(System.in);
        this.loggedInUser = null;
        this.running = true;
        
        // load data az file
        controller.getDbManager().loadData();
    }

    // ========== MAIN METHOD ==========

    /**
     * main method - entry point
     */
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        view.run();
    }

    // ========== RUN LOOP ==========

    /**
     * run - main loop
     */
    public void run() {
        printHeader();
        
        while (running) {
            if (loggedInUser == null) {
                printLoginMenu();
            } else {
                printMainMenu();
            }
        }
        
        printGoodbye();
    }

    // ========== HEADER ==========

    /**
     * print header ba logo va info
     */
    public void printHeader() {
        // pak kardan screen
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // E
        System.out.println();
        ConsoleColors.printlnBold("  ██████╗  ██████╗ ██████╗ ████████╗███████╗ ██████╗ ██╗     ██╗ ██████╗ ", ConsoleColors.BLUE);
        ConsoleColors.printlnBold("  ██╔══██╗██╔═══██╗██╔══██╗╚══██╔══╝██╔════╝██╔═══██╗██║     ██║██╔═══██╗", ConsoleColors.BLUE);
        ConsoleColors.printlnBold("  ██████╔╝██║   ██║██████╔╝   ██║   █████╗  ██║   ██║██║     ██║██║   ██║", ConsoleColors.BLUE);
        ConsoleColors.printlnBold("  ██╔═══╝ ██║   ██║██╔══██╗   ██║   ██╔══╝  ██║   ██║██║     ██║██║   ██║", ConsoleColors.BLUE);
        ConsoleColors.printlnBold("  ██║     ╚██████╔╝██║  ██║   ██║   ██║     ╚██████╔╝███████╗██║╚██████╔╝", ConsoleColors.BLUE);
        ConsoleColors.printlnBold("  ╚═╝      ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝      ╚═════╝ ╚══════╝╚═╝ ╚═════╝ ", ConsoleColors.BLUE);
        
        System.out.println();
        ConsoleColors.println("  Made By Hadi Gholipour", ConsoleColors.YELLOW);
        System.out.println();
        ConsoleColors.println("  " + PROJECT_NAME + " v" + VERSION, ConsoleColors.GREEN);
        
        printLineGray();
    }

    /**
     * print khat gray
     */
    private void printLineGray() {
        ConsoleColors.println("  " + "═".repeat(60), ConsoleColors.GRAY);
    }

    // ========== LOGIN MENU ==========

    /**
     * print login menu
     */
    private void printLoginMenu() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ LOGIN MENU ─", ConsoleColors.PURPLE);
        ConsoleColors.println("  │", ConsoleColors.PURPLE);
        ConsoleColors.println("  │  1. Sabt Nam (Sign Up)", ConsoleColors.BLUE);
        ConsoleColors.println("  │  2. Vorod (Login)", ConsoleColors.GREEN);
        ConsoleColors.println("  │  3. Namayesh Karbar Ha", ConsoleColors.YELLOW);
        ConsoleColors.println("  │  0. Khorooj", ConsoleColors.RED);
        ConsoleColors.println("  │", ConsoleColors.PURPLE);
        ConsoleColors.printlnBold("  └────────────────────────────────", ConsoleColors.PURPLE);
        
        System.out.print("\n  Enter action: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline
            
            switch (choice) {
                case 1: handleSignUp(); break;
                case 2: handleLogin(); break;
                case 3: showAllUsers(); break;
                case 0: running = false; break;
                default: printError("Action namo'toufi!"); 
            }
        } catch (Exception e) {
            printError("Voridi ghalat ast!");
            scanner.nextLine();  // clear scanner
        }
    }

    // ========== MAIN MENU ==========

    /**
     * print main menu
     */
    private void printMainMenu() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ MAIN MENU [" + loggedInUser + "] ─", ConsoleColors.PURPLE);
        ConsoleColors.println("  │", ConsoleColors.PURPLE);
        ConsoleColors.println("  │  1. Ezafe Task Jadid", ConsoleColors.BLUE);
        ConsoleColors.println("  │  2. Namayesh Hame Task Ha", ConsoleColors.GREEN);
        ConsoleColors.println("  │  3. Namayesh Task Ba Priority", ConsoleColors.YELLOW);
        ConsoleColors.println("  │  4. Anjam Task", ConsoleColors.LIGHT_BLUE);
        ConsoleColors.println("  │  5. Hazf Task", ConsoleColors.RED);
        ConsoleColors.println("  │  6. Gozaresh Haye Amari", ConsoleColors.GRAY);
        ConsoleColors.println("  │  7. Filter Task Ha", ConsoleColors.PURPLE);
        ConsoleColors.println("  │  8. Virg'ool Shodan", ConsoleColors.ORANGE);
        ConsoleColors.println("  │  0. Khorooj", ConsoleColors.RED);
        ConsoleColors.println("  │", ConsoleColors.PURPLE);
        ConsoleColors.printlnBold("  └────────────────────────────────", ConsoleColors.PURPLE);
        
        System.out.print("\n  Enter action: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: handleAddTask(); break;
                case 2: handleShowAllTasks(); break;
                case 3: handleShowPriorityTasks(); break;
                case 4: handleCompleteTask(); break;
                case 5: handleDeleteTask(); break;
                case 6: handleShowStats(); break;
                case 7: handleFilterTasks(); break;
                case 8: handleVirgool(); break;
                case 0: handleLogout(); break;
                default: printError("Action namo'toufi!");
            }
        } catch (Exception e) {
            printError("Voridi ghalat ast!");
            scanner.nextLine();
        }
    }

    // ========== LOGIN HANDLERS ==========

    /**
     * handle sabt nam
     */
    private void handleSignUp() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ SABT NAM ─", ConsoleColors.BLUE);
        
        System.out.print("  Name Karbari: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            printError("Name karbari nemitoane khali bashe!");
            return;
        }
        
        System.out.print("  Ramz Obur: ");
        String ramz = scanner.nextLine().trim();
        
        if (ramz.isEmpty()) {
            printError("Ramz obur nemitoane khali bashe!");
            return;
        }
        
        try {
            controller.getDbManager().getDataStore().addKarbar(new User(name, ramz));
            controller.getDbManager().saveData();
            printSuccess("Sabt nam movafaghiyat amal!");
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    /**
     * handle vorod
     */
    private void handleLogin() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ VOROD ─", ConsoleColors.GREEN);
        
        System.out.print("  Name Karbari: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("  Ramz Obur: ");
        String ramz = scanner.nextLine().trim();
        
        try {
            controller.getDbManager().login(name, ramz);
            loggedInUser = name;
            printSuccess("Vorod movafaghiyat amal!");
        } catch (Exception e) {
            printError("Ghalat dar vorod: " + e.getMessage());
        }
    }

    /**
     * show hame karbar ha
     */
    private void showAllUsers() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ KARBAR HA ─", ConsoleColors.YELLOW);
        
        List<String> karbarHa = controller.getDbManager().getDataStore().getHameKarbarHa();
        
        if (karbarHa.isEmpty()) {
            ConsoleColors.println("  Hich karbari exist nadarad!", ConsoleColors.GRAY);
        } else {
            for (String name : karbarHa) {
                ConsoleColors.println("  👤 " + name, ConsoleColors.LIGHT_BLUE);
            }
        }
    }

    /**
     * handle logout
     */
    private void handleLogout() {
        loggedInUser = null;
        printSuccess("Virg'ool shodid!");
    }

    // ========== TASK HANDLERS ==========

    /**
     * handle ezafe task
     */
    private void handleAddTask() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ EZAFE TASK JADID ─", ConsoleColors.BLUE);
        
        System.out.print("  Onvan Task: ");
        String onvan = scanner.nextLine().trim();
        
        if (onvan.isEmpty()) {
            printError("Onvan nemitoane khali bashe!");
            return;
        }
        
        System.out.print("  Payvand (1-5): ");
        int payvand = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("  Deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine().trim();
        
        try {
            controller.createTask(loggedInUser, onvan, payvand, deadline);
            printSuccess("Task ba movafaghiyat ezafe shod!");
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    /**
     * handle namayesh hame task ha
     */
    private void handleShowAllTasks() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ HAME TASK HA ─", ConsoleColors.GREEN);
        
        try {
            ArrayList<Task> tasks = controller.readAllTasks(loggedInUser);
            
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski exist nadarad!", ConsoleColors.GRAY);
            } else {
                for (Task task : tasks) {
                    printTask(task);
                }
            }
            
            ConsoleColors.println("  \n  Kol: " + tasks.size() + " task", ConsoleColors.GRAY);
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    /**
     * handle namayesh task ba priority
     */
    private void handleShowPriorityTasks() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ TASK HA BA PRIORITY ─", ConsoleColors.YELLOW);
        
        try {
            ArrayList<Task> tasks = controller.getPrioritizedTasksList(loggedInUser);
            
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski exist nadarad!", ConsoleColors.GRAY);
            } else {
                int rank = 1;
                for (Task task : tasks) {
                    printTaskWithRank(task, rank++);
                }
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    /**
     * handle anjam task
     */
    private void handleCompleteTask() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ ANJAM TASK ─", ConsoleColors.LIGHT_BLUE);
        
        try {
            ArrayList<Task> pendingTasks = controller.getPendingTasksStream(loggedInUser);
            
            if (pendingTasks.isEmpty()) {
                ConsoleColors.println("  Hich task pending nist!", ConsoleColors.GRAY);
                return;
            }
            
            ConsoleColors.println("  Task haye pending:", ConsoleColors.GRAY);
            for (int i = 0; i < pendingTasks.size(); i++) {
                Task task = pendingTasks.get(i);
                ConsoleColors.println("  " + (i + 1) + ". " + task.getOnvan() + 
                    " (Payvand: " + task.getPayvand() + ")", ConsoleColors.LIGHT_BLUE);
            }
            
            System.out.print("\n  Shomare task: ");
            int index = scanner.nextInt();
            scanner.nextLine();
            
            if (index >= 1 && index <= pendingTasks.size()) {
                Task task = pendingTasks.get(index - 1);
                controller.completeTask(loggedInUser, task.getOnvan());
                printSuccess("Task anjam shod!");
            } else {
                printError("Shomare ghalat!");
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    /**
     * handle hazf task
     */
    private void handleDeleteTask() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ HAZF TASK ─", ConsoleColors.RED);
        
        try {
            ArrayList<Task> tasks = controller.readAllTasks(loggedInUser);
            
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski exist nadarad!", ConsoleColors.GRAY);
                return;
            }
            
            ConsoleColors.println("  Task ha:", ConsoleColors.GRAY);
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String vaziat = task.checkAnjamShode() ? "✅" : "❌";
                ConsoleColors.println("  " + (i + 1) + ". " + vaziat + " " + task.getOnvan(), ConsoleColors.RED);
            }
            
            System.out.print("\n  Shomare task: ");
            int index = scanner.nextInt();
            scanner.nextLine();
            
            if (index >= 1 && index <= tasks.size()) {
                Task task = tasks.get(index - 1);
                controller.deleteTask(loggedInUser, task.getOnvan());
                printSuccess("Task hazf shod!");
            } else {
                printError("Shomare ghalat!");
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    // ========== STATS HANDLERS ==========

    /**
     * handle namayesh stats
     */
    private void handleShowStats() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ GOZARESH HAYE AMARI ─", ConsoleColors.GRAY);
        
        try {
            String report = controller.getSummaryReport(loggedInUser);
            // print with colors
            for (String line : report.split("\n")) {
                if (line.contains("📊") || line.contains("Kol")) {
                    ConsoleColors.println("  " + line, ConsoleColors.GREEN);
                } else if (line.contains("✅")) {
                    ConsoleColors.println("  " + line, ConsoleColors.LIGHT_BLUE);
                } else if (line.contains("❌")) {
                    ConsoleColors.println("  " + line, ConsoleColors.RED);
                } else if (line.contains("📈")) {
                    ConsoleColors.println("  " + line, ConsoleColors.YELLOW);
                } else if (line.contains("🔥")) {
                    ConsoleColors.println("  " + line, ConsoleColors.ORANGE);
                } else if (line.contains("⏰")) {
                    ConsoleColors.println("  " + line, ConsoleColors.PURPLE);
                } else {
                    ConsoleColors.println("  " + line, ConsoleColors.GRAY);
                }
            }
            
            // Group by status
            ConsoleColors.println("\n  ┌─ GROUP BY VAZIAT ─", ConsoleColors.PURPLE);
            Map<Boolean, List<Task>> grouped = controller.getTasksGroupedByStatus(loggedInUser);
            
            grouped.forEach((status, tasks) -> {
                String vaziat = status ? "Anjam Shode" : "Anjam Nashode";
                ConsoleColors.println("  " + vaziat + ": " + tasks.size(), 
                    status ? ConsoleColors.GREEN : ConsoleColors.RED);
            });
            
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    // ========== FILTER HANDLERS ==========

    /**
     * handle filter tasks
     */
    private void handleFilterTasks() {
        System.out.println();
        ConsoleColors.printlnBold("  ┌─ FILTER TASK HA ─", ConsoleColors.PURPLE);
        ConsoleColors.println("  │", ConsoleColors.PURPLE);
        ConsoleColors.println("  │  1. Task Haye Anjam Nashode", ConsoleColors.YELLOW);
        ConsoleColors.println("  │  2. Task Haye Anjam Shode", ConsoleColors.GREEN);
        ConsoleColors.println("  │  3. Task Haye Overdue", ConsoleColors.RED);
        ConsoleColors.println("  │  4. Task Haye Payvand Bala (4+)", ConsoleColors.ORANGE);
        ConsoleColors.println("  │  0. Bargasht", ConsoleColors.GRAY);
        ConsoleColors.println("  │", ConsoleColors.PURPLE);
        ConsoleColors.printlnBold("  └────────────────────────────────", ConsoleColors.PURPLE);
        
        System.out.print("\n  Enter action: ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: filterPending(); break;
                case 2: filterCompleted(); break;
                case 3: filterOverdue(); break;
                case 4: filterHighPriority(); break;
                case 0: break;
                default: printError("Action namo'toufi!");
            }
        } catch (Exception e) {
            printError("Voridi ghalat!");
            scanner.nextLine();
        }
    }

    private void filterPending() {
        try {
            ArrayList<Task> tasks = controller.getPendingTasksStream(loggedInUser);
            ConsoleColors.println("\n  Task Haye Anjam Nashode:", ConsoleColors.YELLOW);
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski nist!", ConsoleColors.GRAY);
            } else {
                tasks.forEach(this::printTask);
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    private void filterCompleted() {
        try {
            ArrayList<Task> tasks = controller.getCompletedTasksStream(loggedInUser);
            ConsoleColors.println("\n  Task Haye Anjam Shode:", ConsoleColors.GREEN);
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski nist!", ConsoleColors.GRAY);
            } else {
                tasks.forEach(this::printTask);
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    private void filterOverdue() {
        try {
            ArrayList<Task> tasks = controller.getOverdueTasks(loggedInUser);
            ConsoleColors.println("\n  Task Haye Overdue:", ConsoleColors.RED);
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski nist!", ConsoleColors.GRAY);
            } else {
                tasks.forEach(this::printTask);
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    private void filterHighPriority() {
        try {
            ArrayList<Task> tasks = controller.getHighPriorityTasks(loggedInUser);
            ConsoleColors.println("\n  Task Haye Payvand Bala (4+):", ConsoleColors.ORANGE);
            if (tasks.isEmpty()) {
                ConsoleColors.println("  Hich taski nist!", ConsoleColors.GRAY);
            } else {
                tasks.forEach(this::printTask);
            }
        } catch (Exception e) {
            printError("Ghalat: " + e.getMessage());
        }
    }

    // ========== VIRG'OOL ==========

    /**
     * handle virg'ool
     */
    private void handleVirgool() {
        ConsoleColors.println("\n  Vaghti Virg'ool konid, data zakhire mishavad.", ConsoleColors.GRAY);
        ConsoleColors.println("  Mamnoon az estefade! 🎉", ConsoleColors.GREEN);
        loggedInUser = null;
    }

    // ========== HELPER METHODS ==========

    /**
     * print yek task
     */
    private void printTask(Task task) {
        String vaziat = task.checkAnjamShode() ? "✅" : "❌";
        String payvandEmoji = getPayvandEmoji(task.getPayvand());
        
        String color = task.checkAnjamShode() ? ConsoleColors.GREEN : 
                       task.getPayvand() >= 4 ? ConsoleColors.ORANGE : ConsoleColors.LIGHT_BLUE;
        
        ConsoleColors.println("  " + vaziat + " " + payvandEmoji + " " + task.getOnvan() + 
            " | Payvand: " + task.getPayvand() + " | Deadline: " + task.getMokalefZaman(), color);
    }

    /**
     * print task ba rank
     */
    private void printTaskWithRank(Task task, int rank) {
        String vaziat = task.checkAnjamShode() ? "✅" : "❌";
        String payvandEmoji = getPayvandEmoji(task.getPayvand());
        
        ConsoleColors.println("  " + rank + ". " + vaziat + " " + payvandEmoji + " " + task.getOnvan() + 
            " | Payvand: " + task.getPayvand(), ConsoleColors.YELLOW);
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
     * print success message
     */
    private void printSuccess(String message) {
        ConsoleColors.println("  ✅ " + message, ConsoleColors.GREEN);
    }

    /**
     * print error message
     */
    private void printError(String message) {
        ConsoleColors.println("  ❌ " + message, ConsoleColors.RED);
    }

    /**
     * print goodbye
     */
    private void printGoodbye() {
        System.out.println();
        ConsoleColors.printlnBold("  ╔════════════════════════════════════════════════════════════╗", ConsoleColors.BLUE);
        ConsoleColors.printlnBold("  ║     Khorooj az barname. Mamnoon az estefade!              ║", ConsoleColors.GREEN);
        ConsoleColors.printlnBold("  ║     " + PROJECT_NAME + " v" + VERSION + "                         ║", ConsoleColors.YELLOW);
        ConsoleColors.printlnBold("  ╚════════════════════════════════════════════════════════════╝", ConsoleColors.BLUE);
    }
}