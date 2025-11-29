package utils;

import Models.Project;
import Models.User;

public class ConsoleMenu {
    public static void displayMenu(User currentUser){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                JAVA TASK MANAGEMENT SYSTEM                    |");
        System.out.println("****************************************************************");
        System.out.println();
        System.out.println("Current User: " + currentUser.getDisplayHeader());
        System.out.println();
        System.out.println("Main Menu:");
        System.out.println("1. Manage Projects");
        System.out.println("2. Manage Tasks");
        System.out.println("3. View Status Reports");
        System.out.println("4. Switch User");
        System.out.println("5. Exit");
        System.out.println();
    }

    public static int getMainMenuChoice(){
        return ValidationUtils.readIntInRange("Enter your choice: ",1,5);
    }
    //display project menu
    public static void displayProjectMenu(){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                      PROJECT MANAGEMENT                       |");
        System.out.println("****************************************************************");
        System.out.println();
        System.out.println("1. View All Projects");
        System.out.println("2. Create New Project");
        System.out.println("3. View Project Details");
        System.out.println("4. Delete Project");
        System.out.println("5. Back to Main Menu");
        System.out.println();

    }
    public static int getProjectMenuChoice(){
        return ValidationUtils.readIntInRange("Enter your choice: ",1,5);
    }

    //display project catalog
    public static void displayProjectCatalogHeader(int totalProjects){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                        PROJECT CATALOG                       |");
        System.out.println("****************************************************************");
        System.out.println();
        System.out.println("1. View All Projects (" + totalProjects + ")");
        System.out.println("2. Software Projects Only");
        System.out.println("3. Hardware Projects Only");
        System.out.println("4. Back to Project Menu");
        System.out.println();
    }
    public static int getCatalogFilterChoice(){
        return ValidationUtils.readIntInRange("Enter your choice: ",1,4);
    }

    //display project table
    public static void displayProjectTable(Project[] projects, int count){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.printf("%-6s | %-20s | %-10s | %-9s | %s%n", "ID", "PROJECT NAME", "TYPE", "TEAM SIZE", "BUDGET");
        System.out.println("****************************************************************");

        if(count == 0){
            System.out.println("No projects found");
        }else{
            for (int i = 0; i < count; i++) {
                Project p = projects[i];
                System.out.printf("%-6s | %-20s | %-10s | %-9d | %s%n", p.getProjectId(), truncate(p.getProjectName(), 20), p.getProjectType(), p.getTeamSize(), p.getBudget());
                System.out.println("       | Description: " + truncate(p.getProjectDescription(), 45));
                System.out.println("****************************************************************");

            }
        System.out.println();
        }
    }
    //display create project header
    public static void displayCreatePRojectHeader(){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                        CREATE NEW PROJECT                     |");
        System.out.println("****************************************************************");
        System.out.println();
    }

    //display task menu
    public static void displayTaskMenu(){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                          TASK MANAGEMENT                     |");
        System.out.println("****************************************************************");
        System.out.println();
        System.out.println("1. View Tasks by Project");
        System.out.println("2. Add New Task");
        System.out.println("3. Update Task Status");
        System.out.println("4. Remove Task");
        System.out.println("5. Back to Main Menu");
        System.out.println();
    }

    public static int getTaskMenuChoice() {
        return ValidationUtils.readIntInRange("Enter your choice: ", 1, 5);
    }

    //add task header
    public static void displayAddTaskHeader(){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                           ADD NEW TASK                       |");
        System.out.println("****************************************************************");
        System.out.println();
    }

    //update status header
    public static void displayUpdateStatusHeader(){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                       UPDATE TASK STATUS                     |");
        System.out.println("****************************************************************");
        System.out.println();
    }

    //status report
    public static void displayStatusReportHeader(){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                     PROJECT STATUS REPORT                    |");
        System.out.println("****************************************************************");
        System.out.println();
    }
    public static void displayStatusReportTable(Project[] projects, int count, double avgCompletion) {
        System.out.println("****************************************************************");
        System.out.printf("%-10s | %-18s | %-5s | %-9s | %s%n", "PROJECT ID", "PROJECT NAME", "TASKS", "COMPLETED", "PROGRESS (%)");
        System.out.println("****************************************************************");
        if (count == 0) {
            System.out.println("  No projects to report.");
        } else {
            for (int i = 0; i < count; i++) {
                Project p = projects[i];
                System.out.printf("%-10s | %-18s | %-5d | %-9d | %.0f%%%n", p.getProjectId(), truncate(p.getProjectName(), 18), p.getTaskCount(), p.getCompletedTasksCount(), p.getCompletionPercentage());
            }
        }
        System.out.println("****************************************************************");
        System.out.printf("AVERAGE COMPLETION: %.1f%%%n", avgCompletion);
        System.out.println("****************************************************************");
        System.out.println();
    }

    //display user selection
    public static void displayUserSelection(User[] users, int count){
        System.out.println();
        System.out.println("****************************************************************");
        System.out.println("|                          SELECT USER                         |");
        System.out.println("****************************************************************");
        System.out.println();
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + users[i].getDisplayHeader());
        }
        System.out.println();
    }
    //helpers
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    public static void showSuccess(String message) {
        System.out.println("✓ " + message);
    }
    public static void showError(String message) {
        System.out.println("✗ Error: " + message);
    }
    public static void showInfo(String message) {
        System.out.println("ℹ " + message);
    }

    //permission denied
    public static void showPermissionDenied() {
        System.out.println();
        System.out.println("✗ Permission Denied: You don't have access to this feature.");
        System.out.println("  Please contact an administrator.");
        System.out.println();
    }
}
