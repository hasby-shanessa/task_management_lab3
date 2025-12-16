package Models;

public class SoftwareProject extends Project{
    private String programmingLanguage;
    private String framework;
    private String repositoryUrl;

    public SoftwareProject(String projectName, String projectDescription, int teamSize, String budget, String programmingLanguage, String framework, String repositoryUrl){
        super(projectName, projectDescription, teamSize, budget);
        this.projectType = "Software";
        this.programmingLanguage = programmingLanguage;
        this.framework = framework;
        this.repositoryUrl = repositoryUrl;
    }

    //for loading projects from file

    public SoftwareProject(String projectId, String projectName, String projectDescription, int teamSize, String budget, String programmingLanguage, String framework, String repositoryUrl){
        super(projectId, projectName, projectDescription, teamSize, budget);
        this.projectType = "Software";
        this.programmingLanguage = programmingLanguage;
        this.framework = framework;
        this.repositoryUrl = repositoryUrl;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public String getFramework(){
        return framework;
    }

    public String getRepositoryUrl(){
        return repositoryUrl;
    }

    //DISPLAY PROJECT DETAILS
    @Override
    public void displayProjectDetails(){
        System.out.println();
        System.out.println("********************************************************");
        System.out.println("|           PROJECT DETAILS: " + projectId + "          |");
        System.out.println("********************************************************");
        System.out.println();
        System.out.println("Project Name: " + projectName);
        System.out.println("Type: " + projectType);
        System.out.println("Team Size: " + teamSize);
        System.out.println("Budget: " + budget);
        System.out.println();
        System.out.println("Description: " + projectDescription);
        System.out.println();
        System.out.println("TECHNICAL DETAILS: ");
        System.out.println("Programming Language: " + programmingLanguage);
        System.out.println("Framework: " + framework);
        System.out.println("Repository URL: " + repositoryUrl);
        System.out.printf("Completion: %.0f%%%n", getCompletionPercentage());

        displayTasks();  //inherited from Project

        System.out.println();
        System.out.println("Options:");
        System.out.println("1. Add New Task");
        System.out.println("2. Update Task Status");
        System.out.println("3. Remove Task");
        System.out.println("4. Back to Main Menu");

    }

    // GET PROJECT SUMMARY
    public String getProjectSummary() {
        return String.format("[%s] %s - %s/%s (%.0f%% complete)",
                projectId, projectName, programmingLanguage, framework, getCompletionPercentage());
    }
}
