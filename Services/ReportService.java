package Services;

import Models.Project;
import Models.StatusReport;

public class ReportService {
    private ProjectService projectService;
    public ReportService(ProjectService projectService){
        this.projectService = projectService;
    }
    //generate status report
public StatusReport generateStatusReport(){
        StatusReport report = new StatusReport();
        report.setTotalProjects(projectService.getProjectCount());
        report.setCompletedProjects(projectService.getCompletedProjectCount());
        report.setTotalTasks(projectService.getTotalTaskCount());
        report.setCompletedTasks(projectService.getCompletedTaskCount());
        report.setAverageCompletion(projectService.getAverageCompletion());
        return report;
    }

    //display full status report
    public void displayStatusReport(){
        System.out.println();
        System.out.println("*********************************************************");
        System.out.println("|                PROJECT STATUS REPORT                   |");
        System.out.println("*********************************************************");
        System.out.println();

        Project[] projects = projectService.getAllProjects();
        int count = projectService.getProjectCount();
        System.out.println("_______________________________________________________________");
        System.out.printf("%-10s | %-18s | %-5s | %-9s | %s%n", "PROJECT ID", "PROJECT NAME", "TASKS", "COMPLETED", "PROGRESS (%");
        System.out.println("_______________________________________________________________");

        if(count == 0){
            System.out.println("No projects to report");
        } else {
            for(int i = 0; i<count; i++){
                Project p = projects[i];
                String name = p.getProjectName();
                if(name.length() > 18){
                    name = name.substring(0, 15) + "...";
                }
                System.out.printf("%%-10s | %-18s | %-5d | %-9d | %.0f%%%n", p.getProjectId(), name, p.getTaskCount(), p.getCompletedTasksCount(), p.getCompletionPercentage());
            }
        }
        System.out.println("_______________________________________________________________");
        System.out.printf("AVERAGE COMPLETION: %.1f%%%n", projectService.getAverageCompletion());
        System.out.println("_______________________________________________________________");
        System.out.println();
    }

    //display summary
    public void displaySummary(){
        StatusReport report = generateStatusReport();
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("|                          SYSTEM SUMMARY                  |");
        System.out.println("************************************************************");
        System.out.println("Total Projects: " + report.getTotalProjects());
        System.out.println("Completed Projects: " + report.getCompletedProjects());
        System.out.println("Total Tasks: " + report.getTotalTasks());
        System.out.println("Completed Tasks: " + report.getCompletedTasks());
        System.out.printf("Average Progress: %.1f%%%n", report.getAverageCompletion());
        System.out.println("************************************************************");
        System.out.println();

    }
}
