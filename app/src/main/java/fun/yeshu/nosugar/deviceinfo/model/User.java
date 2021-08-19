package fun.yeshu.nosugar.deviceinfo.model;

import com.google.gson.annotations.Expose;

public class User {
    @Expose
    private String firstName;
    @Expose(serialize = false) private String lastName;
    @Expose (serialize = false, deserialize = false) private String emailAddress;
    private String password;

    public User(String firstName, String lastName, String emailAddress, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
    }
}
