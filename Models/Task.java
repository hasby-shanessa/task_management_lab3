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

    //constructor with custom ID for loading from file
    public Task(String taskId, String taskName, String taskDescription, String status){
        this.taskId = taskId;  //provided id not auto generate
        this.taskName = taskName;
        this.taskDescription = taskDescription;

        if (isValidStatus(status)) {
            this.status = status;
        } else {
            this.status = "Pending";
        }

        this.assignedTo = "Unassigned";
        this.projectId = null;

        try {
            int idNumber = Integer.parseInt(taskId.substring(1));
            if(idNumber >= nextId){
                nextId = idNumber + 1;
            }
        } catch (NumberFormatException e) {}
    }

    private boolean isValidStatus(String status){
        return status.equals("Pending") || status.equals("In Progress") || status.equals("Completed");
    }

    // GETTERS AND SETTERS

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getStatus() {
        return status;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getProjectId() {
        return projectId;
    }

    public synchronized boolean setStatus(String status) {
        if (isValidStatus(status)){
            this.status = status;
            return true;
        } else {
            System.out.println("Invalid status");
            return false;
        }
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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

    //for testing
    public static int getNextId() {
        return nextId;
    }

    //convert task to json like string for file storage
    public String toJsonString() {
        return String.format("{\"id\": \"%s\", \"name\": \"%s\", \"status\": \"%s\"}",
                taskId, taskName, status);
    }

    @Override
    public String toString(){
        return "Task{ID='" + taskId + "', Title='" + taskName + "', Status='" + status + "', Assigned='" + assignedTo + "'}";
    }
}
