package Services;
import utils.exceptions.*;
import Models.HardwareProject;
import Models.Project;
import Models.SoftwareProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectService {
    //array to store all projects in the system
    private Map<String, Project> projects;

    public ProjectService() {
        this.projects = new HashMap<>();
    }

    public ProjectService(Map<String, Project> loadedProjects) {
        this.projects = loadedProjects;
    }

    //Create software project
    public SoftwareProject createSoftwareProject(String projectName, String projectDescription, int teamSize, String budget, String language, String framework, String repositoryUrl) {
        try {
            validateProjectInput(projectName, teamSize);
//            checkCapacity();

            SoftwareProject project = new SoftwareProject(projectName, projectDescription, teamSize, budget, language, framework, repositoryUrl);
            projects.put(project.getProjectId(), project);  // Key = ID, Value = Project

            return project;
        } catch (InvalidInputException e) {
            System.out.println("Validation error [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        }
    }

    //Create Hardware project
    public HardwareProject createHardwareProject(String projectName, String projectDescription, int teamSize, String budget, String components, String supplier) {
        try {
            validateProjectInput(projectName, teamSize);
//            checkCapacity();
            HardwareProject project = new HardwareProject(projectName, projectDescription, teamSize, budget, components, supplier);
            projects.put(project.getProjectId(), project);

            return project;
        } catch (InvalidInputException e) {
            System.out.println("Validation error [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        }
    }

    //Get project by ID
    public Project findProjectById(String projectId){
        try {
            if(projectId == null || projectId.trim().isEmpty()){
                throw new InvalidInputException(projectId, "Project ID cannot be empty");
            }
            Project project = projects.get(projectId);  // Instant lookup

            if (project == null) {
                throw new ProjectNotFoundException(projectId, "Project " + projectId + " not found");
            }
            return project;

        } catch (ProjectNotFoundException e) {
            System.out.println("Not found [" + e.getProjectId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        }
    }

    //Get all projects as Map (for file saving)

    public Map<String, Project> getAllProjects() {
        return projects;
    }

    //get all projects as List
    public List<Project> getAllProjectsList() {
        return new ArrayList<>(projects.values());
    }

    public int getProjectCount(){
        return projects.size();
    }

    // Delete project
    public boolean deleteProject(String projectId){
//        int indexToDelete = -1;
        try{
            if(projectId == null || projectId.trim().isEmpty()){
                throw new InvalidInputException(projectId, "Project ID cannot be empty");
            }
            if (!projects.containsKey(projectId)) {
                throw new ProjectNotFoundException(projectId, "Project " + projectId + " not found");
            }

            projects.remove(projectId);  // One line! No shifting needed!
            return true;

        } catch (ProjectNotFoundException e) {
            System.out.println("Cannot delete [" + e.getProjectId() + "]: " + e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            System.out.println("Invalid input [" + e.getFieldName() + "]: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Internal server error: " + e.getMessage(), e);
        } finally {
            System.out.println("Delete operation completed for projectId: " + projectId);
        }
    }

    public List<Project> getSoftwareProjects(){
        return projects.values().stream()
                .filter(p -> p.getProjectType().equals("Software"))
                .collect(Collectors.toList());
    }

    public List<Project> getHardwareProjects() {
        return projects.values().stream()
                .filter(p -> p.getProjectType().equals("Hardware"))
                .collect(Collectors.toList());
    }

    public List<Project> getProjectsByType(String type) {
        return projects.values().stream()
                .filter(p -> p.getProjectType().equals(type))
                .collect(Collectors.toList());
    }

    public long getSoftwareProjectCount(){
        return projects.values().stream()
                .filter(p->p.getProjectType().equals("Software"))
                .count();
    }

    public long getHardwareProjectCount(){
        return projects.values().stream()
                .filter(p->p.getProjectType().equals("Hardware"))
                .count();
    }

    //get completed projects using stream
    public List<Project> getCompletedProjects(){
        return projects.values().stream()
                .filter(Project::isComplete)
                .collect(Collectors.toList());
    }
    public List<Project> getProjectsAboveCompletion(double threshold) {
        return projects.values().stream()
                .filter(p -> p.getCompletionPercentage() > threshold)
                .collect(Collectors.toList());
    }

    public double getAverageCompletion() {
        if (projects.isEmpty()) {
            return 0.0;
        }

        return projects.values().stream()
                .mapToDouble(Project::getCompletionPercentage)  // Convert to double stream
                .average()                                       // Calculate average
                .orElse(0.0); //default if empty
    }
        public int getTotalTaskCount(){
            return projects.values().stream()
                    .mapToInt(Project::getTaskCount)
                    .sum();
        }

        //display all projects

        public void displayAllProjects(){
            if (projects.isEmpty()) {
                System.out.println("No projects available.");
                return;
            }

            System.out.println("\n=== PROJECT CATALOG ===");
            System.out.println("****************************************************************");
            System.out.printf("%-6s | %-20s | %-10s | %-5s | %s%n",
                    "ID", "PROJECT NAME", "TYPE", "TASKS", "COMPLETION");
            System.out.println("****************************************************************");

            // Using forEach with lambda
            projects.values().forEach(p -> {
                String name = p.getProjectName();
                if (name.length() > 20) {
                    name = name.substring(0, 17) + "...";
                }
                System.out.printf("%-6s | %-20s | %-10s | %-5d | %.0f%%%n",
                        p.getProjectId(), name, p.getProjectType(),
                        p.getTaskCount(), p.getCompletionPercentage());
            });

            System.out.println("****************************************************************");
        }

    // helpers
        private void validateProjectInput(String projectName, int teamSize) throws InvalidInputException {
            if (projectName == null || projectName.trim().isEmpty()) {
                throw new InvalidInputException("projectName", "Project name cannot be empty");
            }
            if (teamSize <= 0) {
                throw new InvalidInputException("teamSize", "Team size must be greater than 0");
            }
        }

        public void setProjects(Map<String, Project> loadedProjects) {
            this.projects = loadedProjects;
        }
}
