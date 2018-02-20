package e.sky64.retrofit_practice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.AssignmentList;
import e.sky64.retrofit_practice.GlobalUserApplication;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AssignmentActivity extends AppCompatActivity {

    //하단 리스트뷰
    ListView asListView;

    //과제 본문 내용 TextView
    TextView texts;

//    //위의 TextView에 담길 과제 상세 내용
//    private String contentText;

    private String hw_no;
    int is_student;

    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="homework";
    private static final String TAG_NAME="hw_name";
    private static final String TAG_CONTENT ="hw_content";
    private static final String TAG_DUE ="hw_due";

    //FileUploadActivity로 넘어가는 버튼, 관리자 과제 수정, 관리자 과제 삭제
    private Button uploadButton,editBtn,deleteBtn;

    android.support.v7.app.ActionBar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        //뒤로 가기 버튼 추가
//        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        hw_no = intent.getStringExtra("hw_no");
        Toast.makeText(AssignmentActivity.this,hw_no, Toast.LENGTH_SHORT).show();

        asListView = (ListView) findViewById(R.id.ListView);
        texts = (TextView) findViewById(R.id.textView);

        uploadButton = (Button) findViewById(R.id.button3);
        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent moveToFileUpload =  new Intent(AssignmentActivity.this, FileUploadActivity.class);
                startActivity(moveToFileUpload);
            }
        });

        editBtn = (Button) findViewById(R.id.btn_delete);
        deleteBtn = (Button) findViewById(R.id.btn_edit);

        getAssignmentInfo();

        GlobalUserApplication userApplication = (GlobalUserApplication) getApplication();
        is_student=userApplication.getIsStudent();

        //관리자인 경우
        if(is_student==0){
            editBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(AssignmentActivity.this,EditAssignmentActivity.class);
                    intent.putExtra("hw_no",hw_no);
                    startActivity(intent);

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }else{  //만약 학생인 경우
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);

        }

    }
    private void getAssignmentInfo() {
        // Gson object를 만들어서 Json을 읽어와서 Gson으로 해석할 수 있도록 함
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<AssignmentList>> call = api.getAssignmentInfo(Integer.parseInt(hw_no));

        call.enqueue(new Callback<List<AssignmentList>>() {

            @Override
            public void onResponse(Call<List<AssignmentList>> call, Response<List<AssignmentList>> response) {
                List<AssignmentList> asList = response.body();

//                String[] as = new String[asList.size()];
                String content,due;

                content = asList.get(0).getHw_content();
                due = asList.get(0).getHw_due();

                texts.setText(content);
                asListView.setFilterText(due);
//                asListView.setAdapter(new ArrayAdapter<String>(AssignmentActivity.this,
//                        android.R.layout.simple_list_item_1, Integer.parseInt(due)));


//                asListView.setAdapter(new ArrayAdapter<String>(AssignmentActivity.this, android.R.layout.simple_list_item_1, as));
            }

            @Override
            public void onFailure(Call<List<AssignmentList>> call, Throwable t) { //만약 db에서 데이터 갖고오는 것을 실패하였을 경우
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
