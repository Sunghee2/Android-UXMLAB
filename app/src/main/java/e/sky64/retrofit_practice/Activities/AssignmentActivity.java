package e.sky64.retrofit_practice.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import e.sky64.retrofit_practice.Adapters.AssignmentListViewAdapter;
import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.AssignmentList;
import e.sky64.retrofit_practice.DataPackage.Result;
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

    private String hw_no;
    int is_student;

    //과제 제출버튼, 관리자 과제 수정,관리자 과제 삭제
    private Button uploadButton,editBtn,deleteBtn;

    Toolbar mActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        //toolbar 생성
        mActionBar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mActionBar);

        //hw_no 받아옴.
        Intent intent = getIntent();
        hw_no = intent.getStringExtra("hw_no");

        asListView = (ListView) findViewById(R.id.ListView);
        texts = (TextView) findViewById(R.id.textView);

        //과제 제출 버튼
        uploadButton = (Button) findViewById(R.id.button3);
        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent moveToFileUpload =  new Intent(AssignmentActivity.this, FileUploadActivity.class);
                startActivity(moveToFileUpload);
            }
        });

        //관리자 과제 수정버튼
        editBtn = (Button) findViewById(R.id.btn_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AssignmentActivity.this,EditAssignmentActivity.class);
                intent.putExtra("hw_no",hw_no);
                startActivity(intent);
            }
        });

        //관리자 과제 삭제버튼
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeleteAssignment();
            }
        });

        //과제정보 받아옴
        getAssignmentInfo();

        //글로벌
        GlobalUserApplication userApplication = (GlobalUserApplication) getApplication();
        is_student=userApplication.getIsStudent();

        //관리자인 경우 과제 수정, 과제 삭제 버튼만 보이게 하고 과제 제출 버튼은 보이지 않도록 함.
        if(is_student==0){
            editBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.INVISIBLE);

        }else{  //학생인 경우 관리자가 사용하는 버튼 2개는 invisible하게 하고 과제 제출버튼만 보이도록 함.
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
            uploadButton.setVisibility(View.VISIBLE);
        }
    }

    //과제 삭제하는 함수
    private void deleteAssignment(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api  = retrofit.create(Api.class);
        Call<List<Result>> call = api.deleteAssignment(hw_no);
        call.enqueue(new Callback<List<Result>>() {

            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                int result = response.body().get(0).getResult();

                if (result==1){ //삭제 성공
                    Toast.makeText(getApplicationContext(), "과제를 삭제 했습니다.", Toast.LENGTH_SHORT).show();

                }else{ //삭제 실패
                    Toast.makeText(getApplicationContext(), "Error! 수정 실패", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {

            }
        });

    }
    public void alertDeleteAssignment() {
        new AlertDialog.Builder(AssignmentActivity.this)
                .setTitle("과제 삭제")
                .setMessage("과제를 삭제하시겠습니까?")
//                .setIcon(R.drawable.ninja)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAssignment();
//                                showToast("Thank you! You're awesome too!");
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
//                        showToast("Mike is not awesome for you. :(");
                        dialog.cancel();
                    }
                }).show();
    }

    //과제 정보 받아오는 함수.
    private void getAssignmentInfo() {
        // Gson object
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
                String name,content,due;

                name =  asList.get(0).getHw_name();
                content = asList.get(0).getHw_content();
                due = asList.get(0).getHw_due();

                texts.setText(content);
                setSupportActionBar(mActionBar);
                getSupportActionBar().setTitle(name);

                AssignmentListViewAdapter adapter = new AssignmentListViewAdapter();
                asListView.setAdapter(adapter);
                adapter.addAsList("채점상태: ","없음");
                adapter.addAsList("마감일시: ",due);
                adapter.addAsList("제출파일: ","없음");
            }

            @Override
            public void onFailure(Call<List<AssignmentList>> call, Throwable t) {
                //만약 db에서 데이터 갖고오는 것을 실패하였을 경우 에러메세지를 토스트로 출력
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
