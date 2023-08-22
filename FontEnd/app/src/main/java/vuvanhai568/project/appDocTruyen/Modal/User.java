package vuvanhai568.project.appDocTruyen.Modal;

public class User {
    private String username;
    private String email;
    private String fullname;
    private String password;

    public User(String username, String email, String fullname, String password) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
    }

    public User(String username, String password ) {
        this.username = username; 
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
