package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MenuInteraction - Simulates user interaction ba menu
 * in class main thread ra simulate mikone
 * va daroniri nadare (System.out nadarad)
 */
public class MenuInteraction {

    // ========== FIELD HA ==========
    private TaskController controller;
    private String nameKarbari;
    private boolean active;
    private List<String> actionLog;  // log baraye test

    // ========== CONSTRUCTOR ==========

    /**
     * sazande
     * @param controller task controller
     * @param nameKarbari username
     */
    public MenuInteraction(TaskController controller, String nameKarbari) {
        this.controller = controller;
        this.nameKarbari = nameKarbari;
        this.active = true;
        this.actionLog = new ArrayList<>();
    }

    // ========== SIMULATE MENU ==========

    /**
     * simulate yek menu interaction
     * @param actionNumber shomare action (1-5)
     * @return natije action
     */
    public String simulateAction(int actionNumber) {
        if (!active) {
            return "Menu ghadim nemitoane action begirad!";
        }

        try {
            switch (actionNumber) {
                case 1:
                    return simulateCreateTask();
                case 2:
                    return simulateReadTasks();
                case 3:
                    return simulateCompleteTask();
                case 4:
                    return simulateDeleteTask();
                case 5:
                    return simulateAddRandomTask();
                default:
                    return "Action namo'toufi!";
            }
        } catch (Exception e) {
            return "Xatam: " + e.getMessage();
        }
    }

    /**
     * simulate sakhtan task jadid
     */
    private String simulateCreateTask() throws InvalidTaskException, UserNotFoundException {
        Random random = new Random();
        String[] onvanha = {
            "Tamrin jadid", "Proje paiini", "Email javab dadan",
            "Jalase pardakht", "Raporte ruzane", "Call ba karbar"
        };
        String onvan = onvanha[random.nextInt(onvanha.length)] + " " + System.currentTimeMillis() % 1000;
        int payvand = random.nextInt(5) + 1;
        String deadline = "2026-" + String.format("%02d", random.nextInt(12) + 1) + "-" + 
                          String.format("%02d", random.nextInt(28) + 1);

        controller.createTask(nameKarbari, onvan, payvand, deadline);
        actionLog.add("CREATE: " + onvan);
        return "Task jadid sakhte shod: " + onvan;
    }

    /**
     * simulate khandane task ha
     */
    private String simulateReadTasks() throws UserNotFoundException {
        ArrayList<Task> tasks = controller.readAllTasks(nameKarbari);
        actionLog.add("READ: " + tasks.size() + " task");
        return "Task haye " + nameKarbari + ": " + tasks.size() + " adad";
    }

    /**
     * simulate complete kardan yek task
     */
    private String simulateCompleteTask() throws UserNotFoundException {
        ArrayList<Task> pendingTasks = controller.getPendingTasksSync(nameKarbari);
        
        if (pendingTasks.isEmpty()) {
            actionLog.add("COMPLETE: hich task pending nist");
            return "Hich task pending nist!";
        }

        Task task = pendingTasks.get(0);
        controller.completeTask(nameKarbari, task.getOnvan());
        actionLog.add("COMPLETE: " + task.getOnvan());
        return "Task anjam shod: " + task.getOnvan();
    }

    /**
     * simulate hazf yek task
     */
    private String simulateDeleteTask() throws UserNotFoundException {
        ArrayList<Task> tasks = controller.readAllTasks(nameKarbari);
        
        if (tasks.isEmpty()) {
            actionLog.add("DELETE: hich task nist");
            return "Hich task nist!";
        }

        Task task = tasks.get(0);
        controller.deleteTask(nameKarbari, task.getOnvan());
        actionLog.add("DELETE: " + task.getOnvan());
        return "Task hazf shod: " + task.getOnvan();
    }

    /**
     * simulate add task ba payvand bala
     */
    private String simulateAddRandomTask() throws InvalidTaskException, UserNotFoundException {
        Random random = new Random();
        String[] onvanha = {
            "Moshahede video amoozesh", "Khosousiat paiini", 
            "Support karbar VIP", "Fix bug critical"
        };
        String onvan = onvanha[random.nextInt(onvanha.length)] + " " + System.currentTimeMillis() % 100;
        int payvand = random.nextInt(2) + 4;  // 4 ya 5
        String deadline = "2026-0" + (random.nextInt(6) + 1) + "-15";

        controller.createTask(nameKarbari, onvan, payvand, deadline);
        actionLog.add("CREATE HIGH: " + onvan + " (payvand=" + payvand + ")");
        return "Task ba payvand bala sakhte shod: " + onvan;
    }

    // ========== GETTER / SETTER ==========

    /**
     * get action log
     * @return list action ha
     */
    public List<String> getActionLog() {
        return new ArrayList<>(actionLog);
    }

    /**
     * clear action log
     */
    public void clearLog() {
        actionLog.clear();
    }

    /**
     * set active/inactive
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * check active
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * get name karbari
     * @return nameKarbari
     */
    public String getNameKarbari() {
        return nameKarbari;
    }
}