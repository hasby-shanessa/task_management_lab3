package test;

import Models.HardwareProject;
import Models.Project;
import Models.SoftwareProject;
import Models.Task;
import Services.ProjectService;
import org.junit.jupiter.api.*;
import utils.FileUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilePersistenceTest {
    @BeforeEach
    void setUp() {
        Project.resetIdCounter();
        Task.resetIdCounter();
        // Clean up any existing test data
        FileUtils.deleteDataFile();
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
        FileUtils.deleteDataFile();
    }
    //save tests
    @Test
    @Order(1)
    @DisplayName("saveProjects should create data file")
    void testSaveCreatesFile() {
        ProjectService service = new ProjectService();
        service.createSoftwareProject("Test Project", "Desc", 5, "$1000", "Java", "Spring", "url");

        FileUtils.saveProjects(service.getAllProjects());

        assertTrue(FileUtils.dataFileExists());
    }

    @Test
    @Order(2)
    @DisplayName("saveProjects should handle empty project list")
    void testSaveEmptyProjects() {
        ProjectService service = new ProjectService();

        assertDoesNotThrow(() -> FileUtils.saveProjects(service.getAllProjects()));
    }
    @Test
    @Order(3)
    @DisplayName("Load projects should restore saved projects")
    void testSaveAndLoad() {
        // Create and save
        ProjectService service = new ProjectService();
        SoftwareProject sp = service.createSoftwareProject(
                "Alpha Project", "Description", 5, "$10000", "Java", "Spring", "github.com");
        sp.addTask(new Task("Task 1", "", "Pending"));
        sp.addTask(new Task("Task 2", "", "Completed"));

        FileUtils.saveProjects(service.getAllProjects());

        // Reset counters to simulate app restart
        Project.resetIdCounter();
        Task.resetIdCounter();

        // Load
        Map<String, Project> loaded = FileUtils.loadProjects();

        assertEquals(1, loaded.size());
        assertTrue(loaded.containsKey("P001"));

        Project loadedProject = loaded.get("P001");
        assertEquals("Alpha Project", loadedProject.getProjectName());
        assertEquals("Software", loadedProject.getProjectType());
        assertEquals(2, loadedProject.getTaskCount());
    }
    @Test
    @Order(5)
    @DisplayName("loadProjects should restore project type correctly")
    void testLoadProjectTypes() {
        // Create both types
        ProjectService service = new ProjectService();
        service.createSoftwareProject("Software One", "Desc", 3, "$5000", "Python", "Django", "url");
        service.createHardwareProject("Hardware One", "Desc", 4, "$8000", "Arduino", "Supplier");

        FileUtils.saveProjects(service.getAllProjects());

        // Load
        Map<String, Project> loaded = FileUtils.loadProjects();

        assertEquals(2, loaded.size());

        // Check types are preserved
        Project p1 = loaded.get("P001");
        Project p2 = loaded.get("P002");

        assertTrue(p1 instanceof SoftwareProject || p2 instanceof SoftwareProject);
        assertTrue(p1 instanceof HardwareProject || p2 instanceof HardwareProject);
    }
    @Test
    @Order(6)
    @DisplayName("loadProjects should restore task status correctly")
    void testLoadTaskStatus() {
        // Create project with tasks of different statuses
        ProjectService service = new ProjectService();
        SoftwareProject sp = service.createSoftwareProject(
                "Status Test", "Desc", 2, "$1000", "Java", "None", "url");
        sp.addTask(new Task("Pending Task", "", "Pending"));
        sp.addTask(new Task("In Progress Task", "", "In Progress"));
        sp.addTask(new Task("Completed Task", "", "Completed"));

        FileUtils.saveProjects(service.getAllProjects());

        // Load
        Map<String, Project> loaded = FileUtils.loadProjects();
        Project loadedProject = loaded.get("P001");

        assertEquals(3, loadedProject.getTaskCount());

        // Check statuses are preserved
        boolean hasPending = loadedProject.getTasks().stream().anyMatch(t -> t.getStatus().equals("Pending"));
        boolean hasInProgress = loadedProject.getTasks().stream().anyMatch(t -> t.getStatus().equals("In Progress"));
        boolean hasCompleted = loadedProject.getTasks().stream().anyMatch(t -> t.getStatus().equals("Completed"));

        assertTrue(hasPending);
        assertTrue(hasInProgress);
        assertTrue(hasCompleted);
    }

    //utility tests

    @Test
    @Order(7)
    @DisplayName("dataFileExists should return correct status")
    void testDataFileExists() {
        assertFalse(FileUtils.dataFileExists());

        ProjectService service = new ProjectService();
        service.createSoftwareProject("Test", "Desc", 1, "$100", "Java", "None", "url");
        FileUtils.saveProjects(service.getAllProjects());

        assertTrue(FileUtils.dataFileExists());
    }

    @Test
    @Order(8)
    @DisplayName("deleteDataFile should remove the file")
    void testDeleteDataFile() {
        // Create file
        ProjectService service = new ProjectService();
        service.createSoftwareProject("Test", "Desc", 1, "$100", "Java", "None", "url");
        FileUtils.saveProjects(service.getAllProjects());
        assertTrue(FileUtils.dataFileExists());

        // Delete
        boolean deleted = FileUtils.deleteDataFile();

        assertTrue(deleted);
        assertFalse(FileUtils.dataFileExists());
    }

}
