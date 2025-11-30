import Models.*;
import Services.ReportService;
import Services.TaskService;
import Services.ProjectService;
import utils.ConsoleMenu;
import utils.ValidationUtils;
import utils.exceptions.TaskNotFoundException;
import utils.exceptions.*;

import java.io.Console;
import java.util.Scanner;

public class Main {

    //services
    private static ProjectService projectService;
    private static TaskService taskService;
    private static ReportService reportService;

    //user management
    private static User[] users;
    private static int userCount;
    private static User currentUser;
    private static final int MAX_USERS = 10;
    public static void main(String[] args) {
        initialize();  //initialize the system
        showWelcome();  //show welcome and login
        login();
        runMainLoop(); //run the main application loop
        shutdown(); //exit

    }
    //initialization
    private static void initialize(){
        projectService = new ProjectService();
        taskService = new TaskService(projectService);
        reportService = new ReportService(projectService);

        //create users array
        users = new User[MAX_USERS];
        userCount = 0;

        createSampleUsers();
    }
    //create sample users
    private static void createSampleUsers(){
        users[0] = new AdminUser("hasby.admin", "Hasby umu");
        users[1] = new RegularUser("Joy.dev", "Joy Gift", "QA");
        users[2] = new RegularUser("Divine.pm", "Divine Bay", "Product");
        userCount = 3;
    }
    //show welcome screen
    private static void showWelcome() {
        System.out.println();
        System.out.println("--********************************************************************--");
        System.out.println("||                                                                     ||");
        System.out.println("||              WELCOME TO JAVA TASK MANAGEMENT SYSTEM                 ||");
        System.out.println("||                                                                     ||");
        System.out.println("||         A comprehensive tool for managing projects and tasks        ||");
        System.out.println("||                                                                     ||");
        System.out.println("---*******************************************************************---");
        System.out.println();
    }

        //login
        private static void login(){
            System.out.println("--********************************************************************--");
            System.out.println("|                               USER LOGIN                              |");
            System.out.println("--********************************************************************--");
            System.out.println();
            System.out.println("Select your user account:");
            System.out.println();
            for(int i = 0; i<userCount; i++){
                System.out.println((i+1) + ". " + users[i].getDisplayHeader());
            }
            System.out.println();
            int choice = ValidationUtils.readIntInRange("Enter your choice: ",1, userCount);
            currentUser = users[choice - 1];

            System.out.println();
            System.out.println("Welcome, " + currentUser.getFullName() + "!");
            System.out.println("Role: " + currentUser.getRole());
            System.out.println();
            ValidationUtils.waitForEnter();
        }
        //switch user
    private static void switchUser(){
        System.out.println();
        ConsoleMenu.displayUserSelection(users, userCount);
        int choice = ValidationUtils.readIntInRange("Select user (1 - " + userCount + "): ", 1, userCount);
        currentUser = users[choice - 1];
        System.out.println();
        System.out.println("Switched to: " + currentUser.getDisplayHeader());
        ValidationUtils.waitForEnter();
    }
    //run main loop
    private static void runMainLoop(){
        boolean running = true;
        while(running){
            ConsoleMenu.displayMenu(currentUser);
            int choice = ConsoleMenu.getMainMenuChoice();

            switch(choice){
                case 1:
                    manageProjects();
                    break;
                case 2:
                    manageTasks();
                    break;
                case 3:
                    viewStatusReports();
                    break;
                case 4:
                    switchUser();
                    break;
                case 5:
                    running = false;
                    break;
            }
        }
    }
    //manage projects menu
    private static void manageProjects(){
        boolean inProjectMenu = true;
        while(inProjectMenu){
            ConsoleMenu.displayProjectMenu();
            int choice = ConsoleMenu.getProjectMenuChoice();
            switch(choice){
                case 1:
                    viewProjectCatalog();
                    break;
                case 2:
                    createProject();
                    break;
                case 3:
                    viewProjectDetails();
                    break;
                case 4:
                    deleteProject();
                    break;
                case 5:
                    inProjectMenu = false;
                    break;
            }
        }
    }
    //view project catalog
    private static void viewProjectCatalog(){
        boolean inCatalog = true;
        while(inCatalog){
            ConsoleMenu.displayProjectCatalogHeader(projectService.getProjectCount());
            int filterChoice = ConsoleMenu.getCatalogFilterChoice();
            Project[] displayProjects;
            int displayCount;

            switch (filterChoice){
                case 1:
                    displayProjects = projectService.getAllProjects();
                    displayCount = projectService.getProjectCount();
                    ConsoleMenu.displayProjectTable(displayProjects, displayCount);
                    break;
                case 2:
                    displayProjects = projectService.getSoftwareProjects();
                    displayCount = projectService.getSoftwareProjectCount();
                    System.out.println("\n [ Filtered: Software projects only ]");
                    ConsoleMenu.displayProjectTable(displayProjects, displayCount);
                    break;
                case 3:
                    displayProjects = projectService.getHardwareProjects();
                    displayCount = projectService.getHardwareProjectCount();
                    System.out.println("\n [ Filtered: Hardware projects only ]");
                    ConsoleMenu.displayProjectTable(displayProjects, displayCount);
                    break;
                case 4:
                    inCatalog = false;
                    continue;
                default:
                    displayProjects = new Project[0];
                    displayCount = 0;
            }
            if(filterChoice != 4 && displayCount > 0){
                String input = ValidationUtils.readString("Enter Project ID to view details (or press Enter to go back): ");
                if(!input.isEmpty()){
                    String projectId = normalizeProjectId(input);
                    try {
                        Project project = projectService.findProjectById(projectId);
                        showProjectDetailsAndOptions(project);
                    } catch (EmptyProjectException e) {
                        ConsoleMenu.showError("Project " + projectId + " not found");
                    }
                }
            }
            if(filterChoice != 4){
                ValidationUtils.waitForEnter();
            }
        }
    }
    //convert project id to P001
    private static String normalizeProjectId(String input){
        input = input.trim().toUpperCase();
        if(input.matches("P\\d{3}")){
            return input;
        }
        if(input.matches("\\d+")){
            int num = Integer.parseInt(input);
            return "P" + String.format("%03d", num);
        }
        return input;
    }
    //create new project
    private static void createProject(){
        if(!currentUser.canCreateProjects()){
            ConsoleMenu.showPermissionDenied();
            return;
        }
        ConsoleMenu.displayCreateProjectHeader();
        //get project type
        String projectType = ValidationUtils.readProjectType();
        System.out.println();
        String name = ValidationUtils.readNonEmptyString("Enter project name: ");
        String description = ValidationUtils.readString("Enter description: ");
        int teamSize = ValidationUtils.readPositiveInt("Enter team size: ");
        String budget = ValidationUtils.readBudget("Enter budget: ");

        try {
            Project createdProject;
            if (projectType.equals("Software")) {
                System.out.println("\n[Software Project Details]");
                String language = ValidationUtils.readNonEmptyString("Enter programming language: ");
                String framework = ValidationUtils.readString("Enter framework (or press Enter to skip): ");
                if (framework.isEmpty()) framework = "Not specified";
                String repoUrl = ValidationUtils.readString("Enter repository URL (or press Enter to skip): ");
                if (repoUrl.isEmpty()) repoUrl = "Not specified";

                createdProject = projectService.createSoftwareProject(
                        name, description, teamSize, budget, language, framework, repoUrl
                );
            } else {
                System.out.println("\n[Hardware Project Details]");
                String components = ValidationUtils.readNonEmptyString("Enter components: ");
                String supplier = ValidationUtils.readString("Enter supplier (or press Enter to skip): ");
                if (supplier.isEmpty()) supplier = "Not specified";

                createdProject = projectService.createHardwareProject(
                        name, description, teamSize, budget, components, supplier
                );
            }

            if (createdProject != null) {
                System.out.println();
                ConsoleMenu.showSuccess("Project \"" + name + "\" created with ID: " + createdProject.getProjectId());

                // Ask if user wants to add tasks
                if (ValidationUtils.confirm("\nWould you like to add tasks to this project?")) {
                    addTasksToProject(createdProject);
                }
            }
        } catch (InvalidInputException e) {
            ConsoleMenu.showError(e.getMessage());
        }

        ValidationUtils.waitForEnter();
    }
    //view project details
    public static void viewProjectDetails(){
        if(projectService.getProjectCount() == 0){
            ConsoleMenu.showInfo("No projects found");
            ValidationUtils.waitForEnter();
            return;
        }
        String projectId = ValidationUtils.readProjectId("Enter Project ID: ");
        try {
            Project project = projectService.findProjectById(projectId);
            showProjectDetailsAndOptions(project);
        } catch (EmptyProjectException e) {
            System.out.println("Error: " + e.getMessage());
            ValidationUtils.waitForEnter();
        }
    }
    //show project details with options
    private static void showProjectDetailsAndOptions(Project project){
        boolean viewingProject = true;
        while(viewingProject){
            project.displayProjectDetails();
            int choice = ValidationUtils.readIntInRange("Enter your choice: ", 1, 4);
            switch (choice){
                case 1:
                    addTaskToSpecificProject(project);
                    break;
                case 2:
                    updateTaskInProject(project);
                    break;
                case 3:
                    removeTaskFromProject(project);
                    break;
                case 4:
                    viewingProject = false;
                    break;
            }
        }
    }
    //add task to specific project
    private static void addTaskToSpecificProject(Project project){
        ConsoleMenu.displayAddTaskHeader();
        String taskName = ValidationUtils.readNonEmptyString("Enter task name: ");
        String taskDescription = ValidationUtils.readString("Enter description or press enter to skip: ");
        ValidationUtils.displayStatusOptions();
        String status = ValidationUtils.readTaskStatus("Enter status: ");
        Task task = new Task(taskName, taskDescription, status);
        project.addTask(task);
        ValidationUtils.waitForEnter();
    }
    //add multiple tasks to project
    private static void addTasksToProject(Project project){
        boolean addingTasks = true;
        while(addingTasks){
            addTaskToSpecificProject(project);
            addingTasks = ValidationUtils.confirm("Add another task?");
        }
    }
    //update task in project
    private static void updateTaskInProject(Project project){
        if(project.getTaskCount()==0){
            ConsoleMenu.showInfo("No tasks in this project");
            ValidationUtils.waitForEnter();
            return;
        }
        ConsoleMenu.displayUpdateStatusHeader();

        String taskId = ValidationUtils.readTaskId("Enter Task ID: ");
        Task task = project.findTaskById(taskId);

        if (task == null) {
            ConsoleMenu.showError("Task " + taskId + " not found in this project");
            ValidationUtils.waitForEnter();
            return;
        }

        System.out.println("Current status: " + task.getStatus());
        ValidationUtils.displayStatusOptions();
        String newStatus = ValidationUtils.readTaskStatus("Enter new status: ");

        project.updateTaskStatus(taskId, newStatus);
        ValidationUtils.waitForEnter();
    }
    //remove task from project
    private static void removeTaskFromProject(Project project) {
        if (project.getTaskCount() == 0) {
            ConsoleMenu.showInfo("No tasks in this project");
            ValidationUtils.waitForEnter();
            return;
        }

        String taskId = ValidationUtils.readTaskId("Enter Task ID to remove: ");

        if (ValidationUtils.confirm("Are you sure you want to remove task " + taskId + "?")) {
            project.removeTask(taskId);
        } else {
            ConsoleMenu.showInfo("Removal cancelled");
        }

        ValidationUtils.waitForEnter();
    }
    //delete project
    private static void deleteProject() {
        if (!currentUser.canDeleteProjects()) {
            ConsoleMenu.showPermissionDenied();
            return;
        }

        if (projectService.getProjectCount() == 0) {
            ConsoleMenu.showInfo("No projects to delete");
            ValidationUtils.waitForEnter();
            return;
        }

        String projectId = ValidationUtils.readProjectId("Enter Project ID to delete: ");
        try {
            Project project = projectService.findProjectById(projectId);
            System.out.println("Project: " + project.getProjectName());

            if (ValidationUtils.confirm("Are you sure?")) {
                projectService.deleteProject(projectId);
                System.out.println("Project deleted.");
            }
        } catch (EmptyProjectException e) {
            System.out.println("Error: " + e.getMessage());
        }

        ValidationUtils.waitForEnter();
    }
    //manage tasks menu
    private static void manageTasks(){
        boolean inTaskMenu = true;
        while(inTaskMenu){
            ConsoleMenu.displayTaskMenu();
            int choice = ConsoleMenu.getTaskMenuChoice();
            switch (choice) {
                case 1:
                    viewTasksByProject();
                    break;
                case 2:
                    addNewTask();
                    break;
                case 3:
                    updateTaskStatus();
                    break;
                case 4:
                    removeTask();
                    break;
                case 5:
                    inTaskMenu = false;
                    break;
            }
        }
    }
    //view tasks by project
    private static void viewTasksByProject(){
        if (projectService.getProjectCount() == 0) {
            ConsoleMenu.showInfo("No projects available");
            ValidationUtils.waitForEnter();
            return;
        }
        Project[] projects = projectService.getAllProjects();
        int count = projectService.getProjectCount();
        System.out.println("\nAvailable Projects:");
        System.out.println("**********************************************************");
        for(int i = 0; i<count; i++){
            System.out.printf("%s | %s | Tasks: %d%n", projects[i].getProjectId(), projects[i].getProjectName(), projects[i].getTaskCount());
        }
        System.out.println("**********************************************************");
        String projectId = ValidationUtils.readProjectId("\nEnter Project ID: ");
        try {
            Project project = projectService.findProjectById(projectId);
            project.displayTasks();
        } catch (EmptyProjectException e) {
            ConsoleMenu.showError("Project not found");
        }

        ValidationUtils.waitForEnter();
    }
    //add new task
    private static void addNewTask() {
        if (projectService.getProjectCount() == 0) {
            ConsoleMenu.showInfo("No projects available. Create a project first");
            ValidationUtils.waitForEnter();
            return;
        }

        ConsoleMenu.displayAddTaskHeader();

        String taskName = ValidationUtils.readNonEmptyString("Enter task name: ");
        String projectId = ValidationUtils.readProjectId("Enter assigned project ID: ");

        try {
            Project project = projectService.findProjectById(projectId);
            ValidationUtils.displayStatusOptions();
            String status = ValidationUtils.readTaskStatus("Enter initial status: ");
            taskService.addTaskToProject(projectId, taskName, status);
        } catch (EmptyProjectException e) {
            ConsoleMenu.showError("Project " + projectId + " not found");
        }

        ValidationUtils.waitForEnter();
    }
    //update task status
    private static void updateTaskStatus() {
        if (projectService.getTotalTaskCount() == 0) {
            ConsoleMenu.showInfo("No tasks available");
            ValidationUtils.waitForEnter();
            return;
        }

        ConsoleMenu.displayUpdateStatusHeader();

        String taskId = ValidationUtils.readTaskId("Enter Task ID: ");

        try {
            Task task = taskService.findTaskById(taskId);

            System.out.println("Task: " + task.getTaskName());
            System.out.println("Current Status: " + task.getStatus());
            System.out.println();

            ValidationUtils.displayStatusOptions();
            String newStatus = ValidationUtils.readTaskStatus("Enter new status: ");

            taskService.updateTaskStatus(taskId, newStatus);
        } catch (TaskNotFoundException e) {
            ConsoleMenu.showError("Task " + taskId + " not found");
        }

        ValidationUtils.waitForEnter();
    }
    //remove task
    private static void removeTask() {
        if (projectService.getTotalTaskCount() == 0) {
            ConsoleMenu.showInfo("No tasks available");
            ValidationUtils.waitForEnter();
            return;
        }

        String taskId = ValidationUtils.readTaskId("Enter Task ID to remove: ");

        try {
            Task task = taskService.findTaskById(taskId);

            System.out.println("Task: " + task.getTaskName());
            System.out.println("Status: " + task.getStatus());

            if (ValidationUtils.confirm("Remove this task?")) {
                taskService.removeTask(taskId);
            } else {
                ConsoleMenu.showInfo("Removal cancelled");
            }
        } catch (TaskNotFoundException e) {
            ConsoleMenu.showError("Task " + taskId + " not found");
        }

        ValidationUtils.waitForEnter();
    }
    //view status reports
    private static void viewStatusReports(){
        reportService.displayStatusReport();
        ValidationUtils.waitForEnter();
    }
    //shutdown
    private static void shutdown(){
        System.out.println();
        System.out.println("--********************************************************************--");
        System.out.println("||                                                                     ||");
        System.out.println("||                  THANK YOU FOR USING THE SYSTEM!                    ||");
        System.out.println("||                                                                     ||");
        System.out.println("||                              Goodbye!                               ||");
        System.out.println("||                                                                     ||");
        System.out.println("---*******************************************************************---");
        System.out.println();

        ValidationUtils.closeScanner();
    }
}
