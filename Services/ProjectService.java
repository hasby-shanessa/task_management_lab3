package Services;

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
    public SoftwareProject createSoftwareProject(String projectName, String projectDescription, int teamSize, String budget, String language, String framework, String repositoryUrl){
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
    public HardwareProject createHardwareProject(String projectName, String projectDescription, int teamSize, String budget, String components, String supplier){
        if(projectCount >= MAX_PROJECTS){
            System.out.println("Error: Maximum projects reached (" + MAX_PROJECTS + ")");
            return null;
        }
        HardwareProject project = new HardwareProject(projectName, projectDescription, teamSize, budget, components, supplier);
        projects[projectCount] = project;
        projectCount++;
        return project;
    }
}
