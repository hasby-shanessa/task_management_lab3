package Models;

public class StatusReport {
    private int totalProjects;
    private int completedProjects;
    private int totalTasks;
    private int completedTasks;
    private double averageCompletion;

    public StatusReport(int totalProjects, int completedProjects, int totalTasks, int completedTasks, double averageCompletion){
        this.totalProjects = totalProjects;
        this.completedProjects = completedProjects;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.averageCompletion = averageCompletion;
    }

    public int getTotalProjects(){
        return totalProjects;
    }
    public int getCompletedProjects(){
        return completedProjects;
    }
    public int getTotalTasks(){
        return totalTasks;
    }
    public int getCompletedTasks(){
        return completedTasks;
    }
    public double getAverageCompletion(){
        return averageCompletion;
    }

    @Override
    public String toString(){
        return "StatusReport{" + "Projects: " + completedProjects + "/" + totalProjects + ", Tasks: " + completedTasks + "/" + totalTasks + ", Avg: " + String.format("%.1f%%", averageCompletion) + '}';
    }

}
