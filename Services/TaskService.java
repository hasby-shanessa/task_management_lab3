package Services;

import utils.exceptions.*;
import Models.Project;
import Models.Task;

public class TaskService {
    //dependency injection
    private final ProjectService projectService;

    public TaskService(ProjectService projectService) {
        this.projectService = projectService;
    }

    //add task to project
    public Task addTaskToProject(String projectId, String taskName, String status) {
        try {
            if (taskName == null || taskName.trim().isEmpty()) {
                throw new InvalidInputException("taskName", "Task name cannot be empty");
            }

            Project project = projectService.findProjectById(projectId);
            Task task = new Task(taskName, "", status);

            if (project.addTask(task)) {
                System.out.println("✓ Task '" + taskName + "' added to project " + projectId);
                return task;
            }
            throw new IllegalStateException("Failed to add task to project");

        } catch (ProjectNotFoundException e) {
            System.out.println("Not found [" + e.getProjectId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("addTaskToProject completed for project: " + projectId);
        }
    }

    //find task by ID across all projects
    public Task findTaskById(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new InvalidInputException("taskId", "Task ID cannot be empty");
            }

            Task task = projectService.getAllProjectsList().stream()
                    .flatMap(project -> project.getTasks().stream())
                    .filter(t -> t.getTaskId().equals(taskId))
                    .findFirst()
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
        } finally {
            System.out.println("findTaskById completed for task: " + taskId);
        }
    }

    //find project containing a task
    public Project findProjectContainingTask(String taskId) {
        try {
            if (taskId == null || taskId.trim().isEmpty()) {
                throw new InvalidInputException("taskId", "Task ID cannot be empty");
            }

            Project project = projectService.getAllProjectsList().stream()
                    .filter(p -> p.findTaskById(taskId) != null)
                    .findFirst()
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
        } finally {
            System.out.println("findProjectContainingTask completed for task: " + taskId);
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
                System.out.println("✓ Task '" + task.getTaskName() + "' updated to " + newStatus);
                return true;
            }
            throw new InvalidInputException("status", "Invalid status: " + newStatus);

        } catch (TaskNotFoundException e) {
            System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("updateTaskStatus completed for task: " + taskId);
        }
    }

    //remove task from project
    public boolean removeTask(String taskId) {
        try {
            Project project = findProjectContainingTask(taskId);

            if (project.removeTask(taskId)) {
                System.out.println("✓ Task " + taskId + " removed");
                return true;
            }
            throw new IllegalStateException("Failed to remove task from project");

        } catch (TaskNotFoundException e) {
            System.out.println("Not found [" + e.getTaskId() + "]: " + e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            System.out.println("State error: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("removeTask completed for task: " + taskId);
        }
    }
}