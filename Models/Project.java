package Models;

import Interfaces.Completable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Project implements Completable {
    private static int nextId = 1;
    protected String projectId;
    protected String projectName;
    protected String projectDescription;
    protected String projectType;
    protected int teamSize;
    protected String budget;
//    protected Task[] tasks;
//    protected int taskCount;

//    protected static final int MAX_TASKS = 100;

    protected ArrayList<Task> tasks;
    public Project(String projectName, String projectDescription, int teamSize, String budget) {
        this.projectId = "P" + String.format("%03d", nextId++);
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.teamSize = teamSize;
        this.projectType = "Unknown";
        this.budget = budget;
        this.tasks = new ArrayList<>();
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectType() {
        return projectType;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public String getBudget() {
        return budget;
    }

    public List<Task> getTasks(){
        return tasks;
    }
    public int getTaskCount(){
        return tasks.size();
    }


        // ADDING A TASK TO THE PROJECT
    public boolean addTask(Task task){
        task.setProjectId(this.projectId);
        tasks.add(task);  // ArrayList handles everything

        System.out.println("Task \"" + task.getTaskName() + "\" added successfully to Project " + projectId + "!");
        return true;
    }

    //GETTING TASK BY ID
    public Task findTaskById(String taskId){
        return tasks.stream()                              // Convert list to stream
                .filter(task -> task.getTaskId().equals(taskId))  // Lambda: keep only matching
                .findFirst()                               // Get first match (or empty)
                .orElse(null);
    }

    // REMOVING TASK
    public boolean removeTask(String taskId){
        boolean removed = tasks.removeIf(task -> task.getTaskId().equals(taskId));
        if (removed) {
            System.out.println("Task " + taskId + " removed successfully");
        } else {
            System.out.println("Error: Task " + taskId + " not found");
        }
        return removed;
    }

    //UPDATING TASK STATUS
    public boolean updateTaskStatus(String taskId, String newStatus){
        Task task = findTaskById(taskId);
        if (task == null) {
            System.out.println("Error: Task " + taskId + " not found");
            return false;
        }
        if (task.setStatus(newStatus)) {
            System.out.println("Task \"" + task.getTaskName() + "\" marked as " + newStatus);
            return true;
        }
        return false;
    }

    @Override
    public boolean isComplete(){
        if (tasks.isEmpty()) {  // CHANGE: isEmpty() instead of taskCount == 0
            return false;
        }
        return tasks.stream().allMatch(Task::isComplete);
    }

    @Override
    public void markAsComplete(){
        tasks.forEach(Task::markAsComplete);

        System.out.println("All tasks marked as complete");
    }

    @Override
    public double getCompletionPercentage(){
        if (tasks.isEmpty()) {
            return 0.0;
        }
        long completedCount = tasks.stream()
                .filter(Task::isComplete)  // Keep only completed tasks
                .count();                   // Count them

        return ((double) completedCount / tasks.size()) * 100.0;
    }

    //GETTING COUNT OF THE COMPLETED TASKS
    public int getCompletedTasksCount(){
        return (int) tasks.stream()
                .filter(Task::isComplete)
                .count();
    }

    public List<Task> getCompletedTasks(){
        return tasks.stream()
                .filter(Task::isComplete)
                .collect(Collectors.toList());
    }

    //get task by status
    public List<Task> getTasksByStatus(String status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))  // Lambda
                .collect(Collectors.toList());
    }

    public abstract void displayProjectDetails();
//    public abstract String getProjectSummary();

    //DISPLAY METHOD
    public void displayTasks(){
        System.out.println("\nAssociated Tasks: ");
        System.out.println("----------------------------------------------------");
        System.out.printf("%-8s | %-25s | %15s%n" , "ID", "TASK NAME", "STATUS");
        System.out.println("----------------------------------------------------");

        if (tasks.isEmpty()) {
            System.out.println("No tasks");
        } else {
            tasks.forEach(t ->
                    System.out.printf("%-8s | %-25s | %15s%n",
                            t.getTaskId(), t.getTaskName(), t.getStatus())
            );
        }
        System.out.println("----------------------------------------------------");
        System.out.printf("Completion Rate: %.0f%%%n", getCompletionPercentage());
    }

    //RESET ID COUNTER
    public static void resetIdCounter(){
        nextId = 1;
    }

    @Override
    public String toString() {
        return projectId + " | " + projectName + " | " + projectType + " | Team: " + teamSize + " | " + budget;
    }
}
