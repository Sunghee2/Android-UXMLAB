package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by seungyeonlee on 2018. 2. 21..
 */

public class AssignmentList {


    @SerializedName("course_no")
    @Expose
    private String course_no;
    public String getHw_no() {
        return hw_no;
    }

    public void setHw_no(String hw_no) {
        this.hw_no = hw_no;
    }

    @SerializedName("hw_no")
    @Expose

    private String hw_no;

    @SerializedName("hw_name")
    @Expose
    private String hw_name;

    @SerializedName("hw_due")
    @Expose
    private String hw_due;

    @SerializedName("result")
    @Expose
    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getHw_name() {
        return hw_name;
    }

    public void setHw_name(String hw_name) {
        this.hw_name = hw_name;
    }

    public String getHw_due() {
        return hw_due;
    }

    public void setHw_due(String hw_due) {
        this.hw_due = hw_due;
    }
}
