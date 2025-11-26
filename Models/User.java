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

    public String getUserId(){
        return userId;
    }
    public String getUsername(){
        return username;
    }
    public String getFullName(){
        return fullName;
    }
    public String getRole(){
        return role;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    //PERMISSIONS

    //Can user create new project
    public abstract boolean canCreateProjects();
    //Can user delete
    public abstract boolean canDeleteProjects();
    //Can manage other users
    public abstract boolean canManageUsers();
    //Get user's summary about their permissions
    public abstract String getPermissionsSummary();

    //USER INFO DISPLAY
    public String getDisplayHeader(){
        return fullName + "( " +role + " )";
    }
    public static void resetIdCounter(){
        nextId = 1;
    }

    @Override
    public String toString(){
        return userId + " | " + username + " | " + fullName + " | " + role;
    }
}
