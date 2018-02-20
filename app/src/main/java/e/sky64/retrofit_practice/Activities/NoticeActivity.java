package e.sky64.retrofit_practice.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
//이 액티비티는 강의 게시물 상세내용 보여주는 액티비티입니다.
// 정확하게는 각 강의 번호별로 해당하는 게시물을 보여줘야 하는 것인데, 이는 성희언니꺼와 합친 후에 구현하도록 하겠습니다.

public class NoticeActivity extends AppCompatActivity {
    ListView listView;
    String course_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Intent intent= getIntent();
        course_no= intent.getStringExtra("course_number");

        listView = (ListView) findViewById(R.id.list_view);
        //      listView.setOnItemClickListener(this);
        getBoards();
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

                String[] boards = new String[boardList.size()];

                for (int i = 0; i < boardList.size(); i++) {
                    boards[i] = "Title : " + boardList.get(i).getBoard_title() + "\nContent : " + boardList.get(i).getBoard_content()+
                            "\nDate : " + boardList.get(i).getBoard_date();
                }

                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, boards));
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}