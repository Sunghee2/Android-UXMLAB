package e.sky64.retrofit_practice.Activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import e.sky64.retrofit_practice.DataPackage.Board;
import e.sky64.retrofit_practice.GlobalUserApplication;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticeDetailActivity extends AppCompatActivity {

    TextView title_t;
    TextView content_t;
    TextView author_t;
    TextView date_t;
    Button editBtn, deleteBtn;
    private String board_no, course_no;
    int is_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        Intent intent = getIntent();
        board_no = intent.getStringExtra("board_no");
        course_no = intent.getStringExtra("course_no");

        title_t = (TextView) findViewById(R.id.textView_title);
        content_t = (TextView) findViewById(R.id.textview_content);
        author_t = (TextView) findViewById(R.id.textview_author);
        date_t = (TextView) findViewById(R.id.textview_date);

        editBtn = (Button) findViewById(R.id.edit_bttn);
        deleteBtn = (Button) findViewById(R.id.delete_bttn);
        //공지 정보 받아온다
        getBoardInfo();

        //전역변수 이용해서 학생인지 교수님인지 구분
        GlobalUserApplication userApplication = (GlobalUserApplication) getApplication();
        is_student = userApplication.getIsStudent();

        //관리자인 경우
        if (is_student == 0) {
            //버튼들 보여준다
            editBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            //수정버튼 누를경우
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(NoticeDetailActivity.this, NoticeEditActivity.class);
                    intent.putExtra("board_no", board_no);
                    intent.putExtra("course_no", course_no);
                    startActivity(intent);
                }
            });
            //삭제버튼 누를경우
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDeleteBoard();
                }
            });

        } else {  //만약 학생인 경우 버튼 숨긴다
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    //공지 삭제 구현 함수
    private void DeleteBoard() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Board>> call = api.deleteBoard(board_no);
        call.enqueue(new Callback<List<Board>>() {

            @Override //성공시
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                Toast.makeText(getApplicationContext(), "성공적으로 삭제되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                intent.putExtra("course_number", course_no);
                startActivity(intent);

            }

            @Override //실패시
            public void onFailure(Call<List<Board>> call, Throwable t) {
                //delete는 정상적으로 되지만 받아오는 값이 없게 설정하여 onFailure가 실행된다. 정상적으로 delete되므로 성공한것으로 간주^^
                Toast.makeText(getApplicationContext(), "성공적으로 삭제되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                intent.putExtra("course_number", course_no);
                startActivity(intent);
            }
        });
    }

    //삭제할 것인지 물어보는 알림창을 띄워주는 함수
    public void AlertDeleteBoard() {
        new AlertDialog.Builder(NoticeDetailActivity.this)
                .setTitle("공지 삭제")
                .setMessage("공지를 삭제하시겠습니까?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteBoard();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    //공지사항의 자세한 내용을 가져오는 함수이다.
    private void getBoardInfo() {
        // Gson object
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Board>> call = api.getBoardInfo(Integer.parseInt(board_no));

        call.enqueue(new Callback<List<Board>>() {
            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                List<Board> asList = response.body();
                String title, name, content, due;

                title = asList.get(0).getBoard_title();
                content = asList.get(0).getBoard_content();
                name = asList.get(0).getAuthor();
                due = asList.get(0).getBoard_date();

                title_t.setText(title);
                content_t.setText(content);
                author_t.setText(name);
                date_t.setText(due);
            }

            //가져오는 것 실패시
            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
