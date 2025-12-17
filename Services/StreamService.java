package Services;

import Interfaces.TaskFilter;
import Models.Project;
import Models.Task;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamService {
    private final ProjectService projectService;
    public StreamService(ProjectService projectService){
        this.projectService = projectService;
    }
    //get all completed projects
    public List<Project> getCompletedProjects(){
        return projectService.getAllProjectsList().stream()
                .filter(Project::isComplete)
                .collect(Collectors.toList());
    }
    //get a project above a completion threshold
    public List<Project> getProjectsAboveCompletion(double threshold) {
        return projectService.getAllProjectsList().stream()
                .filter(p -> p.getCompletionPercentage() > threshold)
                .collect(Collectors.toList());
    }

    //get projects by type
    public List<Project> getProjectsByType(String type){
        return projectService.getAllProjectsList().stream()
                .filter(p -> p.getProjectType().equals(type))
                .collect(Collectors.toList());
    }
    //get all project names as a list
    public List<String> getProjectNames(){
        return projectService.getAllProjectsList().stream()
                .map(Project::getProjectName)
                .collect(Collectors.toList());
    }
    //count projects by type
    public Map<String, Long> countProjectsByType(){
        return projectService.getAllProjectsList().stream()
                .collect(Collectors.groupingBy(Project::getProjectType, Collectors.counting()));
    }

    //get all tasks from all projects
    public List<Task> getAllTasks(){
        return projectService.getAllProjectsList().stream()
                .flatMap(project -> project.getTasks().stream())
                .collect(Collectors.toList());
    }

    //filter tasks (TaskFilter)
    public List<Task> filterTasks(TaskFilter filter){
        return getAllTasks().stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }
    //get count of completed tasks
    public long getCompletedTasksCount(){
        return getAllTasks().stream()
                .filter(Task::isComplete)
                .count();
    }
    //get tasks by status
    public List<Task> getTasksByStatus(String status){
        return getAllTasks().stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }
    //group tasks by status
    public Map<String, List<Task>> groupTasksByStatus(){
        return getAllTasks().stream()
                .collect(Collectors.groupingBy(Task::getStatus));
    }
    //group tasks by project
    public Map<String, List<Task>> groupTasksByProject(){
        return getAllTasks().stream()
                .collect(Collectors.groupingBy(Task::getProjectId));
    }

    //display completed projects
    public void displayCompletedProjects() {
        System.out.println("\n=== COMPLETED PROJECTS ===");
        List<Project> completed = getCompletedProjects();

        if (completed.isEmpty()) {
            System.out.println("No completed projects found.");
        } else {
            completed.forEach(p ->
                    System.out.println("✓ [" + p.getProjectId() + "] " + p.getProjectName())
            );
        }
    }
    //display projects above completion threshold
    public void displayProjectsAboveCompletion(double threshold) {
        System.out.println("\n=== PROJECTS ABOVE " + threshold + "% COMPLETION ===");

        projectService.getAllProjectsList().stream()
                .filter(p -> p.getCompletionPercentage() > threshold)
                .forEach(p -> System.out.printf("  [%s] %s - %.0f%%%n",
                        p.getProjectId(), p.getProjectName(), p.getCompletionPercentage()));
    }
    //display task statistics
    public void displayTaskStatistics() {
        System.out.println("\n=== TASK STATISTICS ===");

        Map<String, Long> statusCounts = getAllTasks().stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        statusCounts.forEach((status, count) ->
                System.out.println("  " + status + ": " + count + " tasks"));

        System.out.println("  ─────────────────");
        System.out.println("  Total: " + getAllTasks().size() + " tasks");
    }
    //display all tasks in table format
    public void displayAllTasks() {
        System.out.println("\n=== ALL TASKS ===");
        System.out.println("*****************************************************************");
        System.out.printf("%-8s | %-10s | %-20s | %s%n", "TASK ID", "PROJECT", "NAME", "STATUS");
        System.out.println("*****************************************************************");

        getAllTasks().forEach(task -> {
            String name = task.getTaskName();
            if (name.length() > 20) {
                name = name.substring(0, 17) + "...";
            }
            System.out.printf("%-8s | %-10s | %-20s | %s%n",
                    task.getTaskId(),
                    task.getProjectId(),
                    name,
                    task.getStatus());
        });

        System.out.println("*****************************************************************");
    }
}
