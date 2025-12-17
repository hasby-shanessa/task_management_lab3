package Controllers;
import Services.ServiceContainer;
import utils.ConsoleMenu;
import utils.ValidationUtils;

public class ConcurrencyController {
    private final ServiceContainer services;

    public ConcurrencyController(ServiceContainer services) {
        this.services = services;
    }

    public void showMenu() {
        boolean inMenu = true;
        while (inMenu) {
            displayMenu();
            int choice = ValidationUtils.readIntInRange("Enter choice: ", 1, 4);

            switch (choice) {
                case 1 -> runThreadPoolDemo();
                case 2 -> runParallelStreamDemo();
                case 3 -> services.getConcurrencyService().displayConcurrencyInfo();
                case 4 -> inMenu = false;
            }
            if (choice != 4) ValidationUtils.waitForEnter();
        }
    }

    private void displayMenu() {
        System.out.println("\n=== CONCURRENCY DEMO (Week 3) ===");
        System.out.println("1. Thread Pool Demo");
        System.out.println("2. Parallel Stream Demo");
        System.out.println("3. Concurrency Info");
        System.out.println("4. Back");
    }

    private void runThreadPoolDemo() {
        if (services.getProjectService().getTotalTaskCount() == 0) {
            ConsoleMenu.showInfo("No tasks available. Create some tasks first.");
        } else {
            services.getConcurrencyService().simulateConcurrentUpdates();
        }
    }

    private void runParallelStreamDemo() {
        if (services.getProjectService().getTotalTaskCount() == 0) {
            ConsoleMenu.showInfo("No tasks available. Create some tasks first.");
        } else {
            services.getConcurrencyService().parallelStreamUpdate();
        }
    }
}
