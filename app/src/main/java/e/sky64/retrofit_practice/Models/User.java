package e.sky64.retrofit_practice.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sky64 on 2018-02-18.
 */

public class User {

    // initailize variables;
    private int id;
    private String password;
    private String name;
    private String email;
    private int is_student;

    // 필요한 User의 모양대로 overload해서 constructor를 추가한다.
    public User(int id, String password, String name, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    public User(int id, String name, String email, int is_student) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.is_student = is_student;
    }

    //getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setEmail(String email) { this.email = email; }

    public int getIs_student() {
        return is_student;
    }

    public void setIs_student(int is_student) {
        this.is_student = is_student;
    }

    public User getUser() {
        return this;
    }

}
