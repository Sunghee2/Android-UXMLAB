package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by seungyeonlee on 2018. 2. 21..
 */

public class AssignmentList {
    @SerializedName("courseNo")
    @Expose
    private String courseNo;

    @SerializedName("asName")
    @Expose
    private String asName;

    @SerializedName("asDue")
    @Expose
    private String asDue;

    public AssignmentList(String asName, String asDue) {
//        this.courseNo = courseNo;
        this.asName = asName;
        this.asDue = asDue;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

    public String getAsDue() {
        return asDue;
    }

    public void setAsDue(String asDue) {
        this.asDue = asDue;
    }
}
