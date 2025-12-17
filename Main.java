import Services.*;
import Controllers.*;
import utils.FileUtils;
import utils.ValidationUtils;
import java.util.Map;
import Models.Project;
public class Main {
    public static void main(String[] args) {
        // Initialize services
        ServiceContainer services = initializeServices();

        // Create and run application
        ApplicationController app = new ApplicationController(services);
        app.run();

        // Shutdown
        shutdown(services);
    }

    private static ServiceContainer initializeServices() {
        System.out.println("Initializing system...");

        Map<String, Project> loadedProjects = FileUtils.loadProjects();

        ProjectService projectService = new ProjectService(loadedProjects);
        TaskService taskService = new TaskService(projectService);
        ReportService reportService = new ReportService(projectService);
        StreamService streamService = new StreamService(projectService);
        ConcurrencyService concurrencyService = new ConcurrencyService(projectService);

        System.out.println("System ready!\n");

        return new ServiceContainer(projectService, taskService, reportService, streamService, concurrencyService
        );
    }

    private static void shutdown(ServiceContainer services) {
        System.out.println("\nSaving data...");
        FileUtils.saveProjects(services.getProjectService().getAllProjects());

        System.out.println("\nThank you for using the system! Goodbye!\n");
        ValidationUtils.closeScanner();
    }
}