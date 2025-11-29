package utils;

import java.util.Scanner;

public class ValidationUtils {
    private static Scanner scanner = new Scanner(System.in);

    //read nonempty string
    public static String readNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Input cannot be empty, please try again");
            } else {
                return input;
            }
        }
    }

    //ready any string
    public static String readStrin(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    //read integer
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    //read integer in range(between min and max)
    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    //read positive integer
    public static int readPositiveInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);

                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Error: Value must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    //read valid task status
    public static String readTaskStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Pending") || input.equals("1")) {
                return "Pending";
            } else if (input.equalsIgnoreCase("In Progress") || input.equalsIgnoreCase("InProress") || input.equals("2")) {
                return "In Progress";
            } else if (input.equalsIgnoreCase("Completed") || input.equalsIgnoreCase("Complete") || input.equalsIgnoreCase("Done") || input.equals("3")) {
                return "Completed";
            } else {
                System.out.println("Error: Invalid status, please choose from (Pending, In progress, Completed)");
            }
        }
    }

    //display status options
    public static void displayStatusOptions() {
        System.out.println("Status Options:");
        System.out.println("1. Pending");
        System.out.println("2. In Progress");
        System.out.println("3. Complete");
    }

    //read project id
    public static String readProjectId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.matches("P\\D{3}")) {
                return input;
            }
            if (input.matches("\\d+")) {
                try {
                    int num = Integer.parseInt(input);
                    return "P" + String.format("%03d", num);
                } catch (NumberFormatException e) {
                }
            }
            System.out.println("Error: Invalid input, please enter a valid numeric or prefixed ID(e.g. P001");
        }
    }

    //read task id
    public static String readTaskId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.matches("T\\d{3}")) {
                return input;
            }
            if (input.matches("\\d+")) {
                try {
                    int num = Integer.parseInt(input);
                    return "T" + String.format("%03d", num);
                } catch (NumberFormatException e) {
                }
            }
            System.out.println("Error: Invalid input, please enter a valid Task ID(e.g. T001");
        }
    }
    //read budget
    public static String readBudget(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String cleaned = input.replace("$", "").replace(",", "");

            try {
                int amount = Integer.parseInt(cleaned);
                if (amount >= 0) {
                    // Format with $ and commas
                    return "$" + String.format("%,d", amount);
                } else {
                    System.out.println("Error: Budget cannot be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid amount (e.g., 15000 or $15,000)");
            }
        }
    }
    //read project type
    public static String readProjectType(){
        System.out.println("Project Type:");
        System.out.println("1. Software");
        System.out.println("2. Hardware");

        while (true){
            System.out.print("Enter choice (1 or 2): ");
            String input = scanner.nextLine().trim();
            if (input.equals("1") || input.equalsIgnoreCase("Software")) {
                return "Software";
            } else if (input.equals("2") || input.equalsIgnoreCase("Hardware")) {
                return "Hardware";
            } else {
                System.out.println("Error: Please enter 1 for Software or 2 for Hardware");
            }
        }
    }
    //confirmation
    public static boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.startsWith("y")) {
                return true;
            } else if (input.startsWith("n")) {
                return false;
            } else {
                System.out.println("Error: Please enter 'y' for yes or 'n' for no");
            }
        }
    }
    //wait for enter
    public static void waitForEnter(){
        System.out.print("\n Press Enter to continue...");
        scanner.nextLine();
    }
    //custom message
    public static void pause(String message){
        System.out.println(message);
        scanner.nextLine();
    }
    //close scanner
    public static void closeScanner(){
        scanner.close();
    }
    //clear screen
    public static void clearScreen(){
        System.out.println("\n".repeat(50));
    }
}
