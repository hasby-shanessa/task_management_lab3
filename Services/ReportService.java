package Services;

import Models.Project;
import Models.StatusReport;
import java.util.List;

public class ReportService {
    private ProjectService projectService;

    public ReportService(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Generate report data and return as StatusReport object
    public StatusReport generateStatusReport() {
        List<Project> projects = projectService.getAllProjectsList();
        int count = projects.size();

        int totalTasks = 0;
        int completedTasks = 0;
        int completedProjects = 0;

        for (Project project : projects) {
            totalTasks += project.getTaskCount();
            completedTasks += project.getCompletedTasksCount();
            if (project.isComplete()) {
                completedProjects++;
            }
        }

        double avgCompletion = projectService.getAverageCompletion();

        // Return data packaged in StatusReport object
        return new StatusReport(count, completedProjects, totalTasks, completedTasks, avgCompletion);
    }

    // Display full status report
    public void displayStatusReport() {
        // Use StatusReport object
        StatusReport report = generateStatusReport();

        System.out.println();
        System.out.println("*********************************************************");
        System.out.println("|                PROJECT STATUS REPORT                   |");
        System.out.println("*********************************************************");

        // Display summary from StatusReport
        System.out.println();
        System.out.println("┌*********************************************************┐");
        System.out.println("│                      SUMMARY                            │");
        System.out.println("|*********************************************************┤");
        System.out.printf("│  Total Projects:      %-10d                        │%n", report.getTotalProjects());
        System.out.printf("│  Completed Projects:  %-10d                        │%n", report.getCompletedProjects());
        System.out.printf("│  Total Tasks:         %-10d                        │%n", report.getTotalTasks());
        System.out.printf("│  Completed Tasks:     %-10d                        │%n", report.getCompletedTasks());
        System.out.printf("│  Average Completion:  %-10.1f%%                       │%n", report.getAverageCompletion());
        System.out.println("└*********************************************************┘");
        System.out.println();

        // Display detailed project breakdown
        List<Project> projects = projectService.getAllProjectsList();

        System.out.println("PROJECT DETAILS:");
        System.out.println("*********************************************************");
        System.out.printf("%-10s | %-18s | %-5s | %-9s | %s%n", "PROJECT ID", "PROJECT NAME", "TASKS", "COMPLETED", "PROGRESS");
        System.out.println("*********************************************************");

        if (projects.isEmpty()) {
            System.out.println("                     No projects to report                     ");
        } else {
            for (Project p : projects) {
                String name = p.getProjectName();
                if (name.length() > 18) {
                    name = name.substring(0, 15) + "...";
                }
                System.out.printf("%-10s | %-18s | %-5d | %-9d | %.0f%%%n",
                        p.getProjectId(),
                        name,
                        p.getTaskCount(),
                        p.getCompletedTasksCount(),
                        p.getCompletionPercentage());
            }
        }
        System.out.println("***************************************************************");
        System.out.println();
    }
}