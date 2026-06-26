package com.example.taskmanager;

import com.example.taskmanager.exception.InvalidTaskException;
import com.example.taskmanager.exception.UserNotFoundException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TaskControllerTest - Unit Tests baraye TaskController
 * 
 * in class 10+ test unit darad baraye test kardan:
 * - CRUD operations
 * - Stream API filters
 * - Edge cases
 * - Exception handling
 */
public class TaskControllerTest {

    // ========== FIELDS ==========
    private TaskController controller;
    private DatabaseManager dbManager;

    // ========== SETUP ==========

    @BeforeEach
    void setUp() {
        // har test ba data khali shorou mishavad
        dbManager = DatabaseManager.getInstance();
        dbManager.deleteFile();
        dbManager.getDataStore().getDataStoreKarbarHa().clear();
        dbManager.getDataStore().getDataStoreKarbarHa().clear();
        
        controller = new TaskController(dbManager);
        
        // sabt karbar
        dbManager.getDataStore().addKarbar(new User("TestUser", "12345"));
        dbManager.getDataStore().addKarbar(new User("User2", "pass"));
    }

    @AfterEach
    void tearDown() {
        // cleanup
        dbManager.deleteFile();
    }

    // ========== CRUD TESTS ==========

    @Test
    @DisplayName("Test 1: Create Task - Normal Case")
    void testCreateTaskNormal() throws InvalidTaskException, UserNotFoundException {
        // arrange
        String onvan = "Test Task";
        int payvand = 3;
        String deadline = "2026-12-31";

        // act
        Task task = controller.createTask("TestUser", onvan, payvand, deadline);

        // assert
        assertNotNull(task);
        assertEquals(onvan, task.getOnvan());
        assertEquals(payvand, task.getPayvand());
        assertEquals(deadline, task.getMokalefZaman());
        assertFalse(task.checkAnjamShode());
    }

    @Test
    @DisplayName("Test 2: Create Task - Invalid Priority")
    void testCreateTaskInvalidPriority() {
        // arrange
        String onvan = "Test Task";
        int payvandInValid = 10;  // ghalat
        String deadline = "2026-12-31";

        // act & assert
        assertThrows(InvalidTaskException.class, () -> {
            controller.createTask("TestUser", onvan, payvandInValid, deadline);
        });
    }

    @Test
    @DisplayName("Test 3: Create Task - Empty Title")
    void testCreateTaskEmptyTitle() {
        // act & assert
        assertThrows(InvalidTaskException.class, () -> {
            controller.createTask("TestUser", "", 3, "2026-12-31");
        });

        assertThrows(InvalidTaskException.class, () -> {
            controller.createTask("TestUser", "   ", 3, "2026-12-31");
        });
    }

    @Test
    @DisplayName("Test 4: Read Tasks - User Not Found")
    void testReadTasksUserNotFound() {
        // act & assert
        assertThrows(UserNotFoundException.class, () -> {
            controller.readAllTasks("NagofteUser");
        });
    }

    @Test
    @DisplayName("Test 5: Complete Task")
    void testCompleteTask() throws InvalidTaskException, UserNotFoundException {
        // arrange
        Task task = controller.createTask("TestUser", "Task for Complete", 3, "2026-12-31");

        // act
        assertFalse(task.checkAnjamShode());
        controller.completeTask("TestUser", "Task for Complete");

        // assert
        Task updatedTask = controller.readTask("TestUser", "Task for Complete");
        assertNotNull(updatedTask);
        assertTrue(updatedTask.checkAnjamShode());
    }

    @Test
    @DisplayName("Test 6: Delete Task")
    void testDeleteTask() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task for Delete", 3, "2026-12-31");
        assertEquals(1, controller.countAllTasks("TestUser"));

        // act
        controller.deleteTask("TestUser", "Task for Delete");

        // assert
        assertEquals(0, controller.countAllTasks("TestUser"));
    }

    // ========== STREAM API TESTS ==========

    @Test
    @DisplayName("Test 7: Pending Tasks Stream - Empty")
    void testPendingTasksEmpty() throws UserNotFoundException {
        // arrange - hich task niST

        // act
        ArrayList<Task> pending = controller.getPendingTasksStream("TestUser");

        // assert
        assertTrue(pending.isEmpty());
    }

    @Test
    @DisplayName("Test 8: Pending Tasks Stream - Mix")
    void testPendingTasksMix() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 5, "2026-12-31");
        
        controller.completeTask("TestUser", "Task 1");

        // act
        ArrayList<Task> pending = controller.getPendingTasksStream("TestUser");

        // assert
        assertEquals(2, pending.size());
    }

    @Test
    @DisplayName("Test 9: Completed Tasks Stream")
    void testCompletedTasksStream() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 5, "2026-12-31");
        
        controller.completeTask("TestUser", "Task 1");
        controller.completeTask("TestUser", "Task 2");

        // act
        ArrayList<Task> completed = controller.getCompletedTasksStream("TestUser");

        // assert
        assertEquals(2, completed.size());
    }

    @Test
    @DisplayName("Test 10: Group By Status")
    void testGroupByStatus() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 5, "2026-12-31");
        
        controller.completeTask("TestUser", "Task 1");

        // act
        Map<Boolean, List<Task>> grouped = controller.getTasksGroupedByStatus("TestUser");

        // assert
        assertEquals(2, grouped.size());  // true & false
        assertEquals(1, grouped.get(true).size());   // completed
        assertEquals(2, grouped.get(false).size());  // pending
    }

    @Test
    @DisplayName("Test 11: High Priority Tasks")
    void testHighPriorityTasks() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 2, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 4", 5, "2026-12-31");

        // act
        ArrayList<Task> highPriority = controller.getHighPriorityTasks("TestUser");

        // assert
        assertEquals(2, highPriority.size());
        assertTrue(highPriority.stream().allMatch(t -> t.getPayvand() >= 4));
    }

    @Test
    @DisplayName("Test 12: Completion Percentage")
    void testCompletionPercentage() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 5, "2026-12-31");
        controller.createTask("TestUser", "Task 4", 5, "2026-12-31");
        
        controller.completeTask("TestUser", "Task 1");
        controller.completeTask("TestUser", "Task 2");

        // act
        double percentage = controller.getCompletionPercentageStream("TestUser");

        // assert
        assertEquals(50.0, percentage, 0.01);
    }

    @Test
    @DisplayName("Test 13: Average Priority")
    void testAveragePriority() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 2, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 5, "2026-12-31");

        // act
        double avg = controller.getAveragePriority("TestUser");

        // assert
        assertEquals(3.67, avg, 0.01);  // (2+4+5)/3 = 3.67
    }

    @Test
    @DisplayName("Test 14: Task Count by Priority")
    void testTaskCountByPriority() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 3", 4, "2026-12-31");
        controller.createTask("TestUser", "Task 4", 5, "2026-12-31");

        // act
        Map<Integer, Long> countMap = controller.getTaskCountByPriority("TestUser");

        // assert
        assertEquals(2, countMap.get(3));
        assertEquals(1, countMap.get(4));
        assertEquals(1, countMap.get(5));
    }

    @Test
    @DisplayName("Test 15: All Tasks Completed - False")
    void testAreAllTasksCompletedFalse() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.completeTask("TestUser", "Task 1");

        // act
        boolean allCompleted = controller.areAllTasksCompleted("TestUser");

        // assert
        assertFalse(allCompleted);
    }

    @Test
    @DisplayName("Test 16: All Tasks Completed - True")
    void testAreAllTasksCompletedTrue() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.completeTask("TestUser", "Task 1");
        controller.completeTask("TestUser", "Task 2");

        // act
        boolean allCompleted = controller.areAllTasksCompleted("TestUser");

        // assert
        assertTrue(allCompleted);
    }

    // ========== EDGE CASES ==========

    @Test
    @DisplayName("Test 17: Priority Boundary - Min (1)")
    void testPriorityBoundaryMin() throws InvalidTaskException, UserNotFoundException {
        // act
        Task task = controller.createTask("TestUser", "Min Priority", 1, "2026-12-31");

        // assert
        assertEquals(1, task.getPayvand());
    }

    @Test
    @DisplayName("Test 18: Priority Boundary - Max (5)")
    void testPriorityBoundaryMax() throws InvalidTaskException, UserNotFoundException {
        // act
        Task task = controller.createTask("TestUser", "Max Priority", 5, "2026-12-31");

        // assert
        assertEquals(5, task.getPayvand());
    }

    @Test
    @DisplayName("Test 19: Empty User Tasks")
    void testEmptyUserTasks() throws UserNotFoundException {
        // act
        ArrayList<Task> tasks = controller.readAllTasks("User2");

        // assert
        assertTrue(tasks.isEmpty());
    }

    @Test
    @DisplayName("Test 20: Summary Report")
    void testSummaryReport() throws InvalidTaskException, UserNotFoundException {
        // arrange
        controller.createTask("TestUser", "Task 1", 3, "2026-12-31");
        controller.createTask("TestUser", "Task 2", 4, "2026-12-31");
        controller.completeTask("TestUser", "Task 1");

        // act
        String report = controller.getSummaryReport("TestUser");

        // assert
        assertNotNull(report);
        assertTrue(report.contains("SUMMARY REPORT"));
        assertTrue(report.contains("TestUser"));
        assertTrue(report.contains("2"));  // kol
    }
}