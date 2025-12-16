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

    //loading projects from file
    public HardwareProject(String projectId, String projectName, String projectDescription, int teamSize, String budget, String components, String supplier) {
        super(projectId, projectName, projectDescription, teamSize, budget);  // Pass ID to parent
        this.projectType = "Hardware";
        this.components = components;
        this.supplier = supplier;
    }

    public String getComponents(){
        return components;
    }

    public String getSupplier(){
        return supplier;
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

   public String getProjectSummary() {
        return String.format("[%s] %s - %s (%.0f%% complete)",
                projectId, projectName, components, getCompletionPercentage());
    }
}
