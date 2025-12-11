package utils.exceptions;

import java.util.NoSuchElementException;

public class ProjectNotFoundException extends NoSuchElementException {
    private String projectId;
    public ProjectNotFoundException(String projectId, String message){
        super(message);
        this.projectId = projectId;
    }
    public String getProjectId(){
        return projectId;
    }
}
