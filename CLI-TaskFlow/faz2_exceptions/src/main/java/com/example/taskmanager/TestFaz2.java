package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.LoginFailedException;
import com.example.taskmanager.exception.UserNotFoundException;

/**
 * TestFaz2 class - test class baraye phase 2
 * test exception handling ba try-catch
 */
public class TestFaz2 {

    public static void main(String[] args) {
        
        // ===== PRINT SERI ======
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚀 FAZ 2: EXCEPTION HANDLING - TEST                    ║");
        System.out.println("║     Validation & Custom Exceptions                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        DataStore dataStore = new DataStore();

        // ===== TEST 1: SABT KARBAR =====
        System.out.println("━━━━━━━━━━━━━━━━━━━━ TEST 1: SABT KARBAR ━━━━━━━━━━━━━━━━━━━━");
        dataStore.addKarbar(new User("Ali", "12345"));
        dataStore.addKarbar(new User("Sara", "abcdef"));

        // ===== TEST 2: ADD TASK NORMAL =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 2: ADD TASK NORMAL ━━━━━━━━━━━━━━━━━━━━");
        try {
            dataStore.addTask("Ali", "Sakhtan web site", 3, "2026-08-15");
            dataStore.addTask("season2.life", 5, "2026-08-20");
            dataStore.addTask("Tamiz karḋan otagh", 1, "2026-09-25");
            System.out.println("✅ Hame task ha ba movafaghiyat ezafe shodand!");
        } catch (InvalidTaskException | UserNotFoundException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 3: ADD TASK BA TITLE KHALI =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 3: TASK BA TITLE KHALI ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying to add task ba title khali...");
            dataStore.addTask("Ali", "", 3, "2025-03-01");
            System.out.println("❌ Inja na! exception pal mish!");
        } catch (InvalidTaskException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        } catch (UserNotFoundException e) {
            System.out.println("❌ User exception: " + e.getMessage());
        }

        // ===== TEST 4: ADD TASK BA PAYVAND KHALAGH =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 4: TASK BA PAYVAND KHALAGH ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying to add task ba payvand = 10...");
            dataStore.addTask("Ali", "Task ghale", 10, "2025-03-01");
            System.out.println("❌ Inja na! exception pal mish!");
        } catch (InvalidTaskException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        } catch (UserNotFoundException e) {
            System.out.println("❌ User exception: " + e.getMessage());
        }

        // ===== TEST 5: ADD TASK BARAYE KARBAR NAGOFTE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 5: TASK BARAYE KARBAR NAGOFTE ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying to add task baraye karbar 'Reza' ke exist nadarad...");
            dataStore.addTask("Reza", "Task reza", 2, "2025-03-01");
            System.out.println("❌ Inja na! exception pal mish!");
        } catch (InvalidTaskException e) {
            System.out.println("❌ Task exception: " + e.getMessage());
        } catch (UserNotFoundException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 6: LOGIN BA RAMZ OBUR DALIL =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 6: LOGIN BA RAMZ OBUR DALIL ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying login ba ramz obur غلط...");
            dataStore.login("Ali", "wrongPass");
            System.out.println("❌ Inja na! exception pal mish!");
        } catch (UserNotFoundException e) {
            System.out.println("❌ User exception: " + e.getMessage());
        } catch (LoginFailedException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 7: LOGIN BA KARBAR NAGOFTE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 7: LOGIN BA KARBAR NAGOFTE ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying login baraye karbar 'Nagofte'...");
            dataStore.login("Nagofte", "12345");
            System.out.println("❌ Inja na! exception pal mish!");
        } catch (UserNotFoundException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        } catch (LoginFailedException e) {
            System.out.println("❌ Login exception: " + e.getMessage());
        }

        // ===== TEST 8: LOGIN MOVAFAGH =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 8: LOGIN MOVAFAGH ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying login movafagh...");
            dataStore.login("Ali", "12345");
            System.out.println("✅ Login movafaghiyat amal!");
        } catch (UserNotFoundException | LoginFailedException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        }

        // ===== TEST 9: REMOVE KARBAR NAGOFTE =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 9: REMOVE KARBAR NAGOFTE ━━━━━━━━━━━━━━━━━━━━");
        try {
            System.out.println("  trying remove karbar 'Nagofte'...");
            dataStore.removeKarbar("Nagofte");
            System.out.println("❌ Inja na! exception pal mish!");
        } catch (UserNotFoundException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 10: SETTER EXCEPTIONS =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 10: SETTER EXCEPTIONS ━━━━━━━━━━━━━━━━━━━━");
        Task testTask = new Task();
        try {
            System.out.println("  trying setOnvan ba string khali...");
            testTask.setOnvan("   ");
        } catch (InvalidTaskException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        try {
            System.out.println("  trying setPayvand ba value 0...");
            testTask.setPayvand(0);
        } catch (InvalidTaskException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 11: GET TADAD TASK BA EXCEPTION =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 11: GET TADAD TASK ━━━━━━━━━━━━━━━━━━━━");
        try {
            int tedad = dataStore.getTedadTask("Ali");
            System.out.println("  Tedad task haye Ali: " + tedad);
            
            tedad = dataStore.getTedadTask("Nagofte");
            System.out.println("  Tedad task haye Nagofte: " + tedad);
        } catch (UserNotFoundException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 12: PRINT TASKS =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 12: PRINT TASKS ━━━━━━━━━━━━━━━━━━━━");
        try {
            dataStore.printTaskHayeKarbar("Ali");
            dataStore.printTaskHayeKarbar("Sara");
            dataStore.printTaskHayeKarbar("Nagofte");  // in exception mide
        } catch (UserNotFoundException e) {
            System.out.println("✅ Exception gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 13: MULTIPLE EXCEPTION HANDLING =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 13: MULTI CATCH ━━━━━━━━━━━━━━━━━━━━");
        try {
            // yek operation ke har 2 exception ro mitoane bede
            dataStore.addTask("Nagofte", "Task jadid", 3, "2025-03-01");
        } catch (InvalidTaskException e) {
            System.out.println("❌ Task ghalat: " + e.getMessage());
        } catch (UserNotFoundException e) {
            System.out.println("✅ UserNotFoundException gerefte shod!");
            System.out.println("   " + e.toString());
        }

        // ===== TEST 14: FINALLY BLOCK =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST 14: FINALLY BLOCK ━━━━━━━━━━━━━━━━━━━━");
        try {
            dataStore.login("Ali", "12345");
        } catch (UserNotFoundException | LoginFailedException e) {
            System.out.println("❌ Ghalat: " + e.getMessage());
        } finally {
            System.out.println("🔄 Finally block hamishe ejra mishavad!");
        }

        // ===== PROGRAM STILL RUNNING =====
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ PROGRAM BA OMID KHAMUSH NASHOD!                     ║");
        System.out.println("║     hame exception ha managed shodand va program edame dare║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}