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

    // ========== AUTH METHODS ==========

    /**
     * login - check ramz obur
     * @param ramzOburVoridi ramz obur vared shode
     * @throws LoginFailedException agar ramz obur غلط bashe
     */
    public void login(String ramzOburVoridi) throws LoginFailedException {
        if (!this.ramzObur.equals(ramzOburVoridi)) {
            throw new LoginFailedException("Ramz obur غلط ast!", nameKarbari);
        }
        System.out.println("✅ Login movafaghiyat amal! Karbar: " + nameKarbari);
    }

    /**
     * change ramz obur
     * @param ramzQabe رمز قبلی
     * @param ramzJadid ramz جدید
     * @throws LoginFailedException agar ramz قبلی غلط bashe
     */
    public void changeRamzObur(String ramzQabe, String ramzJadid) throws LoginFailedException {
        if (!this.ramzObur.equals(ramzQabe)) {
            throw new LoginFailedException("Ramz قبلی غلط ast!", nameKarbari);
        }
        this.ramzObur = ramzJadid;
        System.out.println("✅ Ramz obur taghir kard shod!");
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        return "👤 Karbar: " + nameKarbari;
    }
}