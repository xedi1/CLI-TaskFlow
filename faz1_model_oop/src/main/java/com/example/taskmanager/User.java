package com.example.taskmanager;

/**
 * User class - represent yek karbar ba name va ramz obur
 * hame field ha private hastand va access only through setter/getter
 */
public class User {

    // ========== FIELD HA ==========
    private String nameKarbari;    // username
    private String ramzObur;       // password

    // ========== CONSTRUCTOR ==========

    /**
     * sazande - creates yek karbar jadid
     * @param nameKarbari username
     * @param ramzObur password
     */
    public User(String nameKarbari, String ramzObur) {
        this.nameKarbari = nameKarbari;
        this.ramzObur = ramzObur;
    }

    /**
     * sazande khali - creates empty user
     */
    public User() {
        this.nameKarbari = "";
        this.ramzObur = "";
    }

    // ========== GETTER HA ==========

    public String getNameKarbari() {
        return nameKarbari;
    }

    public String getRamzObur() {
        return ramzObur;
    }

    // ========== SETTER HA ==========

    public void setNameKarbari(String nameKarbari) {
        this.nameKarbari = nameKarbari;
    }

    public void setRamzObur(String ramzObur) {
        this.ramzObur = ramzObur;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        return "👤 Karbar: " + nameKarbari;
    }
}