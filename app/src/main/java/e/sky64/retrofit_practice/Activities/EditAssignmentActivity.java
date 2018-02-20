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
import e.sky64.retrofit_practice.DataPackage.Assignment;
import e.sky64.retrofit_practice.DataPackage.Course;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditAssignmentActivity extends AppCompatActivity {
    //과제 번호
    private String hw_no;
    //과제명, 상세 내용, 마감날짜, 마감시간
    private EditText asName,asContent,inDate,inTime;

    //마감 날짜, 시간, 과제 등록
    private Button btnDatePicker, btnTimePicker, btnAddAssignment;
    Assignment assignment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);

        Intent intent = getIntent();
        hw_no = intent.getStringExtra("hw_no");

        asName = (EditText) findViewById(R.id.asName);
        asContent = (EditText) findViewById(R.id.asContent);
        inDate = (EditText) findViewById(R.id.in_date);
        inTime = (EditText) findViewById(R.id.in_time);

        //마감 날짜 선택 버튼
        btnDatePicker = (Button)findViewById(R.id.btn_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new AddAssignmentActivity.DatePickerDialogTheme();
                dialogfragment.show(getFragmentManager(), "datepicker");
            }
        });

        //마감 시간 선택 버튼
        btnTimePicker = (Button)findViewById(R.id.btn_time);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new AddAssignmentActivity.TimePickerDialogTheme();
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

    // 강의 번호를 이용해서 강의 정보를 가져오는 함수
    private void readAssignment(int hw_no) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Assignment>> call = api.readAssignment(hw_no);

        call.enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                // 강의를 받아옴
                assignment = response.body().get(0);

                //기한을 날짜와 시간으로 자름
                String[] dueAndTime = assignment.getHw_due().split("\\s");
                String due = dueAndTime[0];
                String time = dueAndTime[1];

                //과제 정보 값을 가져와서 edittext에 넣음
                asName.setText(assignment.getHw_name());
                asContent.setText(assignment.getHw_content());
                inDate.setText(due);
                inTime.setText(time);

            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
            editAssignment(name,content,due);
        }
    }

    private void editAssignment(String hw_name, String hw_content, String hw_due) {
        // Gson object를 만들어서 Json을 읽어와서 Gson으로 해석할 수 있도록 함
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Result>> call = api.editAssignment(hw_name,hw_content,hw_due);

        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                //수정의 결과 받아오기
                int result = response.body().get(0).getResult();
                if(result==0) { // 수정 실패
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                } else if (result==1) { // 강의 수정 성공
                    Toast.makeText(getApplicationContext(), "성공적으로 강의를 수정했습니다.", Toast.LENGTH_SHORT).show();
                }

            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
}
