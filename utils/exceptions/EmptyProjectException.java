package utils.exceptions;

public class EmptyProjectException extends NullPointerException{
    private String projectId;
    public EmptyProjectException(String projectId, String message){
        super(message);
        this.projectId = projectId;
    }
}
