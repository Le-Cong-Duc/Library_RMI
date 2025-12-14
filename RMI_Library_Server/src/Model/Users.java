package Model;

import java.io.Serializable;

public class Users implements Serializable {
    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String role;

    public Users() {
    }

    public Users(String userName, String password, String fullName, String email, String role) {
        super();
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format(userName + "|" + password + "|" + fullName + "|" + email + "|" + role);
    }


}
