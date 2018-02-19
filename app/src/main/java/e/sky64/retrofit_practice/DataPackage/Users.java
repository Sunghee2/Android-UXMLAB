package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sky64 on 2018-02-18.
 */

public class Users {
    // initailize variables;
    @SerializedName("ID")
    private int ID;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("is_student")
    private int is_student;

    public Users(int ID, String password, String name, String email, int is_student) {
        this.ID = ID;
        this.password = password;
        this.name = name;
        this.email = email;
        this.is_student = is_student;
    }

    //getters & setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIs_student() {
        return is_student;
    }

    public void setIs_student(int is_student) {
        this.is_student = is_student;
    }


}
