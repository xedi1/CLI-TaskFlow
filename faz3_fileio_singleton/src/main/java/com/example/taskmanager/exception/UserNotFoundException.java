package com.example.taskmanager.exception;

/**
 * UserNotFoundException - unchecked exception baraye vaghti ke
 * karbar peyda nashavad (masalan login ya assign task)
 * 
 * in yek RuntimeException hast ke niaz be declare kardan dar method signature nadarad
 */
public class UserNotFoundException extends RuntimeException {

    // ========== FIELD HA ==========
    private String nameKarbariGhalat;    // username ke peyda nashod

    // ========== CONSTRUCTOR ==========

    /**
     * sazande ba message
     * @param message payam ghalat
     */
    public UserNotFoundException(String message) {
        super(message);
        this.nameKarbariGhalat = "";
    }

    /**
     * sazande ba message va name karbari
     * @param message payam ghalat
     * @param nameKarbariGhalat username ke peyda nashod
     */
    public UserNotFoundException(String message, String nameKarbariGhalat) {
        super(message);
        this.nameKarbariGhalat = nameKarbariGhalat;
    }

    // ========== GETTER ==========

    public String getNameKarbariGhalat() {
        return nameKarbariGhalat;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        String result = "❌ UserNotFoundException: " + getMessage();
        if (!nameKarbariGhalat.isEmpty()) {
            result += " | Karbar: " + nameKarbariGhalat;
        }
        return result;
    }
}