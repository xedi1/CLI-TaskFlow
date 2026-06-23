package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;

/**
 * Task class - represent yek task ba title, priority, deadline va status
 * hame field ha private hastand va access only through setter/getter
 * hame method ha exception palkhand mikond
 */
public class Task {

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
     * @param onvan title task (nemitoone khali bashe)
     * @param payvand priority (1-5)
     * @param mokalefZaman deadline
     * @throws InvalidTaskException agar vorudi ghalat bashad
     */
    public Task(String onvan, int payvand, String mokalefZaman) throws InvalidTaskException {
        setOnvan(onvan);                           // throws exception agar khali bashe
        setPayvand(payvand);                       // throws exception agar range ghalat bashe
        this.mokalefZaman = mokalefZaman;
        this.vaziatAnjam = false;                  // default: anjam nashode
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

    /**
     * set title task
     * @param onvan title jadid (nemitoone khali bashe)
     * @throws InvalidTaskException agar title khali bashe
     */
    public void setOnvan(String onvan) throws InvalidTaskException {
        if (onvan == null || onvan.trim().isEmpty()) {
            throw new InvalidTaskException("Onvan task nemitoane khali bashe!", "KHALI", "Title Empty");
        }
        this.onvan = onvan.trim();
    }

    /**
     * set priority ba validation
     * @param payvand bayad bein PAYVAND_MIN ta PAYVAND_MAX bashe
     * @throws InvalidTaskException agar payvand khareg az range bashe
     */
    public void setPayvand(int payvand) throws InvalidTaskException {
        if (payvand < PAYVAND_MIN || payvand > PAYVAND_MAX) {
            throw new InvalidTaskException(
                "Payvand bayad bein " + PAYVAND_MIN + " ta " + PAYVAND_MAX + " bashe!",
                String.valueOf(payvand),
                "Priority Out Of Range"
            );
        }
        this.payvand = payvand;
    }

    /**
     * set deadline
     * @param mokalefZaman deadline jadid
     */
    public void setMokalefZaman(String mokalefZaman) {
        this.mokalefZaman = mokalefZaman;
    }

    /**
     * set vaziat anjam
     * @param vaziatAnjam vaziat jadid
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
     * @return true agar anjam shode bashe
     */
    public boolean checkAnjamShode() {
        return vaziatAnjam;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        String vaziatStr = vaziatAnjam ? "Anjam Shode ✅" : "Anjam Nashode ❌";
        return "📋 Task: " + onvan + 
               " | Payvand: " + payvand + 
               " | Deadline: " + mokalefZaman + 
               " | Vaziat: " + vaziatStr;
    }
}