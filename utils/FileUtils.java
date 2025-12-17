package utils;

import Models.HardwareProject;
import Models.Project;
import Models.SoftwareProject;
import Models.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    private static final String DATA_FILE = "data/projects_data.json";

    //save all projects to JSON file
    public static void saveProject(Map<String, Project> projects) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        List<Project> projectList = new ArrayList<>(projects.values());

        for (int i = 0; i < projectList.size(); i++) {
            Project p = projectList.get(i);
            json.append(" {\n");
            json.append("    \"projectId\": \"").append(p.getProjectId()).append("\", \n");
            json.append("    \"name\": \"").append(escapeJson(p.getProjectName())).append("\",\n");
            json.append("    \"description\": \"").append(escapeJson(p.getProjectDescription())).append("\",\n");
            json.append("    \"type\": \"").append(p.getProjectType()).append("\",\n");
            json.append("    \"teamSize\": ").append(p.getTeamSize()).append(",\n");
            json.append("    \"budget\": \"").append(escapeJson(p.getBudget())).append("\",\n");

            if (p instanceof SoftwareProject) {
                SoftwareProject sp = (SoftwareProject) p;
                json.append("    \"language\": \"").append(escapeJson(sp.getProgrammingLanguage())).append("\",\n");
                json.append("    \"framework\": \"").append(escapeJson(sp.getFramework())).append("\",\n");
                json.append("    \"repository\": \"").append(escapeJson(sp.getRepositoryUrl())).append("\",\n");
            } else if (p instanceof HardwareProject) {
                HardwareProject hp = (HardwareProject) p;
                json.append("    \"components\": \"").append(escapeJson(hp.getComponents())).append("\",\n");
                json.append("    \"supplier\": \"").append(escapeJson(hp.getSupplier())).append("\",\n");
            }
            //add tasks array
            json.append(" \"tasks\": [\n");
            List<Task> tasks = p.getTasks();
            for (int j = 0; j < tasks.size(); j++) {
                Task t = tasks.get(j);
                json.append("      {");
                json.append("\"id\": \"").append(t.getTaskId()).append("\", ");
                json.append("\"name\": \"").append(escapeJson(t.getTaskName())).append("\", ");
                json.append("\"status\": \"").append(t.getStatus()).append("\"");
                json.append("}");

                if (j < tasks.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append("     ]\n");
            json.append("   }");

            if (i < projectList.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");

        try {
            Path path = Paths.get(DATA_FILE);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, json.toString());
            System.out.println("Data saved to " + DATA_FILE + "successfully");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    //
    public static Map<String, Project> loadProjects() {
        Map<String, Project> projects = new HashMap<>();
        Path path = Paths.get(DATA_FILE);

        //check if file exists
        if (!Files.exists(path)) {
            System.out.println("No saved data found");
            return projects;
        }
        System.out.println("Loading projects from file...");

        try {
            String content = Files.readString(path);
            projects = parseJsonToProjects(content);
            System.out.println(projects.size() + " projects loaded from " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            System.out.println("Starting with empty catalog");
        }
        return projects;
    }

    private static Map<String, Project> parseJsonToProjects(String json) {
        Map<String, Project> projects = new HashMap<>();

        json = json.trim();
        if(json.equals("[]") || json.isEmpty()) {
            return projects;
        }
        List<String> projectBlocks = extractJsonObjects(json);
        for(String block : projectBlocks) {
            try {
                Project project = parseProject(block);
                if (project != null) {
                    projects.put(project.getProjectId(), project);
                }
            } catch (Exception e) {
                System.out.println("Could not parse a project block");
            }
        }
            return projects;
    }
    //extract json object from array string
    private static List<String> extractJsonObjects(String json){
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        int objectStart = -1;

        for(int i = 0; i<json.length(); i++){
            char c = json.charAt(i);
            if(c=='{'){
                if(braceCount==0){
                    objectStart = i;
                }
                braceCount++;
            } else if(c== '}'){
                braceCount--;
                if(braceCount==0 && objectStart != -1){
                    objects.add(json.substring(objectStart, i+1));
                    objectStart = -1;
                }
            }
        }
        return objects;
    }
    //parse a single project from json string
    private static Project parseProject(String json){
        String projectId = extractValue(json, "projectId");
        String name = extractValue(json, "name");
        String description = extractValue(json, "description");
        String type = extractValue(json, "type");
        int teamSize = parseIntSafe(extractValue(json, "teamSize"));
        String budget = extractValue(json, "budget");

        Project project;

        if("Software".equals(type)){
            String language = extractValue(json, "language");
            String framework = extractValue(json, "framework");
            String repository = extractValue(json, "repository");
            project = new SoftwareProject(projectId, name, description, teamSize, budget, language, framework, repository);
        } else{
            String components = extractValue(json, "components");
            String supplier = extractValue(json, "supplier");
            project = new HardwareProject(projectId, name, description, teamSize, budget, components, supplier);
        }
        parseTasks(json, project);
        return project;
    }
    //pare tasks from json and add to project
    private static void parseTasks(String json, Project project){
        int tasksStart = json.indexOf("\"tasks\"");
        if(tasksStart == -1) return;
        int arrayStart = json.indexOf("[", tasksStart);
        int arrayEnd = json.indexOf("]", arrayStart);
        if(arrayStart == -1 || arrayEnd == -1) return;

        String tasksJson = json.substring(arrayStart, arrayEnd+1);
        List<String> taskBlocks = extractJsonObjects(tasksJson);
        for (String taskBlock : taskBlocks) {
            String taskId = extractValue(taskBlock, "id");
            String taskName = extractValue(taskBlock, "name");
            String status = extractValue(taskBlock, "status");

            if (taskId != null && taskName != null) {
                Task task = new Task(taskId, taskName, "", status);
                project.getTasks().add(task);
                task.setProjectId(project.getProjectId());
            }
        }
    }
    //extract a value from json by key
    private static String extractValue(String json, String key){
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if(keyIndex == -1) return "";
        int colonIndex = json.indexOf(":", keyIndex);
        if(colonIndex == -1)return "";

        int valueStart = colonIndex + 1;
        while(valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))){
            valueStart++;
        }
        if(valueStart >= json.length()) return "";
        char startChar = json.charAt(valueStart);
        if(startChar == '"'){
            int valueEnd = json.indexOf("\"", valueStart + 1);
            if(valueEnd == -1) return "";
            return json.substring(valueStart + 1, valueEnd);
        }
        int valueEnd = valueStart;
        while (valueEnd < json.length() && (Character.isDigit(json.charAt(valueEnd)) || json.charAt(valueEnd) == '.')){
            valueEnd++;
        }
        return json.substring(valueStart, valueEnd);
    }
    //parse integer safely
    private static int parseIntSafe(String value){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    // escape special chars for json
    private static String escapeJson(String value){
        if(value == null) return "";
        return value
                .replace("\\","\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    //check if data file exists
    public static boolean dataFileExists(){
        return Files.exists(Paths.get(DATA_FILE));
    }
    //get data file path
    public static String getDataFilePath(){
        return DATA_FILE;
    }
    //delete data file(for testing)
    public static boolean deleteDataFile(){
        try{
            return Files.deleteIfExists(Paths.get(DATA_FILE));
        } catch (IOException e){
            return false;
        }
    }
}
