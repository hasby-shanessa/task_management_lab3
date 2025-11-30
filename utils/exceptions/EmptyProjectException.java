package utils.exceptions;

public class EmptyProjectException extends Exception{
    private String projectId;
    public EmptyProjectException(String message){
        super(message);
    }
    public EmptyProjectException(String projectId, String message){
        super(message);
        this.projectId = projectId;
    }
    public String getProjectId(){
        return projectId;
    }
}
