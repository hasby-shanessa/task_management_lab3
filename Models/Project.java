package Models;

import Interfaces.Completable;

public abstract class Project implements Completable {
    private static int nextId = 1;
    protected String projectId;
    protected String projectName;
    protected String projectDescription;
    protected String projectType;
    protected int teamSize;
    protected String budget;
    protected Task[] tasks;
    protected int taskCount;

    protected static final int MAX_TASKS = 100;

    public Project( String projectName, String projectDescription, int teamSize, String budget){
        this.projectId = "P" + String.format("%03d", nextId++);
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.teamSize = teamSize;
        this.projectType = "Unknow"; //(will be set by child class(software/hardware)
        this.tasks = new Task [MAX_TASKS];
        this.taskCount = 0;
        this.budget =  budget;
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

    public Task[] getTasks() {
        return tasks;
    }

    public int getTaskCount() {
        return taskCount;
    }

        // ADDING A TASK TO THE PROJECT
    public boolean addTask(Task task){
        if(taskCount >= MAX_TASKS){
            System.out.println("Error: Project is full(max " + MAX_TASKS + " tasks)");
            return false;
        }
        tasks[taskCount] = task;
        task.setProjectId(this.projectId);
        taskCount++;

        System.out.println("Task \"" + task.getTaskName()+ "\" added successfully to Project " + projectId + "!");
        return true;
    }

    //GETTING TASK BY ID
    public Task findTaskById(String taskId){
        for(int i = 0; i<taskCount; i++){
            if(tasks[i].getTaskId().equals(taskId)){
                return tasks[i];
            }
        }
        return null;
    }

    // REMOVING TASK
    public boolean removeTask(String taskId){
        int indexToRemove = -1;
        for(int i = 0; i<taskCount; i++){
            if(tasks[i].getTaskId().equals(taskId)){
                indexToRemove = i;
                break;
            }
        }
        if(indexToRemove == -1){
            System.out.println("Error: Task "+ taskId + "not found");
            return false;
        }
        for(int i = indexToRemove; i<taskCount-1; i++){
            tasks[i] = tasks[i+1];
        }
        tasks[taskCount - 1] = null;
        taskCount--;
        System.out.println("Task " + taskId + " removed successfully");
        return true;
    }

    //UPDATING TASK STATUS
    public boolean updateTaskStatus(String taskId, String newStatus){
        Task task = findTaskById(taskId);
        if(task == null){
            System.out.println("Error: Task " + taskId + " not found");
            return false;
        }
        if(task.setStatus(newStatus)){
            System.out.println("Task \"" + task.getTaskName() + "\" marked as "+ newStatus);
            return true;
        }
        return false;
    }

    @Override
    public boolean isComplete(){
        if(taskCount==0){
            return false;
        }
        for(int i = 0; i<taskCount; i++){
            if(!tasks[i].isComplete()){
                return false;
            }
        }
        //If all tasks are complete
        return true;
    }

    @Override
    public void markAsComplete(){
        for(int i =0; i<taskCount; i++){
            tasks[i].markAsComplete();
        }
        System.out.println("All tasks are complete");
    }

    @Override
    public double getCompletionPercentage(){
        if(taskCount==0){
            return 0.0;
        }
        int completedTasks = 0;
        for(int i = 0; i<taskCount; i++){
            if(tasks[i].isComplete()){
                completedTasks++;
            }
        }
        return ((double)completedTasks / taskCount) * 100.0;
    }

    //GETTING COUNT OF THE COMPLETED TASKS
    public int getCompletedTasksCount(){
        int count = 0;
        for(int i = 0; i<taskCount; i++){
            if(tasks[i].isComplete()){
                count++;
            }
        }
        return count;
    }

    public abstract void displayProjectDetails();
//    public abstract String getProjectSummary();

    //DISPLAY METHOD
    public void displayTasks(){
        System.out.println("\nAssociated Tasks: ");
        System.out.println("----------------------------------------------------");
        System.out.printf("%-8s | %-25s | %15s%n" , "ID", "TASK NAME", "STATUS");
        System.out.println("----------------------------------------------------");

        if(taskCount == 0){
            System.out.println("No tasks");
        } else {
            for(int i = 0; i<taskCount; i++){
                Task t = tasks[i];
                System.out.printf("%-8s | %-25s | %15s%n", t.getTaskId(), t.getTaskName(), t.getStatus());
            }
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
