package Services;

import Models.Project;
import Models.Task;

public class TaskService {
    //dependency injection
    private ProjectService projectService;

    public TaskService(ProjectService projectService){
        this.projectService = projectService;
    }
    public Task addTaskToProject(String projectId, String taskName, String taskDescription, String status){
        Project project = projectService.findProjectById(projectId);
        if(project == null){
            System.out.println("Error: Project: " + projectId + " not found");
            return null;
        }
        Task task = new Task(taskName, taskDescription, status);
        if(project.addTask(task)){
            return task;
        }
        return null;
    }
    public Task addTaskToProject(String projectId, String taskName, String status){
        return addTaskToProject(projectId, taskName, "", status);
    }

    //find task by ID
    public Task findTaskById(String taskId){
        Project[] projects = projectService.getAllProjects();
        int count = projectService.getProjectCount();

        for(int i=0; i<count; i++){
            Task task = projects[i].findTaskById((taskId));
            if(task != null){
                return task;
            }
        }
        return null;
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
}
