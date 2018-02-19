package e.sky64.retrofit_practice;

import android.app.Application;

/**
 * Created by 630su on 2018-02-19.
 */

public class GlobalUserApplication extends Application {
    private int id;
    private int isStudent;

    public void setIsStudent(int student) {
        isStudent = student;
    }

    public int getIsStudent() {
        return isStudent;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
