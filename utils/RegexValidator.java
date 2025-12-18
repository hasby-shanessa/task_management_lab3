package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {
    private static final String PROJECT_ID_REGEX = "P\\d{3}";
    private static final String TASK_ID_REGEX = "T\\d{3}";

    //compiled patterns(for performance)
    private static final Pattern PROJECT_PATTERN = Pattern.compile(PROJECT_ID_REGEX);
    private static final Pattern TASK_PATTERN = Pattern.compile(TASK_ID_REGEX);

public static boolean isValidProjectId(String projectId){
    if(projectId == null || projectId.isEmpty()){
        return false;
    }
    Matcher matcher = PROJECT_PATTERN.matcher(projectId);
    return matcher.matches();
}

public static boolean isValidTaskId(String taskId){
    if(taskId == null || taskId.isEmpty()){
        return false;
    }
    Matcher matcher = TASK_PATTERN.matcher(taskId);
    return matcher.matches();
}

//convert user input to proper Project ID format
public static String toProjectId(String input){
    if(input == null || input.trim().isEmpty()){
        return null;
    }
    input = input.trim().toUpperCase();
    if(isValidProjectId(input)){
        return input;
    }
    try {
        String numPart = input.replace("P", "");
        int number = Integer.parseInt(numPart);
        if(number >= 1 && number <= 999){
            return String.format("P%03d", number);
        }
    } catch (NumberFormatException e){}
    return null;
}

//convert to proper taskid format
public static String toTaskId(String input){
    if(input == null || input.trim().isEmpty()){
        return null;
    }
    input = input.trim().toUpperCase();
    if(isValidTaskId(input)){
        return input;
    }
    try{
        String numPart = input.replace("T", "");
        int number = Integer.parseInt(numPart);
        if(number >= 1 && number <= 999){
            return String.format("T%03d", number);
        }
    } catch (NumberFormatException e){}
    return null;
}

//check error message for invalid project id
public static String getProjectIdError(){
    return "Invalid Project ID format. Enter a number (1-999 or ID (P001)";
    }

public static String getTaskIdError(){
    return "Invalid input. Enter a number (1-999) or ID (T001)";
    }
}
