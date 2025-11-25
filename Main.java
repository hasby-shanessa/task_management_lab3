import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("============================================================");
        System.out.println("||           Welcome Our Task Management System            ||");
        System.out.println("============================================================\n");

        //Display options

        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean isRunning = true;

        System.out.println("Filter Options");
        System.out.println("************************************");
        System.out.println("1. Add New Project");
        System.out.println("2. View All Projects");
        System.out.println("3. Software Projects Only");
        System.out.println("4. Hardware Projects Only");
        System.out.println("5. Search By Budget Range");
        System.out.println("6. Exit");
        System.out.println("************************************\n");

        // Get choices
        System.out.print("Enter your choice from 1 - 6: ");

        choice = scanner.nextInt();
        switch(choice){
            case 1:
                System.out.println("User wants to add new Project");
                break;

            case 2:
                System.out.println("User wants to view their projects");
                break;

            case 3:
                System.out.println("User wants to view software projects only");
                break;

            case 4:
                System.out.println("User wants to view hardware projects only");
                break;

            case 5:
                System.out.println("User wants to search project by budget range");
                break;

            case 6:
                System.out.println("Exiting...");
                isRunning = false;
                break;

            default:
                System.out.print ("Please enter a valid choice from 1 - 6: ");


        }


    }
}
