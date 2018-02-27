package e.sky64.retrofit_practice.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 630su on 2018-02-19.
 */

public class Result {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    @SerializedName("course")
    private Course course;

    public Result(Boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() { return course; }
}

