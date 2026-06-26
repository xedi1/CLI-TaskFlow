package com.example.taskmanager;

import com.example.taskmanager.exception.LoginFailedException;

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

    // ========== AUTH METHODS ==========

    /**
     * login - check ramz obur
     */
    public void login(String ramzOburVoridi) throws LoginFailedException {
        if (!this.ramzObur.equals(ramzOburVoridi)) {
            throw new LoginFailedException("Ramz obur eshtebah ast!", nameKarbari);
        }
        System.out.println("✅ Login ba movafaghiyat! Karbar: " + nameKarbari);
    }

    /**
     * change ramz obur
     */
    public void changeRamzObur(String ramzQabe, String ramzJadid) throws LoginFailedException {
        if (!this.ramzObur.equals(ramzQabe)) {
            throw new LoginFailedException("Ramz gbali eshtebah ast!", nameKarbari);
        }
        this.ramzObur = ramzJadid;
        System.out.println("✅ Ramz obur taghir kard!");
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        return "👤 Karbar: " + nameKarbari;
    }
}