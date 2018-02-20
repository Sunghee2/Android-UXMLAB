package e.sky64.retrofit_practice.Activities;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Calendar;
import java.util.List;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.Assignment;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddAssignmentActivity extends AppCompatActivity {

    //과제명, 상세 내용, 마감날짜, 마감시간
    private EditText asName,asContent,inDate,inTime;

    //마감 날짜, 시간, 과제 등록
    private Button btnDatePicker, btnTimePicker, btnAddAssignment;

    //액션바
    android.support.v7.app.ActionBar mActionBar2;

    //강좌번호 받아오기
    private String course_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);


        asName = (EditText) findViewById(R.id.asName);
        asContent = (EditText) findViewById(R.id.asContent);
        inDate = (EditText) findViewById(R.id.in_date);
        inTime = (EditText) findViewById(R.id.in_time);

//        //액션바에 뒤로가기 버튼 추가
//        mActionBar2 = getSupportActionBar();
//        mActionBar2.setDisplayHomeAsUpEnabled(true);


        //CourseDetailActivity에서 course_number를 받아옴
        Intent intent = getIntent();
        course_no = intent.getStringExtra("course_number");
//        Toast.makeText(AddAssignmentActivity.this,course_no, Toast.LENGTH_SHORT).show();



        //마감 날짜 선택 버튼
        btnDatePicker = (Button)findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "datepicker");
            }
        });

        //마감 시간 선택 버튼
        btnTimePicker = (Button)findViewById(R.id.btn_time);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new TimePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "timepicker");
            }
        });

        //과제 등록 버튼
        btnAddAssignment = (Button)findViewById(R.id.addAssignment);
        btnAddAssignment.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                formChecker(v);
            }
        });
    }

    //액션바 뒤로가기 클릭 시 뒤로 가게 함.
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:

                // NavUtils.navigateUpFromSameTask(this);
                finish();

                return true;

        }

        return super.onOptionsItemSelected(item);
    };


    //모든 폼을 작성했는지 확인
    public void formChecker(View view){
        String name = String.valueOf(asName.getText());
        String content = String.valueOf(asContent.getText());
        String date = String.valueOf(inDate.getText());
        String time = String.valueOf(inTime.getText());
        String due = date +" "+ time;

        if(name==null || name.equals("")){
            Toast.makeText(getApplicationContext(), "과제명을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (content==null || content.equals("")){
            Toast.makeText(getApplicationContext(), "상세내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if(date==null || date.equals("")){
            Toast.makeText(getApplicationContext(), "날짜를 설정해주세요.", Toast.LENGTH_SHORT).show();
        } else if(time==null || time.equals("")){
            Toast.makeText(getApplicationContext(), "시간을 설정해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            // 모든 폼 작성시 POST
            addAssignment(Integer.parseInt(course_no),name,content,due);
        }
    }

    //날짜 선택 정보 받아오기
    public static class DatePickerDialogTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        private TextView txtDate;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

            return datepickerdialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){

            txtDate = (TextView) getActivity().findViewById(R.id.in_date);
            txtDate.setText(year+"/"+(month+1)+"/"+day);
        }
    }

    //시간 선택 정보 받아오기
    public static class TimePickerDialogTheme extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.Theme_AppCompat_Dialog_Alert,this,hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

            return timePickerDialog;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView txtTime = (TextView) getActivity().findViewById(R.id.in_time);
            txtTime.setText(hourOfDay+":"+minute+":00");
        }
    }

    //Assignment 추가
    private void addAssignment(int course_no, String hw_name, String hw_content, String hw_due) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Result>> call = api.addAssignment(course_no, hw_name, hw_content, hw_due);

        call.enqueue(new Callback<List<Result>>() {

            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                // 강의 추가하는 결과가 어떻게 되었는지. result를 받아옴.
                int result = response.body().get(0).getResult();

                if (result == 0) { // 강의 추가가 안된 경우
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                } else if (result == 1) { // 강의 추가 성공
                    Toast.makeText(getApplicationContext(), "성공적으로 강의를 추가했습니다.", Toast.LENGTH_SHORT).show();

                    // 강의가 추가된 리스트를 새로 업데이트하기 위해서.
                    Intent intent = new Intent(AddAssignmentActivity.this, AssignmentListActivity.class);
                    startActivity(intent);
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
