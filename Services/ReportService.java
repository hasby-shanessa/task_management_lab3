package Services;

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
}
