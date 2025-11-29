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
}
