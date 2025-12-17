package Services;

import Models.Project;
import Models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyService {
    private final ProjectService projectService;
    public ConcurrencyService(ProjectService projectService){
        this.projectService = projectService;
    }
    public void simulateConcurrentUpdates(){
        System.out.println();
        System.out.println("**********************************************************************");
        System.out.println("|                   PARALLEL TASK UPDATE SIMULATION                  |");
        System.out.println("**********************************************************************");
        System.out.println();

        List<Task> allTasks = getAllTasks();
        if(allTasks.isEmpty()){
            System.out.println("No tasks available for concurrent update simulation");
            return;
        }
        int threadCound = Math.min(3, allTasks.size());
        System.out.println("Starting " + threadCound + " threads....");
        System.out.println();

        //thread pool with fixed number
        ExecutorService executor = Executors.newFixedThreadPool(threadCound);

        for(int i = 0; i<threadCound; i++){
            Task task = allTasks.get(i);
            String newStatus = getNextStatus(task.getStatus());
            int threadNum = i+1;
            executor.submit(() ->{
                updateTaskWIthDelay(task, newStatus, threadNum);
            });
        }
        executor.shutdown();
        try{
            boolean completed = executor.awaitTermination(10, TimeUnit.SECONDS);
            if(!completed){
                System.out.println("Some tasks not completed in time");
            }
        } catch (InterruptedException e){
            System.out.println("Thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        System.out.println();
        System.out.println("All threads finished successfully");
        System.out.println("Task updates applied concurrently and safely");
    }

    private synchronized void updateTaskWIthDelay(Task task, String newStatus, int threadNum){
        try{
            Thread.sleep(500 +(int)(Math.random() * 500));
            task.setStatus(newStatus);
            System.out.println("Thread-" + threadNum+ " updating "+ task.getTaskId() + " (" + task.getTaskName() + ") - " + newStatus);
        } catch(InterruptedException e){
            System.out.println("Thread-"  + threadNum + " interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public void parallelStreamUpdate(){
        System.out.println();
        System.out.println("╔************************************************************╗");
        System.out.println("║              PARALLEL STREAM UPDATE                        ║");
        System.out.println("╚************************************************************╝");
        System.out.println();

        //get pending tasks
        List<Task> pendingTasks = getAllTasks().stream()
                .filter(task -> task.getStatus().equals("Pending"))
                .toList();

        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks to update.");
            return;
        }

        System.out.println("Updating " + pendingTasks.size() + " pending tasks in parallel...");
        System.out.println();

        pendingTasks.parallelStream().forEach(task -> {
            synchronized (task){
                String threadName = Thread.currentThread().getName();
                task.setStatus("In Progress");
                System.out.println("[" + threadName + "] Updated " + task.getTaskId() + " → In Progress");
            }
        });
        System.out.println();
        System.out.println("✓ Parallel stream update completed.");
    }

    //get next logical status for a task
    private String getNextStatus(String currentStatus) {
        return switch (currentStatus) {
            case "Pending" -> "In Progress";
            case "In Progress" -> "Completed";
            default -> "Completed";
        };
    }
    //get all tasks from all projects
    private List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (Project project : projectService.getAllProjectsList()) {
            allTasks.addAll(project.getTasks());
        }
        return allTasks;
    }

    //display thread pool information
    public void displayConcurrencyInfo() {
        System.out.println("\n=== CONCURRENCY INFORMATION ===");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Active threads: " + Thread.activeCount());
        System.out.println("Current thread: " + Thread.currentThread().getName());
    }
}
