package e.sky64.retrofit_practice.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.Board;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//이 액티비티는 공지글을 보여주는 액티비티입니다. (교수님/학생 계정으로 이용 가능)

public class NoticeActivity extends AppCompatActivity {
    ListView listView;
    String course_no;
    List<Board> boardList;
    String[] board_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        //course_number를 intent로 받아온다.
        Intent intent = getIntent();
        course_no = intent.getStringExtra("course_number");

        listView = (ListView) findViewById(R.id.list_view);
        //각 리스트를 선택했을 때

        getBoards();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
                intent.putExtra("board_no", board_no[position]);
                intent.putExtra("course_no", course_no);
                startActivity(intent);
            }
        });
    }

    private void getBoards() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Board>> call = api.getBoard(Integer.parseInt(course_no));


        call.enqueue(new Callback<List<Board>>() {

            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                List<Board> boardList = response.body();

                String[] boards = new String[boardList.size()]; //게시글의 개수만큼 리스트로 보여주게 된다.
                board_no=new String[boardList.size()];
                for (int i = 0; i < boardList.size(); i++) {
                    boards[i] = "제목  :  " + boardList.get(i).getBoard_title() + "\n\n내용  :  " + boardList.get(i).getBoard_content() +
                            "\n\n작성일  :  " + boardList.get(i).getBoard_date() + "\n";
                    board_no[i]=String.valueOf(boardList.get(i).getBoard_no());
                }

                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, boards));
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) { //만약 db에서 데이터 갖고오는 것을 실패하였을 경우
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}