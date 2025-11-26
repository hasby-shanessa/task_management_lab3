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

    //IN CASE SOME FIELDS AREN'T KNOWN YET
    public SoftwareProject(String projectName, String projectDescription, int teamSize, String budget){
        super(projectName, projectDescription, teamSize, budget);;
        this.projectType = "Software";
        this.programmingLanguage = "Not specified";
        this.framework = "Not specified";
        this.repositoryUrl = "Not specified";
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }
    public void setProgrammingLanguage(String programmingLanguage){
        this.programmingLanguage = programmingLanguage;
    }
    public String getFramework(){
        return framework;
    }
    public void setFramework(String framework){
        this.framework = framework;
    }
    public String getRepositoryUrl(){
        return repositoryUrl;
    }
    public void setRepositoryUrl(String repositoryUrl){
        this.repositoryUrl = repositoryUrl;
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

        displayTasks();  //inherited from Project

        System.out.println();
        System.out.println("Options:");
        System.out.println("1. Add New Task");
        System.out.println("2. Update Task Status");
        System.out.println("3. Remove Task");
        System.out.println("4. Back to Main Menu");

    }

    // GET PROJECT SUMMARY
    @Override
    public String getProjectSummary(){
        return String.format("Software: %s | %s/%s | Tasks: %d/%d (%.0f%%)", projectName, programmingLanguage, framework, getCompletedTasksCount(), taskCount, getCompletionPercentage());
    }
}
