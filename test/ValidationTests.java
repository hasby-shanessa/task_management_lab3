package test;

import Models.Project;
import Models.SoftwareProject;
import Models.Task;
import Services.ProjectService;
import Services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.exceptions.EmptyProjectException;
import utils.exceptions.InvalidInputException;
import utils.exceptions.TaskNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {
    private ProjectService projectService;
    @BeforeEach
    void setUp(){
        Project.resetIdCounter();
        Task.resetIdCounter();
        projectService = new ProjectService();
    }

    //invalid input tests
    @Test
    @DisplayName("Empty project name should throw InvalidInputException")
    void testEmptyProjectName() {
        assertThrows(InvalidInputException.class, () -> {
            projectService.createSoftwareProject(
                    "",  // Empty name
                    "Desc", 5, "$1000", "Java", "Spring", "url"
            );
        });
    }

    @Test
    @DisplayName("Negative team size should throw InvalidInputException")
    void testNegativeTeamSize() {
        assertThrows(InvalidInputException.class, () -> {
            projectService.createSoftwareProject(
                    "Project", "Desc",
                    -5,  // Negative size
                    "$1000", "Java", "Spring", "url"
            );
        });
    }

    @Test
    @DisplayName("Zero team size should throw InvalidInputException")
    void testZeroTeamSize() {
        assertThrows(InvalidInputException.class, () -> {
            projectService.createSoftwareProject(
                    "Project", "Desc",
                    0,  // Zero size
                    "$1000", "Java", "Spring", "url"
            );
        });
    }
//TODO::
    // task not found tests
    @Test
    @DisplayName("Finding non-existent task should throw TaskNotFoundException")
    void testTaskNotFound() {
        TaskService taskService = new TaskService(projectService);

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.findTaskById("T999");
        });
    }

    @Test
    @DisplayName("Removing non-existent task should throw TaskNotFoundException")
    void testRemoveTaskNotFound() {
        TaskService taskService = new TaskService(projectService);

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.removeTask("T999");
        });
    }

    //empty project tests
    @Test
    @DisplayName("Finding non-existent project should throw EmptyProjectException")
    void testProjectNotFound() {
        assertThrows(EmptyProjectException.class, () -> {
            projectService.findProjectById("P999");
        });
    }

    @Test
    @DisplayName("Deleting non-existent project should throw EmptyProjectException")
    void testDeleteProjectNotFound() {
        assertThrows(EmptyProjectException.class, () -> {
            projectService.deleteProject("P999");
        });
    }

    //valid input tests
    @Test
    @DisplayName("Valid inputs should create project successfully")
    void testValidInputs() {
        assertDoesNotThrow(() -> {
            SoftwareProject project = projectService.createSoftwareProject(
                    "Valid Project", "Desc", 5, "$1000", "Java", "Spring", "url"
            );
            assertNotNull(project);
        });
    }

    @Test
    @DisplayName("Valid task status should be accepted")
    void testValidStatus() {
        Task task = new Task("Task", "", "Pending");

        assertTrue(task.setStatus("In Progress"));
        assertTrue(task.setStatus("Completed"));
        assertTrue(task.setStatus("Pending"));
    }

    @Test
    @DisplayName("Invalid task status should be rejected")
    void testInvalidStatus() {
        Task task = new Task("Task", "", "Pending");

        assertFalse(task.setStatus("Done"));
        assertFalse(task.setStatus("Finished"));
        assertFalse(task.setStatus(""));
    }
}
