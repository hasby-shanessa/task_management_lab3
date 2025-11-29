package utils;

import java.util.Scanner;

public class validationUtils {
    private static Scanner scanner = new Scanner(System.in);

    //read non empty string
    public static String readNonEmptyString(String prompt){
        String input;
        while (true){
            System.out.println(prompt);
            input = scanner.nextLine().trim();
            if(input.isEmpty()){
                System.out.println("Error: Input cannot be empty, please try again");
            } else{
                return input;
            }
        }
    }

    //ready any string
    public static String readStrin(String prompt){
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    //read integer
    public static int readInt(String prompt){
        while(true){
            try {
                System.out.println(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e){
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    //read integer in range(between min and max)
    public static int readIntInRange(String prompt, int min, int max){
        while(true){
            try{
                System.out.println(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if(value >=min&& value<=max){
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch(NumberFormatException e){
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
    public static String readTaskStatus(String prompt){
        while(true){
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase("Pending") || input.equals("1")){
                return "Pending";
            } else if(input.equalsIgnoreCase("In Progress") || input.equalsIgnoreCase("InProress") || input.equals("2")){
                return "In Progress";
            } else if(input.equalsIgnoreCase("Completed") || input.equalsIgnoreCase("Complete") || input.equalsIgnoreCase("Done") || input.equals("3")){
                return "Completed";
            } else {
                System.out.println("Error: Invalid status, please choose from (Pending, In progress, Completed)");
            }
        }
    }
}
