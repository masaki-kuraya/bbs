package name.kuraya.masaki.bbs.ui.model.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class Signup {

    @Pattern(regexp="^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    private String email;
    @Pattern(regexp="^[^\\s].*[^\\s]$")
    private String name;
    @NotEmpty
    private String password;

    public Signup() {
    }

    public Signup(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean validate() {
        if (!email.matches("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            return true;
        } else if (!name.matches("^[^\\s].*[^\\s]$")) {
            return true;
        } else if (password.isEmpty()) {
            return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}