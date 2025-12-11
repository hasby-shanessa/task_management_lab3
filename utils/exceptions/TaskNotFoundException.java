package utils.exceptions;

import java.util.NoSuchElementException;

public class TaskNotFoundException extends NoSuchElementException {
    private String taskId;
    public TaskNotFoundException(String taskId, String message){
        super(message);
        this.taskId = taskId;
    }
    public String getTaskId(){
        return taskId;
    }
}
