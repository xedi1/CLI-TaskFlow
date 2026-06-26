package com.example.taskmanager;

/**
 * TestFaz1 class - test class ba main method
 * in class baraye test kardan amaliat faz 1 estefade mishavad
 */
public class TestFaz1 {

    public static void main(String[] args) {
        
        // ===== PRINT SERI ======
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     🚀 FAZ 1: MODEL & OOP CORE - TEST                      ║");
        System.out.println("║     Struct shai graft va Encapsulation                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // ===== TEST TASK CLASS =====
        System.out.println("━━━━━━━━━━━━━━━━━━━━ TEST TASK ━━━━━━━━━━━━━━━━━━━━");

        // sakhtan task ba constructor ba parametr
        Task task1 = new Task("Sakhtan web site", 3, "2026-08-15");
        Task task2 = new Task("season2.life", 5, "2026-08-20");
        Task task3 = new Task("Tamiz karḋan otagh", 1, "2026-09-25");

        System.out.println("\n📝 Task haye sakhte shode:");
        System.out.println("  Task 1: " + task1);
        System.out.println("  Task 2: " + task2);
        System.out.println("  Task 3: " + task3);

        // test setter/getter
        System.out.println("\n🔧 Test Setter/Getter:");
        task1.setOnvan("Saḱhtan web site - UPDATE");
        System.out.println("  Onvan task1 bad az update: " + task1.getOnvan());
        
        task2.setPayvand(2);
        System.out.println("  Payvand task2 bad az update: " + task2.getPayvand());

        task3.setVaziatAnjam(true);
        System.out.println("  Vaziat task3 bad az anjam: " + task3.isVaziatAnjam());

        // test validation dar setter payvand
        System.out.println("\n⚠️  Test Validation Payvand:");
        Task taskInvalid = new Task("Task Invalid", 10, "2025-03-01");
        System.out.println("  Payvand task ba value 10: " + taskInvalid.getPayvand() + " (bayad 1 bashe)");

        Task taskValid = new Task("Task Valid", 4, "2025-03-01");
        System.out.println("  Payvand task ba value 4: " + taskValid.getPayvand() + " (OK)");

        // ===== TEST USER CLASS =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST USER ━━━━━━━━━━━━━━━━━━━━");

        // sakhtan karbar ba constructor
        User karbar1 = new User("Ali", "12345");
        User karbar2 = new User("Sara", "abcdef");

        System.out.println("\n👥 Karbar haye sakhte shode:");
        System.out.println("  " + karbar1);
        System.out.println("  " + karbar2);

        // test setter/getter
        System.out.println("\n🔧 Test Setter/Getter:");
        karbar1.setRamzObur("newPass123");
        System.out.println("  Ramz obur karbar1 bad az update: " + karbar1.getRamzObur());

        // ===== TEST DATASTORE (HashMap - Heap Memory) =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ TEST DATASTORE ━━━━━━━━━━━━━━━━━━━━");

        DataStore dataStore = new DataStore();
        System.out.println("📦 DataStore jadid sakhte shod");

        // ezafe kardan task baraye karbar ha
        dataStore.addTask("Ali", task1);
        dataStore.addTask("Ali", task2);
        dataStore.addTask("Sara", task3);
        dataStore.addTask("Sara", taskInvalid);
        dataStore.addTask("Sara", taskValid);

        System.out.println("\n✅ Task haye ezafe shode be DataStore:");

        // check karbar exist
        System.out.println("  Ali exist? " + dataStore.karbarExist("Ali"));
        System.out.println("  Reza exist? " + dataStore.karbarExist("Reza"));

        // tedad task haye har karbar
        System.out.println("\n📊 Tedad task haye har karbar:");
        System.out.println("  Ali: " + dataStore.getTedadTask("Ali") + " task");
        System.out.println("  Sara: " + dataStore.getTedadTask("Sara") + " task");

        // print hame task haye har karbar
        dataStore.printTaskHayeKarbar("Ali");
        dataStore.printTaskHayeKarbar("Sara");

        // hame karbar ha
        System.out.println("\n👥 Hame karbar ha dar DataStore:");
        for (String name : dataStore.getHameKarbarHa()) {
            System.out.println("  - " + name + " (" + dataStore.getTedadTask(name) + " task)");
        }

        // ===== MEMORY CONCEPT =====
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━ MEMORY CONCEPT ━━━━━━━━━━━━━━━━━━━━");
        System.out.println("💡 Dars be dars:");
        System.out.println("  Stack: ref haye task1, task2, karbar1, dataStore");
        System.out.println("  Heap: Object haye Task, User, HashMap dar Heap zakhire mishand");
        System.out.println("  " + dataStore.toString());

        // ===== FINISH =====
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     ✅ TEST FAZ 1 BA MOVAFAGH PAYAN YAFT!                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}