package e.sky64.retrofit_practice.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import e.sky64.retrofit_practice.Adapters.CourseListAdapter;
import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.Course;
import e.sky64.retrofit_practice.DataPackage.Courses;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 630su on 2018-02-19.
 */

public class EditCourseActivity extends AppCompatActivity {
    private EditText edit_course_key;
    private EditText edit_course_no;
    private EditText edit_course_name;
    private EditText edit_professor;
    private EditText edit_description;
    private Button btn_edit;
    private TextView date_text;
    private int day, month, year;
    private Course course;
    private String origin_course_no;

    private void initUI() {
        edit_course_key = (EditText) findViewById(R.id.edit_course_key);
        edit_course_no = (EditText) findViewById(R.id.edit_course_no);
        edit_course_name = (EditText) findViewById(R.id.edit_course_name);
        edit_professor = (EditText)findViewById(R.id.edit_professor);
        edit_description = (EditText)findViewById(R.id.edit_description);
        btn_edit = (Button) findViewById(R.id.button);
        date_text = (TextView) findViewById(R.id.text_date);

        // 강의 정보를 불러옴
        readCourse(origin_course_no);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent intent = getIntent();
        origin_course_no = intent.getStringExtra("course_no");

        initUI();

        month = month+1;
        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        date_text.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkCourse(view, origin_course_no);
            }
        });
    }

    // 강의 번호를 이용해서 강의 정보를 가져오는 함수
    private void readCourse(String origin_course_no) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Course>> call = api.readCourse(origin_course_no);


        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                // 강의를 받아옴
                course = response.body().get(0);

                // 강의 정보를 editText에 입력.
                edit_course_key.setText(course.getCourse_key());
                edit_course_no.setText(course.getCourseNo());
                edit_course_name.setText(course.getCourseName());
                edit_professor.setText(course.getProfessor());
                // description이 있을 때만 editText에 입력.
                String description = course.getDescription();
                if(!description.equals("null") || !description.equals("") || !description.equals(null)) {
                    edit_description.setText(description);
                }
                // 시작날짜는 -구분으로 되어있어서 년월일 나눠서 입력
                String date[] = course.getStart_date().split("[-]");
                year = Integer.parseInt(date[0]);
                month = Integer.parseInt(date[1]);
                day = Integer.parseInt(date[2]);
                date_text.setText(year+"-"+month+"-"+day);

            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // 강의 수정 전에 필요한 정보들이 모두 입력되었는지 확인하는 함수
    private void checkCourse(View view, String origin_course_no) {
        String key = edit_course_key.getText().toString();
        String no = edit_course_no.getText().toString();
        String name = edit_course_name.getText().toString();
        String professor = edit_professor.getText().toString();
        String date = date_text.getText().toString();
        String description = edit_description.getText().toString();

        if(key==null || key.equals("")){
            Toast.makeText(getApplicationContext(), "key를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (no==null || no.equals("")){
            Toast.makeText(getApplicationContext(), "강의번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if(name==null || name.equals("")){
            Toast.makeText(getApplicationContext(), "강의이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if(professor==null || professor.equals("")){
            Toast.makeText(getApplicationContext(), "교수이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 모든 정보가 입력되었으면 updateCourse함수 실행.
            editCourse(origin_course_no, key, no, name, professor, date, description);
        }
    }

    // 입력한 정보를 토대로 강의 정보를 수정하는 함수.
    private void editCourse(String origin_course_no, String key, String no, String name, String professor, String date, String description) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Result>> call = api.editCourse(origin_course_no, key, no, name, professor, date, description);

        call.enqueue(new Callback<List<Result>>() {

            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                // 강의 수정의 결과를 받아옴.
                int result = response.body().get(0).getResult();

                if(result==0) { // 강의 수정이 안됨.
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                } else if (result==1) { // 강의 수정 성공
                    Toast.makeText(getApplicationContext(), "성공적으로 강의를 수정했습니다.", Toast.LENGTH_SHORT).show();

                    // 강의가 수정된 리스트를 새로 업데이트하기 위해서.
                    Intent intent = new Intent(EditCourseActivity.this, CourseListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                }
            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
