package Models;

public class AdminUser extends User {
    public AdminUser(String username, String fullName){
        super(username, fullName, "Admin");
    }

    @Override
    public boolean canCreateProjects(){
        return true;
    }
    @Override
    public boolean canDeleteProjects(){
        return true;
    }

    @Override
    public boolean canManageUsers() {
        return false;
    }

    @Override
    public String getPermissionsSummary() {
        return "ADMIN PERMISSIONS:\n" + "  ✓ Create Projects\n" + "  ✓ Delete Projects\n" + "  ✓ Manage Users\n" + "  ✓ Full System Access";
    }
}
