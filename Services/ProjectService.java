package Services;
import utils.exceptions.*;
import Models.HardwareProject;
import Models.Project;
import Models.SoftwareProject;

public class ProjectService {
    //array to store all projects in the system
    private Project[] projects;
    private int projectCount;
    private static final int MAX_PROJECTS = 100;

    //Initialize empty project storage
    public ProjectService(){
        this.projects = new Project[MAX_PROJECTS];
        this.projectCount = 0;
    }

    //Create software project
    public SoftwareProject createSoftwareProject(String projectName, String projectDescription, int teamSize, String budget, String language, String framework, String repositoryUrl) throws InvalidInputException{
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new InvalidInputException("projectName", "Project name cannot be empty");
        }
        if (teamSize <= 0) {
            throw new InvalidInputException("teamSize", "Team size must be greater than 0");
        }
        if(projectCount >= MAX_PROJECTS){
            System.out.println("Error: Maximum projects reached (" + MAX_PROJECTS + ")");
            return null;
        }
        SoftwareProject project = new SoftwareProject(projectName, projectDescription, teamSize, budget, language, framework, repositoryUrl);
        projects[projectCount] = project;
        projectCount++;
        return project;
    }

    //Create hardware project
    public HardwareProject createHardwareProject(String projectName, String projectDescription, int teamSize, String budget, String components, String supplier) throws InvalidInputException{
        if(projectName == null || projectName.trim().isEmpty()){
            throw new InvalidInputException("projectName", "Project name cannot be empty");
        }
        if(teamSize <=0){
            throw new InvalidInputException("teamSize", "Team size must be greater than 0");
        }
        if(projectCount >= MAX_PROJECTS){
            System.out.println("Error: Maximum projects reached (" + MAX_PROJECTS + ")");
            return null;
        }
        HardwareProject project = new HardwareProject(projectName, projectDescription, teamSize, budget, components, supplier);
        projects[projectCount] = project;
        projectCount++;
        return project;
    }

    //GET ALL PROJECTS
    public Project[] getAllProjects(){
        return projects;
    }
    //get project count
    public int getProjectCount(){
        return projectCount;
    }
    //get project by id(using linear search through the projects array)
    public Project findProjectById(String projectId) throws EmptyProjectException{
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectId().equals(projectId)){
                return projects[i];
            }
        }
        throw new EmptyProjectException(projectId, "Project "+ projectId + " not found");
    }

    //get software projects only
    public Project[] getSoftwareProjects(){
        int count = 0;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectType().equals("Software")){
                count++;
            }
        }
        Project[] result = new Project[count];
        int index = 0;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectType().equals("Software")){
                result[index] = projects[i];
                index++;
            }
        }
        return result;
    }

    //Get hardware projects only
    public Project[] getHardwareProjects(){
        int count = 0;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectType().equals("Hardware")){
                count++;
            }
        }
        Project[] result = new Project[count];
        int index = 0;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectType().equals("Hardware")){
                result[index] = projects[i];
                index++;
            }
        }
        return result;
    }
    //get software project count
    public int getSoftwareProjectCount(){
        int count = 0;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectType().equals("Software")){
                count++;
            }
        }
        return count;
    }
    //get hardware project count
    public int getHardwareProjectCount(){
        int count = 0;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectType().equals("Hardware")){
                count++;
            }
        }
        return count;
    }

    //Delete project
    public boolean deleteProject(String projectId) throws EmptyProjectException{
        int indexToDelete = -1;
        for(int i = 0; i<projectCount; i++){
            if(projects[i].getProjectId().equals(projectId)){
                indexToDelete = i;
                break;
            }
        }
        if ( indexToDelete == -1){
            throw new EmptyProjectException(projectId, "Project " + projectId + " not found");
        }
        //shifting to fill the gap
        for(int i = indexToDelete; i<projectCount-1; i++){
            projects[i] = projects[i+1];
        }
        projects[projectCount -1] = null;
        projectCount--;
        return true;
    }

    //get average completion
    public double getAverageCompletion(){
        if(projectCount == 0){
            return 0.0;
        }
        double total =0.0;
        for (int i = 0; i<projectCount; i++){
            total += projects[i].getCompletionPercentage();
        }
        return total / projectCount;
    }

    //get total task count
    public int getTotalTaskCount(){
        int total = 0;
        for(int i = 0; i<projectCount; i++){
            total += projects[i].getTaskCount();
        }
        return total;
    }
    //get completed task count
    public int getCompletedTaskCount(){
        int total = 0;
        for(int i = 0; i<projectCount; i++){
            total += projects[i].getCompletedTasksCount();
        }
        return total;
    }

    //get completed project count
    public int getCompletedProjectCount(){
        int count = 0;
        for(int i=0; i<projectCount; i++){
            if(projects[i].isComplete()){
                count++;
            }
        }
        return count;
    }
}
