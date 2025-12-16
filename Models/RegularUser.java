package Models;

public class RegularUser extends User{
    private String department;
    public RegularUser(String username, String fullName, String department){
        super(username, fullName, "Regular");
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
}
