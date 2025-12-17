package Controllers;
import Models.Task;
import Services.ServiceContainer;
import utils.ValidationUtils;
import java.util.List;
import java.util.Map;

public class StreamController {
    private final ServiceContainer services;

    public StreamController(ServiceContainer services) {
        this.services = services;
    }

    public void showMenu() {
        boolean inMenu = true;
        while (inMenu) {
            displayMenu();
            int choice = ValidationUtils.readIntInRange("Enter choice: ", 1, 7);

            switch (choice) {
                case 1 -> services.getStreamService().displayAllTasks();
                case 2 -> filterByStatus();
                case 3 -> filterByCompletion();
                case 4 -> groupByStatus();
                case 5 -> services.getStreamService().displayTaskStatistics();
                case 6 -> displayProjectNames();
                case 7 -> inMenu = false;
            }
            if (choice != 7) ValidationUtils.waitForEnter();
        }
    }

    private void displayMenu() {
        System.out.println("\n=== STREAM OPERATIONS (Week 3) ===");
        System.out.println("1. View All Tasks (flatMap)");
        System.out.println("2. Filter Tasks by Status");
        System.out.println("3. Projects Above Completion %");
        System.out.println("4. Group Tasks by Status");
        System.out.println("5. Task Statistics");
        System.out.println("6. Project Names (map)");
        System.out.println("7. Back");
    }

    private void filterByStatus() {
        System.out.println("1. Pending  2. In Progress  3. Completed");
        int c = ValidationUtils.readIntInRange("Choice: ", 1, 3);
        String status = c == 1 ? "Pending" : c == 2 ? "In Progress" : "Completed";

        List<Task> tasks = services.getStreamService().getTasksByStatus(status);
        System.out.println("\n" + status + " tasks:");
        tasks.forEach(t -> System.out.println("  [" + t.getTaskId() + "] " + t.getTaskName()));
    }

    private void filterByCompletion() {
        double threshold = ValidationUtils.readDouble("Minimum completion %: ");
        services.getStreamService().displayProjectsAboveCompletion(threshold);
    }

    private void groupByStatus() {
        Map<String, List<Task>> grouped = services.getStreamService().groupTasksByStatus();
        grouped.forEach((status, tasks) -> {
            System.out.println("\n" + status + " (" + tasks.size() + "):");
            tasks.forEach(t -> System.out.println("  - " + t.getTaskName()));
        });
    }

    private void displayProjectNames() {
        System.out.println("\nProject Names:");
        services.getStreamService().getProjectNames().forEach(n -> System.out.println("  * " + n));
    }
}
