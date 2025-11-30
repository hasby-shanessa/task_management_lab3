import Models.AdminUser;
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
//        runMainLoop(); //run the main application loop
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
}
