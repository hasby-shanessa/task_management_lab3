package Services;
import utils.exceptions.*;
import Models.Project;
import Models.Task;

import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    //dependency injection
    private ProjectService projectService;

    public TaskService(ProjectService projectService){
        this.projectService = projectService;
    }
    public Task addTaskToProject(String projectId, String taskName, String taskDescription, String status) {
        try {
            // Validate input
            if (taskName == null || taskName.trim().isEmpty()) {
                throw new InvalidInputException("taskName", "Task name cannot be empty");
            }

            Project project = projectService.findProjectById(projectId);
            Task task = new Task(taskName, taskDescription, status);

            if (project.addTask(task)) {
                return task;
            }
            throw new IllegalStateException("Failed to add task to project");

        } catch (ProjectNotFoundException e) {
            System.out.println("Not found [" + e.getProjectId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        } finally {
            System.out.println("Add task to project completed for project: " + projectId);
        }
    }

    //overloaded method(add task without description
    public Task addTaskToProject(String projectId, String taskName, String status) {
        return addTaskToProject(projectId, taskName, "", status);
    }

    //find task by ID
    public Task findTaskById(String taskId){
        try {
            if(taskId == null || taskId.trim().isEmpty()){
                throw new InvalidInputException("taskId", "Task id cannot be empty");
            }
            Task task = projectService.getAllProjectsList().stream()
                    .flatMap(project -> project.getTasks().stream())  // Flatten all tasks
                    .filter(t -> t.getTaskId().equals(taskId))        // Find matching ID
                    .findFirst()                                       // Get first match
                    .orElse(null);
            if (task == null) {
                throw new TaskNotFoundException(taskId, "Task " + taskId + " not found");
            }
            return task;

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

    //find project by containing task
    public Project findProjectContainingTask(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new InvalidInputException("taskId", "Task ID cannot be empty");
            }

            Project project = projectService.getAllProjectsList().stream()
                    .filter(p -> p.findTaskById(taskId) != null)  // Project contains task?
                    .findFirst()                                   // Get first match
                    .orElse(null);

            if (project == null) {
                throw new TaskNotFoundException(taskId, "Task " + taskId + " not found in any project");
            }
            return project;

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
    public boolean updateTaskStatus(String taskId, String newStatus) {
        try {
            if (newStatus == null || newStatus.trim().isEmpty()) {
                throw new InvalidInputException("status", "Status cannot be empty");
            }

            Task task = findTaskById(taskId);

            if (task.setStatus(newStatus)) {
                System.out.println("Task " + task.getTaskName() + " marked as " + newStatus);
                return true;
            }
            throw new InvalidInputException("status", "Invalid status: " + newStatus);

        } catch (TaskNotFoundException e) {
            System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        } finally {
            System.out.println("Update task status completed for task: " + taskId);
        }
    }

    //remove task
    public boolean removeTask(String taskId) {
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

    //get all tasks from all projects using flatMap

    public List<Task> getAllTasks() {
        return projectService.getAllProjectsList().stream()
                .flatMap(project -> project.getTasks().stream())
                .collect(Collectors.toList());
    }

    //get tasks by status across all projects
    public List<Task> getTasksByStatus(String status){
        return getAllTasks().stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    //count tasks by status
    public long countTasksByStatus(String status){
        return getAllTasks().stream()
                .filter(task -> task.getStatus().equals(status))
                .count();
    }

    //get total task count across all projects
    public int getTotalTaskCount(){
        return(int) projectService.getAllProjectsList().stream()
                .mapToInt(Project::getTaskCount)
                .sum();
    }

    //get all completed tasks
    public List<Task> getCompletedTasks() {
        return getAllTasks().stream()
                .filter(Task::isComplete)
                .collect(Collectors.toList());
    }

    //get all pending tasks
    public List<Task> getPendingTasks(){
        return getTasksByStatus("Pending");
    }

    //get tasks assigned to a specific person
    public List<Task> getTasksAssignedTo(String assignee) {
        return getAllTasks().stream()
                .filter(task -> task.getAssignedTo().equals(assignee))
                .collect(Collectors.toList());
    }
}
