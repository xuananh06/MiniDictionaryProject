package models;
import java.util.Set;


public class User {

    private final String username;
    private final String password;
    private final Set<String> roles;

    public User(String username, String password, Set<String> roles){
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public Set<String> getRoles(){
        return roles;
    }

    @Override
    public String toString() {
        return username + " [roles: " + String.join(", ", roles) + "]";
    }
}