package Services;
import utils.exceptions.*;
import Models.Project;
import Models.Task;

public class TaskService {
    //dependency injection
    private ProjectService projectService;

    public TaskService(ProjectService projectService){
        this.projectService = projectService;
    }
    public Task addTaskToProject(String projectId, String taskName, String taskDescription, String status){
        try {
            Project project = projectService.findProjectById(projectId);
            Task task = new Task(taskName, taskDescription, status);
            if(project.addTask(task)){
                return task;
            }
            return null;
        } catch (EmptyProjectException e) {
            System.out.println("Error: Project " + projectId + " not found");
            return null;
        }
    }
    public Task addTaskToProject(String projectId, String taskName, String status){
        return addTaskToProject(projectId, taskName, "", status);
    }

    //find task by ID
    public Task findTaskById(String taskId) throws TaskNotFoundException{
        Project[] projects = projectService.getAllProjects();
        int count = projectService.getProjectCount();

        for(int i=0; i<count; i++){
            Task task = projects[i].findTaskById((taskId));
            if(task != null){
                return task;
            }
        }
        throw  new TaskNotFoundException(taskId, " Task " + taskId + " not found");
    }
    //find project containing a task
    public Project findProjectContainingTask(String taskId){
        Project[] projects = projectService.getAllProjects();
        int count = projectService.getProjectCount();

        for(int i = 0; i < count; i++){
            Task task =projects[i].findTaskById(taskId);
            if(task != null){
                return projects[i];
            }
        }
        return null;
    }

    //get all tasks from a project
    public Task[] getTasksFromProject(String projectId) throws EmptyProjectException{
        Project project = projectService.findProjectById(projectId);
        if(project == null){
            return new Task[0];
        }
        Task[] allTasks = project.getTasks();
        int count = project.getTaskCount();
        Task[] result = new Task[count];
        for(int i = 0; i<count; i++){
            result[i] = allTasks[i];
        }
        return result;
    }

    //update task status
    public boolean updateTaskStatus(String taskId, String newStatus) throws TaskNotFoundException{
        Task task = findTaskById(taskId);
        if(task == null){
            System.out.println("Error: Task: " + taskId + " not found");
            return false;
        }
        if(task.setStatus(newStatus)){
            System.out.println("Task " + task.getTaskName() + " marked as " + newStatus);
            return true;
        }
        return false;
    }

    //assign task to a user
    public boolean assignTask(String taskId, String username) throws TaskNotFoundException{
        Task task = findTaskById(taskId);
        if (task == null) {
            System.out.println("Error: Task " + taskId + " not found.");
            return false;
        }

        task.setAssignedTo(username);
        System.out.println("Task " + task.getTaskName() + " assigned to " + username);
        return true;
    }

    //remove task
    public boolean removeTask(String taskId) throws TaskNotFoundException{
        Project project = findProjectContainingTask(taskId);
        if(project == null){
            System.out.println("Error: Not found");
            return false;
        }
        return project.removeTask(taskId);
    }

    //count task by status
    public int countTasksByStatus(String status) {
        int count = 0;
        Project[] projects = projectService.getAllProjects();
        int projectCount = projectService.getProjectCount();

        for (int i = 0; i < projectCount; i++) {
            Task[] tasks = projects[i].getTasks();
            int taskCount = projects[i].getTaskCount();

            for (int j = 0; j < taskCount; j++) {
                if (tasks[j].getStatus().equals(status)) {
                    count++;
                }
            }
        }

        return count;
        }

        //get total task count
    public int getTotalTaskCount(){
        return projectService.getTotalTaskCount();
    }
    //get completed task count
    public int getCompletedTaskCount(){
        return projectService.getCompletedTaskCount();
        }
    }
