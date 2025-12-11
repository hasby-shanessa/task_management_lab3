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
            //validate input
            if (taskName == null || taskName.trim().isEmpty()){
                throw new InvalidInputException("taskName", "Task name cannot be empty");
            }
            Project project = projectService.findProjectById(projectId);
            Task task = new Task(taskName, taskDescription, status);

            if(project.addTask(task)){
                return task;
            }
            throw new IllegalStateException("Failed to add task to project");
        } catch (ProjectNotFoundException e){
            System.out.println("Not found [" + e.getProjectId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e){
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        } finally {
            System.out.println("Add task to project completed for project: " + projectId);
        }
    }
    public Task addTaskToProject(String projectId, String taskName, String status){
        return addTaskToProject(projectId, taskName, "", status);
    }

    //find task by ID
    public Task findTaskById(String taskId){
        try {
            if(taskId == null || taskId.trim().isEmpty()){
                throw new InvalidInputException("taskId", "Task id cannot be empty");
            }
            Project[] projects = projectService.getAllProjects();
            int count = projectService.getProjectCount();

            for (int i = 0; i<count; i++){
                Task task = projects[i].findTaskById(taskId);
                if(task != null){
                    return task;
                }
            }
            throw new TaskNotFoundException(taskId, "Task " + taskId + " not found");
        } catch (TaskNotFoundException e){
            System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e){
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e){
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        }
    }

    //find project by containing task
    public Project findProjectContainingTask(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new InvalidInputException("taskId", "Task ID cannot be empty");
            }

            Project[] projects = projectService.getAllProjects();
            int count = projectService.getProjectCount();

            for (int i = 0; i < count; i++) {
                Task task = projects[i].findTaskById(taskId);
                if (task != null) {
                    return projects[i];
                }
            }
            throw new TaskNotFoundException(taskId, "Task " + taskId + " not found in any project");

        } catch (TaskNotFoundException e) {
            System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        }
    }

    //update task status
    public boolean updateTaskStatus(String taskId, String newStatus){
       try {
           if(newStatus == null || newStatus.trim().isEmpty()){
               throw new InvalidInputException("status", "Status cannot be empty");
           }
           Task task = findTaskById(taskId);
           if(task.setStatus(newStatus)){
               System.out.println("Task " + task.getTaskName() + " marked as " + newStatus);
               return true;
           }
           throw new InvalidInputException("status", "Invalid status: " + newStatus);
       } catch (TaskNotFoundException e){
           System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
           throw e;
       } catch (InvalidInputException e){
           System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
           throw e;
       } catch (Exception e) {
           throw new RuntimeException("Internal server error: " + e.getMessage(), e);
       } finally {
           System.out.println("Update task status completed for task: " + taskId);
       }
    }

    //remove task
    public boolean removeTask(String taskId){
        try {
            Project project = findProjectContainingTask(taskId);

            if (project.removeTask(taskId)) {
                return true;
            }
            throw new IllegalStateException("Failed to remove task from project");

        } catch (TaskNotFoundException e) {
            System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            System.out.println("State error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        } finally {
            System.out.println("removeTask completed for task: " + taskId);
        }
    }
}
