package e.sky64.retrofit_practice.Activities;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import e.sky64.retrofit_practice.DataPackage.AssignmentList;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAssignmentActivity extends AppCompatActivity {

    //과제명, 상세 내용, 마감날짜, 마감시간
    private EditText asName,asContent,inDate,inTime;

    //마감 날짜, 시간, 과제 등록
    private Button btnDatePicker, btnTimePicker, btnAddAssignment;

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


        //CourseDetailActivity에서 course_number를 받아옴
        Intent intent = getIntent();
        course_no = intent.getStringExtra("course_number");



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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<AssignmentList>> call = api.addAssignment(course_no, hw_name, hw_content, hw_due);

        call.enqueue(new Callback<List<AssignmentList>>() {

            @Override
            public void onResponse(Call<List<AssignmentList>> call, Response<List<AssignmentList>> response) {
                // 결과가 어떻게 되었는지. result를 받아옴.
                int result = response.body().get(0).getResult();
                String course_number = response.body().get(0).getCourse_no();

                if (result == 0) { //추가 실패
                    Toast.makeText(getApplicationContext(), "Fail! 과제 생성 실패", Toast.LENGTH_SHORT).show();
                } else if (result == 1) { //과제 추가 성공
                    Toast.makeText(getApplicationContext(), "Success! 과제 생성 성공", Toast.LENGTH_SHORT).show();

                    //과제 추가 성공시 과제 리스트로 이동.
                    Intent intent = new Intent(AddAssignmentActivity.this,AssignmentListActivity.class);
                    intent.putExtra("course_number",course_number);
                    startActivity(intent);
                }
            }

            // 실패
            @Override
            public void onFailure(Call<List<AssignmentList>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }

}
