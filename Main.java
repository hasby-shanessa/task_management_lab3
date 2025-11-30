import Models.AdminUser;
import Models.Project;
import Models.RegularUser;
import Models.User;
import Services.ReportService;
import Services.TaskService;
import Services.ProjectService;
import utils.ConsoleMenu;
import utils.ValidationUtils;

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
//        shutdown(); //exit

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
                    Project project = projectService.findProjectById(projectId);
                    if(project != null){
                        showProjectDetailsAndOptions(project);
                    } else {
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

        ValidationUtils.waitForEnter();
    }
}
