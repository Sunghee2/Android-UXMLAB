package e.sky64.retrofit_practice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import e.sky64.retrofit_practice.R;

/**
 * Created by 630su on 2018-02-19.
 */

public class CourseActivity extends AppCompatActivity {
    private TextView tv_course_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        String course_no = intent.getStringExtra("course_no");

        tv_course_no = (TextView) findViewById(R.id.textView);
        tv_course_no.setText(course_no);
    }
}
