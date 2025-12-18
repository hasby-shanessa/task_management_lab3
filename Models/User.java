package Models;

public abstract class User {
    private static int nextId = 1;

    protected String userId;
    protected String username;
    protected String fullName;
    protected String role;

    public User(String username, String fullName, String role){
        this.userId = "U" + String.format("%03d", nextId);
        nextId++;

        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    public String getFullName(){
        return fullName;
    }
    public String getRole(){
        return role;
    }

    //PERMISSIONS

    //Can user create new project
    public abstract boolean canCreateProjects();
    //Can user delete
    public abstract boolean canDeleteProjects();

    //USER INFO DISPLAY
    public String getDisplayHeader(){
        return fullName + "( " +role + " )";
    }

    @Override
    public String toString(){
        return userId + " | " + username + " | " + fullName + " | " + role;
    }
}
