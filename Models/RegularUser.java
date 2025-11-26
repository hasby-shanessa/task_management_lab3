package Models;

public class RegularUser extends User{
    private String department;
    public RegularUser(String username, String fullName, String department){
        super(username, fullName, "Regular");
        this.department = department;
    }

    public RegularUser(String username, String fullName){
        super(username, fullName, "Regular");
        this.department = "General";
    }

    public String getDepartment(){
        return department;
    }
    public void setDepartment(String department){
        this.department = department;
    }

    @Override
    public boolean canCreateProjects(){
        return true;
    }
    @Override
    public boolean canDeleteProjects(){
        return false;
    }
    @Override
    public boolean canManageUsers(){
        return false;
    }

    @Override
    public String getPermissionsSummary() {
        return "REGULAR USER PERMISSIONS:\n" + "  ✓ Create Projects\n" + "  ✓ View Projects\n" + "  ✓ Manage Tasks\n" + "  ✗ Delete Projects\n" + "  ✗ Manage Users";
    }

}
