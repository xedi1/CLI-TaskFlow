package com.example.taskmanager;

/**
 * ConsoleColors - ANSI escape codes baraye rang dad be console
 * 
 * in class rang haye zir ro support mikone:
 * - ABYI (Blue)
 * - SABZ (Green)  
 * - GHESMAT (Cyan)
 * - SURATI (Purple/Magenta)
 * - GERMY (Red)
 * - TUSI (Gray)
 * - KHAM (Brown/Cream)
 */
public class ConsoleColors {

    // ========== RESET ==========
    public static final String RESET = "\033[0m";       // Reset default

    // ========== RANG HA ==========
    
    // ABYI - Blue
    public static final String BLUE = "\033[94m";       // Blue
    public static final String DARK_BLUE = "\033[34m";  // Dark Blue
    public static final String LIGHT_BLUE = "\033[36m"; // Cyan (close to light blue)

    // SABZ - Green
    public static final String GREEN = "\033[92m";      // Green
    public static final String DARK_GREEN = "\033[32m"; // Dark Green
    public static final String LIGHT_GREEN = "\033[96m"; // Light Cyan

    // SURATI - Purple/Magenta
    public static final String PURPLE = "\033[95m";     // Purple/Magenta
    public static final String MAGENTA = "\033[35m";    // Magenta
    public static final String LIGHT_PURPLE = "\033[94m"; // Light Purple (Pink-ish)

    // GERMY - Red
    public static final String RED = "\033[91m";        // Red
    public static final String DARK_RED = "\033[31m";   // Dark Red

    // TUSI - Gray
    public static final String GRAY = "\033[90m";       // Gray
    public static final String LIGHT_GRAY = "\033[37m"; // Light Gray
    public static final String DARK_GRAY = "\033[30m";  // Dark Gray

    // KHAM - Brown/Cream (using yellow/orange combo)
    public static final String YELLOW = "\033[93m";     // Yellow (cream-ish)
    public static final String ORANGE = "\033[33m";     // Orange/Brown
    public static final String LIGHT_YELLOW = "\033[93m"; // Light Yellow

    // ========== STYLE HA ==========
    public static final String BOLD = "\033[1m";        // Bold
    public static final String UNDERLINE = "\033[4m";   // Underline
    public static final String BOLD_UNDERLINE = "\033[1;4m";

    // ========== BACKGROUND ==========
    public static final String BG_BLUE = "\033[44m";
    public static final String BG_GREEN = "\033[42m";
    public static final String BG_RED = "\033[41m";
    public static final String BG_YELLOW = "\033[43m";
    public static final String BG_PURPLE = "\033[45m";

    // ========== HELPER METHODS ==========

    /**
     * print rang shode
     */
    public static void print(String text, String rang) {
        System.out.print(rang + text + RESET);
    }

    /**
     * print line rang shode
     */
    public static void println(String text, String rang) {
        System.out.println(rang + text + RESET);
    }

    /**
     * print bold rang shode
     */
    public static void printBold(String text, String rang) {
        System.out.print(BOLD + rang + text + RESET);
    }

    /**
     * print bold line rang shode
     */
    public static void printlnBold(String text, String rang) {
        System.out.println(BOLD + rang + text + RESET);
    }

    /**
     * print underline rang shode
     */
    public static void printUnderline(String text, String rang) {
        System.out.print(UNDERLINE + rang + text + RESET);
    }

    /**
     * print underline line rang shode
     */
    public static void printlnUnderline(String text, String rang) {
        System.out.println(UNDERLINE + rang + text + RESET);
    }
}