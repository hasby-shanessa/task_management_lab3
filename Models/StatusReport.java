package Models;

public class StatusReport {
    private int totalProjects;
    private int completedProjects;
    private int totalTasks;
    private int completedTasks;
    private double averageCompletion;

    //default constructor
    public StatusReport(){
        totalProjects = 0;
        completedProjects =0;
        totalTasks = 0;
        completedTasks = 0;
        averageCompletion = 0.0;
    }

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

    public void setTotalProjects(int totalProjects){
        this.totalProjects = totalProjects;
    }
    public void setCompletedProjects(int completedProjects){
        this.completedProjects = completedProjects;
    }
    public void setTotalTasks(int totalTasks){
        this.totalTasks = totalTasks;
    }
    public void setCompletedTasks(int completedTasks){
        this.completedTasks = completedTasks;
    }
    public void setAverageCompletion(double averageCompletion){
        this.averageCompletion = averageCompletion;
    }

    @Override
    public String toString(){
        return "StatusReport{" + "Projects: " + completedProjects + "/" + totalProjects + ", Tasks: " + completedTasks + "/" + totalTasks + ", Avg: " + String.format("%.1f%%", averageCompletion) + '}';
    }

}
