package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;

/**
 * Task class - represent yek task ba title, priority, deadline va status
 * implements Comparable baraye estefade dar PriorityQueue
 * hame field ha private hastand va access only through setter/getter
 */
public class Task implements Comparable<Task> {

    // ========== FIELD HA ==========
    private String onvan;          // title task
    private int payvand;           // priority: 1 ta 5
    private String mokalefZaman;   // deadline - String ya LocalDate
    private boolean vaziatAnjam;   // done status

    // ========== CONSTANTS ==========
    private static final int PAYVAND_MIN = 1;
    private static final int PAYVAND_MAX = 5;

    // ========== CONSTRUCTOR ==========

    /**
     * sazande - creates yek task jadid
     */
    public Task(String onvan, int payvand, String mokalefZaman) throws InvalidTaskException {
        setOnvan(onvan);
        setPayvand(payvand);
        this.mokalefZaman = mokalefZaman;
        this.vaziatAnjam = false;
    }

    /**
     * sazande khali - creates empty task
     */
    public Task() {
        this.onvan = "";
        this.payvand = 1;
        this.mokalefZaman = "";
        this.vaziatAnjam = false;
    }

    // ========== COMPARABLE IMPLEMENTATION ==========

    /**
     * compareTo - baraye sorting bar asase payvand
     * payvand bala tar avval return mishavad
     * NOT: payvand 5 bala tarin, 1 paiin tarin
     */
    @Override
    public int compareTo(Task other) {
        // chon mikhahim payvand bala tar aval bashe, reverse mikone
        // ya'ni task ba payvand 5 ghabl az payvand 1 miad
        return other.payvand - this.payvand;
    }

    // ========== GETTER HA ==========

    public String getOnvan() {
        return onvan;
    }

    public int getPayvand() {
        return payvand;
    }

    public String getMokalefZaman() {
        return mokalefZaman;
    }

    public boolean isVaziatAnjam() {
        return vaziatAnjam;
    }

    // ========== SETTER HA ==========

    /**
     * set title task
     */
    public void setOnvan(String onvan) throws InvalidTaskException {
        if (onvan == null || onvan.trim().isEmpty()) {
            throw new InvalidTaskException("Onvan task nemitoane khali bashe!", "KHALI", "Title Empty");
        }
        this.onvan = onvan.trim();
    }

    /**
     * set priority ba validation
     */
    public void setPayvand(int payvand) throws InvalidTaskException {
        if (payvand < PAYVAND_MIN || payvand > PAYVAND_MAX) {
            throw new InvalidTaskException(
                "Olaviat bayad bein " + PAYVAND_MIN + " ta " + PAYVAND_MAX + " bashe!",
                String.valueOf(payvand),
                "Priority Out Of Range"
            );
        }
        this.payvand = payvand;
    }

    /**
     * set deadline
     */
    public void setMokalefZaman(String mokalefZaman) {
        this.mokalefZaman = mokalefZaman;
    }

    /**
     * set vaziat anjam
     */
    public void setVaziatAnjam(boolean vaziatAnjam) {
        this.vaziatAnjam = vaziatAnjam;
    }

    // ========== UTILITY METHODS ==========

    /**
     * anjam shodane task
     */
    public void anjamKon() {
        this.vaziatAnjam = true;
    }

    /**
     * be dast avordane task
     */
    public void beDastAvord() {
        this.vaziatAnjam = false;
    }

    /**
     * check mikone ke task anjam shode ya na
     */
    public boolean checkAnjamShode() {
        return vaziatAnjam;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        String vaziatStr = vaziatAnjam ? "Anjam Shode" : "Anjam Nashode";
        return "Task{onvan='" + onvan + "', Olaviat=" + payvand +
               ", mokalefZaman='" + mokalefZaman + "', vaziatAnjam=" + vaziatStr + "}";
    }

    /**
     * toString baraye namayesh ba emoji
     * @return string ba emoji
     */
    public String toStringPretty() {
        String vaziatStr = vaziatAnjam ? "Anjam Shode ✅" : "Anjam Nashode ❌";
        return "📋 Task: " + onvan + 
               " | Olaviat: " + payvand +
               " | Deadline: " + mokalefZaman + 
               " | Vaziat: " + vaziatStr;
    }
}