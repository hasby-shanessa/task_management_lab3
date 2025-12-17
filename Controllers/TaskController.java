package Controllers;
import Models.*;
import Services.ServiceContainer;
import utils.ValidationUtils;
import utils.RegexValidator;
import utils.exceptions.*;
import java.util.List;

public class TaskController {
    private final ServiceContainer services;

    public TaskController(ServiceContainer services) {
        this.services = services;
    }

    public void manageTasks() {
        boolean inMenu = true;
        while (inMenu) {
            displayMenu();
            int choice = ValidationUtils.readIntInRange("Enter choice: ", 1, 5);

            switch (choice) {
                case 1 -> viewTasks();
                case 2 -> addTask();
                case 3 -> updateStatus();
                case 4 -> removeTask();
                case 5 -> inMenu = false;
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== TASK MANAGEMENT ===");
        System.out.println("1. View Tasks by Project");
        System.out.println("2. Add New Task");
        System.out.println("3. Update Task Status");
        System.out.println("4. Remove Task");
        System.out.println("5. Back to Main Menu");
    }

    private void viewTasks() {
        if (services.getProjectService().getProjectCount() == 0) {
            System.out.println("No projects available.");
            ValidationUtils.waitForEnter();
            return;
        }

        services.getProjectService().displayAllProjects();

        String input = ValidationUtils.readString("Enter Project ID: ");
        String projectId = RegexValidator.toProjectId(input);

        if (projectId == null) {
            System.out.println(RegexValidator.getProjectIdError());
        } else {
            try {
                Project project = services.getProjectService().findProjectById(projectId);
                project.displayTasks();
            } catch (ProjectNotFoundException e) {
                System.out.println("Project not found.");
            }
        }
        ValidationUtils.waitForEnter();
    }

    private void addTask() {
        List<Project> projects = services.getProjectService().getAllProjectsList();
        if (projects.isEmpty()) {
            System.out.println("No projects available. Create a project first.");
            ValidationUtils.waitForEnter();
            return;
        }

        System.out.println("\nSelect a Project:");
        for (int i = 0; i < projects.size(); i++) {
            Project p = projects.get(i);
            System.out.printf("%d. [%s] %s%n", i + 1, p.getProjectId(), p.getProjectName());
        }

        int choice = ValidationUtils.readIntInRange("Choice: ", 1, projects.size());
        Project project = projects.get(choice - 1);

        String taskName = ValidationUtils.readNonEmptyString("Task name: ");
        ValidationUtils.displayStatusOptions();
        String status = ValidationUtils.readTaskStatus("Status: ");

        services.getTaskService().addTaskToProject(project.getProjectId(), taskName, status);
        ValidationUtils.waitForEnter();
    }

    private void updateStatus() {
        if (services.getProjectService().getTotalTaskCount() == 0) {
            System.out.println("No tasks available.");
            ValidationUtils.waitForEnter();
            return;
        }

        String input = ValidationUtils.readString("Enter Task ID: ");
        String taskId = RegexValidator.toTaskId(input);

        if (taskId == null) {
            System.out.println(RegexValidator.getTaskIdError());
            ValidationUtils.waitForEnter();
            return;
        }

        try {
            Task task = services.getTaskService().findTaskById(taskId);
            System.out.println("Task: " + task.getTaskName());
            System.out.println("Current: " + task.getStatus());

            ValidationUtils.displayStatusOptions();
            String newStatus = ValidationUtils.readTaskStatus("New status: ");
            services.getTaskService().updateTaskStatus(taskId, newStatus);
        } catch (TaskNotFoundException e) {
            System.out.println("Task not found.");
        }
        ValidationUtils.waitForEnter();
    }

    private void removeTask() {
        String input = ValidationUtils.readString("Enter Task ID to remove: ");
        String taskId = RegexValidator.toTaskId(input);

        if (taskId == null) {
            System.out.println(RegexValidator.getTaskIdError());
            ValidationUtils.waitForEnter();
            return;
        }

        try {
            Task task = services.getTaskService().findTaskById(taskId);
            System.out.println("Task: " + task.getTaskName());

            if (ValidationUtils.confirm("Remove this task?")) {
                services.getTaskService().removeTask(taskId);
                System.out.println("✓ Removed.");
            }
        } catch (TaskNotFoundException e) {
            System.out.println("Task not found.");
        }
        ValidationUtils.waitForEnter();
    }
}
