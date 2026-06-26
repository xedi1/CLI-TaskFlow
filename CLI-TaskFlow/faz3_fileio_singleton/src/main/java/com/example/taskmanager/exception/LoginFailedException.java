package com.example.taskmanager.exception;

/**
 * LoginFailedException - unchecked exception baraye vaghti ke
 * login karbar ba moshkele jelo oomade (ramz obur غلط)
 */
public class LoginFailedException extends RuntimeException {

    // ========== FIELD HA ==========
    private String nameKarbari;    // username ke login anjam shod

    // ========== CONSTRUCTOR ==========

    /**
     * sazande ba message
     */
    public LoginFailedException(String message) {
        super(message);
        this.nameKarbari = "";
    }

    /**
     * sazande ba message va name karbari
     */
    public LoginFailedException(String message, String nameKarbari) {
        super(message);
        this.nameKarbari = nameKarbari;
    }

    // ========== GETTER ==========

    public String getNameKarbari() {
        return nameKarbari;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        String result = "❌ LoginFailedException: " + getMessage();
        if (!nameKarbari.isEmpty()) {
            result += " | Karbar: " + nameKarbari;
        }
        return result;
    }
}