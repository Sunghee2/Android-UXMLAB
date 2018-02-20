package e.sky64.retrofit_practice.DataPackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 630su on 2018-02-19.
 */

// 개별 강좌의 키, 번호, 이름, 교수명, 설명, 시작날짜
public class Course {
    @SerializedName("course_key")
    @Expose
    private String course_key;
    @SerializedName("course_no")
    @Expose
    private String courseNo;
    @SerializedName("course_name")
    @Expose
    private String courseName;
    @SerializedName("professor")
    @Expose
    private String professor;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("start_date")
    @Expose
    private String start_date;

    public Course(String course_key, String courseNo, String courseName, String professor, String description, String start_date) {
        this.course_key = course_key;
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.professor = professor;
        this.description = description;
        this.start_date = start_date;
    }

    public Course(String courseNo, String courseName, String professor) {
        this.courseNo = courseNo;
        this.courseName = courseName;
        this.professor = professor;
    }

    public void setCourse_key(String course_key) {
        this.course_key = course_key;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getCourse_key() {

        return course_key;
    }

    public String getCourseNo() {
        return courseNo;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public String getDescription() {
        return description;
    }

    public String getStart_date() {
        return start_date;
    }
}
