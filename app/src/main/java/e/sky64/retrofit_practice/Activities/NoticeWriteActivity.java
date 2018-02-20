package e.sky64.retrofit_practice.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import e.sky64.retrofit_practice.DataPackage.Board;
import e.sky64.retrofit_practice.GlobalUserApplication;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//이 액티비티는 공지글을 작성하는 액티비티입니다. (교수님 계정으로 이용가능)
public class NoticeWriteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private Button button;

    String course_no;
    Retrofit retrofit;
    Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_write);


        editTextTitle = findViewById(R.id.board_title);
        editTextContent = findViewById(R.id.board_content);
        button = findViewById(R.id.btn_submit);

        Intent intent = getIntent();

        course_no = intent.getStringExtra("course_number"); //course_number를 intent로 받아온다.


        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String board_title = editTextTitle.getText().toString();
                String board_content = editTextContent.getText().toString();
                GlobalUserApplication userApplication = (GlobalUserApplication) getApplication();
                String author = String.valueOf(userApplication.getId()); //여기서 작성자 정보는 전역변수 이용하였다. 추후에 세션 이용할것
                clickBoardWrite(board_title, board_content, author, course_no); //clickBoardWrite메소드 실행

            }
        });
    }
    //DB에 데이터를 Insert하는 작업
    public void clickBoardWrite(String board_title, String board_content, String author, final String course_no) {
        // Initialize data for Input at Edittext
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();

        // Gson object를 만들어서 Json을 읽어와서 Gson으로 해석할 수 있도록 함
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Retrofit object 생성 및 api를 통한 클래스 객체 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(Api.class);


        Call<List<Board>> call = api.getResultBoard(title, content, author, course_no);

        // response 받는 부분
        call.enqueue(new Callback<List<Board>>() {
            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                // Response의 결과를 loginResult에 저장
                //course_number를 intent로 넘어가게끔 해줘야 한다.
                List<Board> loginResult = response.body();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                intent.putExtra("course_number", course_no);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
                //onFailure에 가더라도 정상적으로 데이터 삽입이 이루어지기 때문에 성공했다는 말과 함께
                // CourseDetail로 가게끔
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                intent.putExtra("course_number", course_no);
                startActivity(intent);
            }
        });
    }
}
