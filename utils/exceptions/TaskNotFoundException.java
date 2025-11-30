package utils.exceptions;

public class TaskNotFoundException extends Exception{
    private String taskId;
    public TaskNotFoundException(String message){
        super(message);
    }
    public TaskNotFoundException(String taskId, String message){
        super(message);
        this.taskId = taskId;
    }
    public String getTaskId(){
        return taskId;
    }
}
