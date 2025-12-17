package Services;

public class ServiceContainer {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final ReportService reportService;
    private final StreamService streamService;
    private final ConcurrencyService concurrencyService;

    public ServiceContainer(ProjectService projectService, TaskService taskService, ReportService reportService, StreamService streamService, ConcurrencyService concurrencyService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.reportService = reportService;
        this.streamService = streamService;
        this.concurrencyService = concurrencyService;
    }

    public ProjectService getProjectService() { return projectService; }
    public TaskService getTaskService() { return taskService; }
    public ReportService getReportService() { return reportService; }
    public StreamService getStreamService() { return streamService; }
    public ConcurrencyService getConcurrencyService() { return concurrencyService; }
}
