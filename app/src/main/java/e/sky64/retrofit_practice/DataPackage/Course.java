package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 630su on 2018-02-19.
 */

// 개별 강좌의 번호, 이름, 교수명
public class Course {
    @SerializedName("course_no")
    @Expose
    private String courseNo;
    @SerializedName("course_name")
    @Expose
    private String courseName;
    @SerializedName("professor")
    @Expose
    private String professor;

    public Course(String courseNo, String courseName, String professor) {
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.professor = professor;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

}
