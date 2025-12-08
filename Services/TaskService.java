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
//        int num = 12;
        try {
            Project project = projectService.findProjectById(projectId);
            Task task = new Task(taskName, taskDescription, status);
//            num = num / 0;
            if(project.addTask(task)){
                return task;
            }
            return null;

        } catch (EmptyProjectException e) {
            System.out.println("Error: Project " + projectId + " not found");
            return null;
        }
        catch (Exception ex){
            System.out.println("Internal Server Error");
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

    //remove task
    public boolean removeTask(String taskId) throws TaskNotFoundException{
        Project project = findProjectContainingTask(taskId);
        if(project == null){
            throw new TaskNotFoundException(taskId, "Task " + taskId + " not found");
        }
        return project.removeTask(taskId);
        }
    }
