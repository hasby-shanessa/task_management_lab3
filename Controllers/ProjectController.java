package Controllers;

import Models.*;
import Services.ServiceContainer;
import utils.ValidationUtils;
import utils.RegexValidator;
import utils.exceptions.*;

public class ProjectController {

    private final ServiceContainer services;

    public ProjectController(ServiceContainer services) {
        this.services = services;
    }

    public void manageProjects(User currentUser) {
        boolean inMenu = true;
        while (inMenu) {
            displayMenu();
            int choice = ValidationUtils.readIntInRange("Enter choice: ", 1, 5);

            switch (choice) {
                case 1 -> viewCatalog(currentUser);
                case 2 -> createProject(currentUser);
                case 3 -> viewDetails();
                case 4 -> deleteProject(currentUser);
                case 5 -> inMenu = false;
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== PROJECT MANAGEMENT ===");
        System.out.println("1. View Project Catalog");
        System.out.println("2. Create New Project");
        System.out.println("3. View Project Details");
        System.out.println("4. Delete Project");
        System.out.println("5. Back to Main Menu");
    }

    private void viewCatalog(User currentUser) {
        System.out.println("\n=== PROJECT CATALOG ===");
        if (services.getProjectService().getProjectCount() == 0) {
            System.out.println("No projects available.");
            ValidationUtils.waitForEnter();
            return;
        }

        services.getProjectService().displayAllProjects();

        String input = ValidationUtils.readString("\nEnter Project ID to view details (or press Enter to go back): ");
        if (!input.isEmpty()) {
            String projectId = RegexValidator.toProjectId(input);
            if (projectId == null) {
                System.out.println(RegexValidator.getProjectIdError());
            } else {
                try {
                    Project project = services.getProjectService().findProjectById(projectId);
                    showProjectDetailsAndOptions(project);
                } catch (ProjectNotFoundException e) {
                    System.out.println("Project not found.");
                }
            }
        }
    }

    private void viewDetails() {
        if (services.getProjectService().getProjectCount() == 0) {
            System.out.println("No projects available.");
            ValidationUtils.waitForEnter();
            return;
        }

        String input = ValidationUtils.readString("Enter Project ID: ");
        String projectId = RegexValidator.toProjectId(input);

        if (projectId == null) {
            System.out.println(RegexValidator.getProjectIdError());
            ValidationUtils.waitForEnter();
            return;
        }

        try {
            Project project = services.getProjectService().findProjectById(projectId);
            showProjectDetailsAndOptions(project);
        } catch (ProjectNotFoundException e) {
            System.out.println("Project not found.");
            ValidationUtils.waitForEnter();
        }
    }

    // Interactive project details menu
    private void showProjectDetailsAndOptions(Project project) {
        boolean viewing = true;
        while (viewing) {
            project.displayProjectDetails();
            int choice = ValidationUtils.readIntInRange("Enter your choice: ", 1, 4);

            switch (choice) {
                case 1 -> addTaskToProject(project);
                case 2 -> updateTaskInProject(project);
                case 3 -> removeTaskFromProject(project);
                case 4 -> viewing = false;
            }
        }
    }

    // Add task to specific project
    private void addTaskToProject(Project project) {
        System.out.println("\n=== ADD NEW TASK ===");
        String taskName = ValidationUtils.readNonEmptyString("Enter task name: ");
        String taskDescription = ValidationUtils.readString("Enter description (or press Enter to skip): ");
        ValidationUtils.displayStatusOptions();
        String status = ValidationUtils.readTaskStatus("Enter status: ");

        Task task = new Task(taskName, taskDescription, status);
        project.addTask(task);
        System.out.println("✓ Task added successfully!");
        ValidationUtils.waitForEnter();
    }

    // Update task status in project
    private void updateTaskInProject(Project project) {
        if (project.getTaskCount() == 0) {
            System.out.println("No tasks in this project.");
            ValidationUtils.waitForEnter();
            return;
        }

        System.out.println("\n=== UPDATE TASK STATUS ===");
        String input = ValidationUtils.readString("Enter Task ID: ");
        String taskId = RegexValidator.toTaskId(input);

        if (taskId == null) {
            System.out.println(RegexValidator.getTaskIdError());
            ValidationUtils.waitForEnter();
            return;
        }

        Task task = project.findTaskById(taskId);
        if (task == null) {
            System.out.println("Task " + taskId + " not found in this project.");
            ValidationUtils.waitForEnter();
            return;
        }

        System.out.println("Current status: " + task.getStatus());
        ValidationUtils.displayStatusOptions();
        String newStatus = ValidationUtils.readTaskStatus("Enter new status: ");

        project.updateTaskStatus(taskId, newStatus);
        ValidationUtils.waitForEnter();
    }

    // Remove task from project
    private void removeTaskFromProject(Project project) {
        if (project.getTaskCount() == 0) {
            System.out.println("No tasks in this project.");
            ValidationUtils.waitForEnter();
            return;
        }

        String input = ValidationUtils.readString("Enter Task ID to remove: ");
        String taskId = RegexValidator.toTaskId(input);

        if (taskId == null) {
            System.out.println(RegexValidator.getTaskIdError());
            ValidationUtils.waitForEnter();
            return;
        }

        if (ValidationUtils.confirm("Are you sure you want to remove task " + taskId + "?")) {
            project.removeTask(taskId);
        } else {
            System.out.println("Removal cancelled.");
        }
        ValidationUtils.waitForEnter();
    }

    private void createProject(User currentUser) {
        if (!currentUser.canCreateProjects()) {
            System.out.println("Permission denied.");
            ValidationUtils.waitForEnter();
            return;
        }

        System.out.println("\n=== CREATE PROJECT ===");
        System.out.println("1. Software Project");
        System.out.println("2. Hardware Project");
        int type = ValidationUtils.readIntInRange("Select type: ", 1, 2);

        String name = ValidationUtils.readNonEmptyString("Project name: ");
        String desc = ValidationUtils.readString("Description: ");
        int teamSize = ValidationUtils.readPositiveInt("Team size: ");
        String budget = ValidationUtils.readBudget("Budget: ");

        try {
            Project project;
            if (type == 1) {
                String lang = ValidationUtils.readNonEmptyString("Programming language: ");
                String framework = ValidationUtils.readString("Framework (or press Enter to skip): ");
                String repo = ValidationUtils.readString("Repository URL (or press Enter to skip): ");
                project = services.getProjectService().createSoftwareProject(
                        name, desc, teamSize, budget, lang,
                        framework.isEmpty() ? "Not specified" : framework,
                        repo.isEmpty() ? "Not specified" : repo
                );
            } else {
                String components = ValidationUtils.readNonEmptyString("Components: ");
                String supplier = ValidationUtils.readString("Supplier (or press Enter to skip): ");
                project = services.getProjectService().createHardwareProject(
                        name, desc, teamSize, budget, components,
                        supplier.isEmpty() ? "Not specified" : supplier
                );
            }
            System.out.println("✓ Project created with ID: " + project.getProjectId());

            if (ValidationUtils.confirm("Would you like to add tasks to this project?")) {
                addTasksToProject(project);
            }
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
        ValidationUtils.waitForEnter();
    }

    // Add multiple tasks to project
    private void addTasksToProject(Project project) {
        boolean adding = true;
        while (adding) {
            addTaskToProject(project);
            adding = ValidationUtils.confirm("Add another task?");
        }
    }

    private void deleteProject(User currentUser) {
        if (!currentUser.canDeleteProjects()) {
            System.out.println("Permission denied.");
            ValidationUtils.waitForEnter();
            return;
        }

        if (services.getProjectService().getProjectCount() == 0) {
            System.out.println("No projects to delete.");
            ValidationUtils.waitForEnter();
            return;
        }

        String input = ValidationUtils.readString("Enter Project ID to delete: ");
        String projectId = RegexValidator.toProjectId(input);

        if (projectId == null) {
            System.out.println(RegexValidator.getProjectIdError());
            ValidationUtils.waitForEnter();
            return;
        }

        try {
            Project project = services.getProjectService().findProjectById(projectId);
            System.out.println("Project: " + project.getProjectName());

            if (ValidationUtils.confirm("Are you sure you want to delete this project?")) {
                services.getProjectService().deleteProject(projectId);
                System.out.println("✓ Project deleted.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (ProjectNotFoundException e) {
            System.out.println("Project not found.");
        }
        ValidationUtils.waitForEnter();
    }
}