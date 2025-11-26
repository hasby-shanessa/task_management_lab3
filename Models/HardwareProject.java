package Models;

public class HardwareProject extends Project {
    private String components;
    private String supplier;

    public HardwareProject(String projectName, String projectDescription, int teamSize, String budget, String components, String supplier){
        super(projectName, projectDescription, teamSize, budget);
        this.projectType = "Hardware";
        this.components = components;
        this.supplier = supplier;
    }

    //SMALLER IN CASE SOME FIELDS MISS
    public HardwareProject(String projectName, String projectDescription, int teamSize, String budget){
        super(projectName, projectDescription, teamSize, budget);
        this.projectType = "Hardware";
        this.components = "Not specified";
        this.supplier = "Not specified";
    }

    public String getComponents(){
        return components;
    }
    public void setComponents(String components){
        this.components = components;
    }
    public String getSupplier(){
        return supplier;
    }
    public void setSupplier(String supplier){
        this.supplier = supplier;
    }

    @Override
    public void displayProjectDetails(){
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("|            PROJECT DETAILS: " + projectId + "    |");
        System.out.println("***************************************************");
        System.out.println();
        System.out.println("Project Name: " + projectName);
        System.out.println("Type: " + projectType);
        System.out.println("Team Size: " + teamSize);
        System.out.println("Budget: " + budget);
        System.out.println();
        System.out.println("Description: " + projectDescription);
        System.out.println();
        System.out.println("HARDWARE DETAILS:");
        System.out.println("Components: " + components);
        System.out.println("Supplier: " + supplier);

        displayTasks(); //inherited from Project

        System.out.println();
        System.out.println("Options");
        System.out.println("1. Add New Task");
        System.out.println("2: Update Task Status");
        System.out.println("3: Remove Task");
        System.out.println("4: Back to Main Menu");
    }

    @Override
    public String getProjectSummary(){
        return String.format("Hardware: %s | Components: %s | Tasks: %d/%d (%.0f%%", projectName, components, getCompletedTasksCount(), taskCount, getCompletionPercentage());
    }
}
