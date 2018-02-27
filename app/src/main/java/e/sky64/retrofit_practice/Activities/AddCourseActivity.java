package e.sky64.retrofit_practice.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.Api.ApiUrl;
import e.sky64.retrofit_practice.Models.Result;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 630su on 2018-02-19.
 */

public class AddCourseActivity extends AppCompatActivity {
    private EditText edit_course_key;
    private EditText edit_course_no;
    private EditText edit_course_name;
    private EditText edit_professor;
    private EditText edit_description;
    private Button btn_add;
    private TextView date_text;
    private Calendar mCurrentDate;
    private int day, month, year;

    private void initUI() {
        edit_course_key = (EditText) findViewById(R.id.edit_course_key);
        edit_course_no = (EditText) findViewById(R.id.edit_course_no);
        edit_course_name = (EditText) findViewById(R.id.edit_course_name);
        edit_professor = (EditText)findViewById(R.id.edit_professor);
        edit_description = (EditText)findViewById(R.id.edit_description);
        btn_add = (Button) findViewById(R.id.button);
        date_text = (TextView) findViewById(R.id.text_date);
        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;
        date_text.setText(year+"-"+month+"-"+day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        initUI();

        date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCourseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        date_text.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCourseForm(view);
            }
        });
    }

    // 강의 추가 했을 때 필요한 정보를 포함했는지 체크하는 함수
    private void checkCourseForm(View view) {
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
            // 모든 정보를 입력했으면 addCourse 함수 실행.
            addCourse(key, no, name, professor, date, description);
        }
    }

    // checkCourseForm을 한 뒤 강의를 추가하는 함수
    private void addCourse(String key, String no, String name, String professor, String date, String description) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // api 인터페이스 생성
        Api api = retrofit.create(Api.class);

        Call<Result> call = api.addCourse(key, no, name, professor, date, description);

        call.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    // 강의가 등록된 리스트를 새로 업데이트하기 위해서.
                    Intent intent = new Intent(AddCourseActivity.this, CourseListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                } else { // 에러가 존재하는 경우
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
