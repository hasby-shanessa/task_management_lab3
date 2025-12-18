package Controllers;
import Models.*;
import Services.ServiceContainer;
import utils.ValidationUtils;

public class ApplicationController {
    private final ServiceContainer services;
    private final ProjectController projectController;
    private final TaskController taskController;
    private final StreamController streamController;
    private final ConcurrencyController concurrencyController;

    private User[] users;
    private int userCount;
    private User currentUser;
    private static final int MAX_USERS = 10;

    public ApplicationController(ServiceContainer services) {
        this.services = services;
        this.projectController = new ProjectController(services);
        this.taskController = new TaskController(services);
        this.streamController = new StreamController(services);
        this.concurrencyController = new ConcurrencyController(services);

        initializeUsers();
    }

    private void initializeUsers() {
        users = new User[MAX_USERS];
        users[0] = new AdminUser("hasby.admin", "Hasby umu");
        users[1] = new RegularUser("Joy.dev", "Joy Gift", "QA");
        users[2] = new RegularUser("Divine.pm", "Divine Bay", "Product");
        userCount = 3;
    }

    public void run() {
        showWelcome();
        login();
        runMainLoop();
    }

    private void showWelcome() {
        System.out.println();
        System.out.println("--********************************************************************--");
        System.out.println("||              WELCOME TO JAVA TASK MANAGEMENT SYSTEM                 ||");
        System.out.println("||                         Week 3 Edition                              ||");
        System.out.println("---*******************************************************************---");
        System.out.println();
    }

    private void login() {
        System.out.println("Select your user account:");
        for (int i = 0; i < userCount; i++) {
            System.out.println((i + 1) + ". " + users[i].getDisplayHeader());
        }
        int choice = ValidationUtils.readIntInRange("Enter your choice: ", 1, userCount);
        currentUser = users[choice - 1];
        System.out.println("\nWelcome, " + currentUser.getFullName() + "!");
        ValidationUtils.waitForEnter();
    }

    private void runMainLoop() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = ValidationUtils.readIntInRange("Enter your choice: ", 1, 7);

            switch (choice) {
                case 1 -> projectController.manageProjects(currentUser);
                case 2 -> taskController.manageTasks();
                case 3 -> services.getReportService().displayStatusReport();
                case 4 -> streamController.showMenu();
                case 5 -> concurrencyController.showMenu();
                case 6 -> switchUser();
                case 7 -> running = false;
            }
            if (choice == 3) ValidationUtils.waitForEnter();
        }
    }

    private void displayMainMenu() {
        System.out.println("\n--********************************************************************--");
        System.out.println("|                              MAIN MENU                                |");
        System.out.println("--********************************************************************--");
        System.out.println("  Logged in as: " + currentUser.getFullName());
        System.out.println();
        System.out.println("  1. Manage Projects");
        System.out.println("  2. Manage Tasks");
        System.out.println("  3. View Status Reports");
        System.out.println("  4. Stream Operations (Week 3)");
        System.out.println("  5. Concurrency Demo (Week 3)");
        System.out.println("  6. Switch User");
        System.out.println("  7. Save & Exit");
        System.out.println();
    }

    private void switchUser() {
        System.out.println("\nSelect user:");
        for (int i = 0; i < userCount; i++) {
            System.out.println((i + 1) + ". " + users[i].getDisplayHeader());
        }
        int choice = ValidationUtils.readIntInRange("Choice: ", 1, userCount);
        currentUser = users[choice - 1];
        System.out.println("Switched to: " + currentUser.getFullName());
    }
}
