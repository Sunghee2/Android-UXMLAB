package e.sky64.retrofit_practice.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class EditAssignmentActivity extends AppCompatActivity {
    //과제 번호
    private String hw_no;
    //과제명, 상세 내용, 마감날짜, 마감시간
    private EditText asName,asContent,inDate,inTime;

    //마감 날짜, 시간, 과제 등록
    private Button btnDatePicker, btnTimePicker, btnAddAssignment;
    List<AssignmentList> assignment;

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

        readAssignment(Integer.parseInt(hw_no));

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

    // 과제 번호로 과제의 다른 데이터를 가져옴
    private void readAssignment(int hw_no) {
        // Gson object를 만들어서 Json을 읽어와서 Gson으로 해석할 수 있도록 함
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<AssignmentList>> call = api.readAssignment(hw_no);

        call.enqueue(new Callback<List<AssignmentList>>() {
            @Override
            public void onResponse(Call<List<AssignmentList>> call, Response<List<AssignmentList>> response) {

                assignment = response.body();
                //기한을 날짜와 시간으로 자름.
                String[] as;
                String date,time;
                String dueAndTime = assignment.get(0).getHw_due();
                as = dueAndTime.split(" ");
                date = as[0];
                time = as[1];

                //과제 정보 값을 가져와서 edittext에 넣음
                asName.setText(assignment.get(0).getHw_name());
                asContent.setText(assignment.get(0).getHw_content());
                inDate.setText(date);
                inTime.setText(time);

            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<List<AssignmentList>> call, Throwable t) {
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

    //과제 수정된 항목 업데이트하기
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

        Call<List<Result>> call = api.editAssignment(hw_no,hw_name,hw_content,hw_due);

        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                //수정의 결과 받아오기
                int result = response.body().get(0).getResult();

                if(result==0) { // 수정 실패
                    Toast.makeText(getApplicationContext(), "Error! 수정 실패", Toast.LENGTH_SHORT).show();
                } else if (result==1) { // 과제 수정 성공
                    Toast.makeText(getApplicationContext(), "Success! 수정 성공", Toast.LENGTH_SHORT).show();

                    //과제 수정 성공시 AssignmentList로 다시 넘어간다.
                    Intent intent = new Intent(EditAssignmentActivity.this,AssignmentActivity.class);
                    intent.putExtra("hw_no",hw_no);

                    //EditAssignmentActivity위의 모든 액티비티 종료 후
                    //새로 호출된 AssignmentActivity를 기존 액티비티로 변경하는 플래그
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                }
            }
            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
