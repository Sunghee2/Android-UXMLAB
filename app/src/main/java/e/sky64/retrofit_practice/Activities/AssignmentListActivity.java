package e.sky64.retrofit_practice.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.Api.ApiUrl;
import e.sky64.retrofit_practice.Models.AssignmentList;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//과제 리스트 뷰
public class AssignmentListActivity extends AppCompatActivity {
    //리스트뷰
    ListView asListView;
    String course_no;
    List<AssignmentList> asList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);

        //course_number를 intent로 받아온다.
        Intent intent = getIntent();
        course_no = intent.getStringExtra("course_number");

        asListView = (ListView) findViewById(R.id.asListView);

        //과제명과 과제 기한을 받아온다.
        getAssignments();

        //각 리스트를 선택했을 때
        asListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //과제 상세페이지(AssignmentActivity)에 intent로 hw_no를 보내주고 이동.
                Intent intent = new Intent(AssignmentListActivity.this, AssignmentActivity.class);
                intent.putExtra("hw_no", String.valueOf(asList.get(position).getHw_no()));
                startActivity(intent);
            }
        });
    }

    //과제명, 과제 기한을 받아옴.
    private void getAssignments() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<AssignmentList>> call = api.getAssignments(Integer.parseInt(course_no));

        call.enqueue(new Callback<List<AssignmentList>>() {

            @Override
            public void onResponse(Call<List<AssignmentList>> call, Response<List<AssignmentList>> response) {
                asList = response.body();

                String[] as = new String[asList.size()];
                String name,due;

                for (int i = 0; i < asList.size(); i++) {
                    name = asList.get(i).getHw_name();
                    due = asList.get(i).getHw_due();
                    as[i] = "과제명: "+name +
                            "\n\n기한: " + due + "\n";
                }
                //어댑터를 이용하여 리스트뷰에 뿌려준다.
                asListView.setAdapter(new ArrayAdapter<String>(AssignmentListActivity.this, android.R.layout.simple_list_item_1, as));
            }
            @Override
            public void onFailure(Call<List<AssignmentList>> call, Throwable t) { //만약 db에서 데이터 갖고오는 것을 실패하였을 경우
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
