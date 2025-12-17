package test;

import Interfaces.TaskFilter;
import Models.HardwareProject;
import Models.Project;
import Models.SoftwareProject;
import Models.Task;
import Services.ProjectService;
import Services.StreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamOperationsTest {
    private ProjectService projectService;
    private StreamService streamService;

    @BeforeEach
    void setUp(){
        Project.resetIdCounter();
        Task.resetIdCounter();

        projectService = new ProjectService();
        streamService = new StreamService(projectService);
        SoftwareProject sp1 = projectService.createSoftwareProject("Web App", "Description", 5, "$10000", "Java", "Spring", "github.com");
        sp1.addTask(new Task("Design DB", "", "Completed"));
        sp1.addTask(new Task("Build API", "", "Completed"));
        sp1.addTask(new Task("Write Tests", "", "Pending"));

        SoftwareProject sp2 = projectService.createSoftwareProject("Mobile App", "Description", 3, "$8000", "Kotlin", "Android", "github.com");
        sp2.addTask(new Task("UI Design", "", "Completed"));
        sp2.addTask(new Task("Backend", "", "In Progress"));

        HardwareProject hp1 = projectService.createHardwareProject("IoT Sensor", "Description", 4, "$15000", "Arduino", "Supplier A");
        hp1.addTask(new Task("Circuit Design", "", "Completed"));
        hp1.addTask(new Task("Assembly", "", "Pending"));
    }
    @Test
    @DisplayName("getProjectsByType should filter Software projects")
    void testFilterSoftwareProjects() {
        List<Project> software = streamService.getProjectsByType("Software");

        assertEquals(2, software.size());
        assertTrue(software.stream().allMatch(p -> p.getProjectType().equals("Software")));
    }

    @Test
    @DisplayName("getProjectsByType should filter Hardware projects")
    void testFilterHardwareProjects() {
        List<Project> hardware = streamService.getProjectsByType("Hardware");

        assertEquals(1, hardware.size());
        assertEquals("Hardware", hardware.get(0).getProjectType());
    }

    @Test
    @DisplayName("getProjectsAboveCompletion should filter by percentage")
    void testFilterByCompletion() {
        List<Project> above60 = streamService.getProjectsAboveCompletion(60.0);

        assertEquals(1, above60.size());
        assertEquals("Web App", above60.get(0).getProjectName());
    }
    @Test
    @DisplayName("getTasksByStatus should filter tasks correctly")
    void testFilterTasksByStatus() {
        List<Task> pending = streamService.getTasksByStatus("Pending");
        List<Task> completed = streamService.getTasksByStatus("Completed");

        assertEquals(2, pending.size());  // Write Tests + Assembly
        assertEquals(4, completed.size()); // Design DB, Build API, UI Design, Circuit Design
    }
    @Test
    @DisplayName("getAllTasks should flatten all tasks from all projects")
    void testFlatMapAllTasks() {
        List<Task> allTasks = streamService.getAllTasks();

        // 3 + 2 + 2 = 7 tasks total
        assertEquals(7, allTasks.size());
    }

    @Test
    @DisplayName("getCompletedTasksCount should count using filter and count")
    void testCountCompletedTasks() {
        long completed = streamService.getCompletedTasksCount();

        assertEquals(4, completed);
    }
    @Test
    @DisplayName("getProjectNames should map projects to names")
    void testMapToNames() {
        List<String> names = streamService.getProjectNames();

        assertEquals(3, names.size());
        assertTrue(names.contains("Web App"));
        assertTrue(names.contains("Mobile App"));
        assertTrue(names.contains("IoT Sensor"));
    }
    @Test
    @DisplayName("countProjectsByType should group and count")
    void testGroupByType() {
        Map<String, Long> counts = streamService.countProjectsByType();

        assertEquals(2, counts.get("Software"));
        assertEquals(1, counts.get("Hardware"));
    }

    @Test
    @DisplayName("groupTasksByStatus should group tasks correctly")
    void testGroupTasksByStatus() {
        Map<String, List<Task>> grouped = streamService.groupTasksByStatus();

        assertEquals(2, grouped.get("Pending").size());
        assertEquals(4, grouped.get("Completed").size());
        assertEquals(1, grouped.get("In Progress").size());
    }
    @Test
    @DisplayName("TaskFilter with lambda should filter tasks")
    void testTaskFilterLambda() {
        // Using lambda expression
        TaskFilter pendingFilter = task -> task.getStatus().equals("Pending");

        List<Task> pending = streamService.filterTasks(pendingFilter);

        assertEquals(2, pending.size());
        assertTrue(pending.stream().allMatch(t -> t.getStatus().equals("Pending")));
    }

    @Test
    @DisplayName("TaskFilter with method reference should filter tasks")
    void testTaskFilterMethodReference() {
        // Using method reference
        TaskFilter completedFilter = Task::isComplete;

        List<Task> completed = streamService.filterTasks(completedFilter);

        assertEquals(4, completed.size());
    }
    @Test
    @DisplayName("Empty project list should return empty results")
    void testEmptyProjectList() {
        ProjectService emptyService = new ProjectService();
        StreamService emptyStream = new StreamService(emptyService);

        assertTrue(emptyStream.getAllTasks().isEmpty());
        assertTrue(emptyStream.getCompletedProjects().isEmpty());
        assertEquals(0, emptyStream.getCompletedTasksCount());
    }
}
