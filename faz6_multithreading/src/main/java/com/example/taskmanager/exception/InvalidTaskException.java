package com.example.taskmanager.exception;

/**
 * InvalidTaskException - checked exception baraye vaghti ke
 * vorudi haye task natije mokhtalef bashad
 * 
 * masalan: title khali, payvand khareg az range 1-5
 */
public class InvalidTaskException extends Exception {

    // ========== FIELD HA ==========
    private String onvanGhalat;    // title ke ghalat bud
    private String noeGhalat;      // noe ghalat

    // ========== CONSTRUCTOR ==========

    /**
     * sazande ba message
     * @param message payam ghalat
     */
    public InvalidTaskException(String message) {
        super(message);
        this.onvanGhalat = "";
        this.noeGhalat = "";
    }

    /**
     * sazande ba message va onvan ghalat
     * @param message payam ghalat
     * @param onvanGhalat title task ke ghalat bud
     */
    public InvalidTaskException(String message, String onvanGhalat) {
        super(message);
        this.onvanGhalat = onvanGhalat;
        this.noeGhalat = "";
    }

    /**
     * sazande kamel ba hame detail ha
     * @param message payam ghalat
     * @param onvanGhalat title task ke ghalat bud
     * @param noeGhalat noe ghalat
     */
    public InvalidTaskException(String message, String onvanGhalat, String noeGhalat) {
        super(message);
        this.onvanGhalat = onvanGhalat;
        this.noeGhalat = noeGhalat;
    }

    // ========== GETTER HA ==========

    public String getOnvanGhalat() {
        return onvanGhalat;
    }

    public String getNoeGhalat() {
        return noeGhalat;
    }

    // ========== TO STRING ==========

    @Override
    public String toString() {
        String result = "❌ InvalidTaskException: " + getMessage();
        if (!onvanGhalat.isEmpty()) {
            result += " | Onvan Ghalat: " + onvanGhalat;
        }
        if (!noeGhalat.isEmpty()) {
            result += " | Noe Ghalat: " + noeGhalat;
        }
        return result;
    }
}