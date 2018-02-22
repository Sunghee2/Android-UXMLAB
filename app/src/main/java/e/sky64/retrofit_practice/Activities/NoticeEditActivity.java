package e.sky64.retrofit_practice.Activities;

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
import e.sky64.retrofit_practice.DataPackage.Board;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeEditActivity extends AppCompatActivity{
    private String board_no,course_no;
    private EditText asName,asContent;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_edit);

        Intent intent = getIntent();
        board_no = intent.getStringExtra("board_no");
        course_no = intent.getStringExtra("course_no");

        asName = (EditText) findViewById(R.id.board_title);
        asContent = (EditText) findViewById(R.id.board_content);

        readBoard(Integer.parseInt(board_no));

        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formChecker(v);
            }
        });
    }

    //공지 제목, 내용을 editText에 표시해주게끔
    private void readBoard(int board_no) {
        // Gson object를 만들어서 Json을 읽어와서 Gson으로 해석할 수 있도록 함
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Board>> call = api.getBoardInfo(board_no);

        call.enqueue(new Callback<List<Board>>() {
            @Override //공지 내용 가져오는 것 성공시
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                List<Board> asList = response.body();
                String title,content;

                title =  asList.get(0).getBoard_title();
                content = asList.get(0).getBoard_content();

                asName.setText(title);
                asContent.setText(content);

            }
            @Override  //공지 정보 가져오는 것 실패ㅣ
            public void onFailure(Call<List<Board>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //editText에 모두 채웠나 체크 함수
    public void formChecker(View view){
        String name = String.valueOf(asName.getText());
        String content = String.valueOf(asContent.getText());


        if(name==null || name.equals("")){
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (content==null || content.equals("")){
            Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();

        } else {
            editBoard(name,content);
        }
    }
    //공지글 수정 함수
    private void editBoard(String title, String content) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);


        Call<List<Board>> call = api.editBoard(title,content,board_no);
        call.enqueue(new Callback<List<Board>>() {
            @Override //성공 시
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {

                Toast.makeText(getApplicationContext(), "성공적으로 수정되었습니다.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                intent.putExtra("course_number", course_no);
                startActivity(intent);
            }
            @Override //실패 시
            public void onFailure(Call<List<Board>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
