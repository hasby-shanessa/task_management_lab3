package utils;

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
}
