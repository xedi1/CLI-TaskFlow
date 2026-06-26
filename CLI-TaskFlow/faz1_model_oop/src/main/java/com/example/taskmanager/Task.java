package com.example.taskmanager;

/**
 * Task class - represent yek task ba title, priority, deadline va status
 * hame field ha private hastand va access only through setter/getter
 */
public class Task {

    // ========== FIELD HA ==========
    private String onvan;          // title task
    private int payvand;           // priority: 1 ta 5
    private String mokalefZaman;   // deadline - String ya LocalDate
    private boolean vaziatAnjam;   // done status

    // ========== CONSTRUCTOR ==========

    /**
     * sazande - creates yek task jadid
     */
    public Task(String onvan, int payvand, String mokalefZaman) {
        this.onvan = onvan;
        setPayvand(payvand);           // use setter for validation
        this.mokalefZaman = mokalefZaman;
        this.vaziatAnjam = false;      // default: anjam nashode
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

    public void setOnvan(String onvan) {
        this.onvan = onvan;
    }

    /**
     * set priority ba validation
     */
    public void setPayvand(int payvand) {
        if (payvand >= 1 && payvand <= 5) {
            this.payvand = payvand;
        } else {
            System.out.println("Error! Olaviat bayad bein 1 ta 5 bashe. Default: 1");
            this.payvand = 1;
        }
    }

    public void setMokalefZaman(String mokalefZaman) {
        this.mokalefZaman = mokalefZaman;
    }

    public void setVaziatAnjam(boolean vaziatAnjam) {
        this.vaziatAnjam = vaziatAnjam;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        String vaziatStr = vaziatAnjam ? "Anjam Shode ✅" : "Anjam Nashode ❌";
        return "📋 Task: " + onvan + 
               " | Olaviat: " + payvand +
               " | Deadline: " + mokalefZaman + 
               " | Vaziat: " + vaziatStr;
    }
}