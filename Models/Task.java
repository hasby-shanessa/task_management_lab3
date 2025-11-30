package Models;

import Interfaces.Completable;

public class Task implements Completable {
    private static int nextId = 1;
    private String taskId;
    private String taskName;
    private String taskDescription;
    private String status;
    private String assignedTo;
    private String projectId;

    public Task(String taskName, String taskDescription, String status){
        this.taskId = "T" + String.format("%03d", nextId++);
        this.taskName = taskName;
        this.taskDescription = taskDescription;

        if(isValidStatus(status)){
            this.status = status;
        } else{
            this.status = "Pending";
        }

        this.assignedTo = "Unassigned";
        this.projectId = null;
    }

    public Task(String taskName){
        this(taskName, "", "Pending");
    }

    private boolean isValidStatus(String status){
        return status.equals("Pending") || status.equals("In Progress") || status.equals("COMPLETED");
    }

    // GETTERS AND SETTERS

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String title) {
        this.taskName = title;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getStatus() {
        return status;
    }

    public boolean setStatus(String status) {
        if (isValidStatus(status)){
            this.status = status;
            return true;
        } else {
            System.out.println("Invalid status");
            return false;
        }
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean isComplete(){
        return status.equals("Completed");
    }

    @Override
    public void markAsComplete(){
        this.status = "Completed";
    }

    @Override
    public double getCompletionPercentage(){
        return status.equals("Completed")?100.0:0.0;
    }

    public static void resetIdCounter(){
        nextId =1;
    }

    public static int getCurrentIdCounter(){
        return nextId;
    }

    @Override
    public String toString(){
        return "Task{" + "ID= " + taskId + + '\'' + ", Title='" + taskName + '\'' + ", Status='" + status + '\'' + ", Assigned='" + assignedTo + '\'' + '}';
    }
}
