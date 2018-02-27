package e.sky64.retrofit_practice.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import e.sky64.retrofit_practice.Adapters.CourseListAdapter;
import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.Api.ApiUrl;
import e.sky64.retrofit_practice.Models.Course;
import e.sky64.retrofit_practice.Models.Courses;
import e.sky64.retrofit_practice.Models.Result;
import e.sky64.retrofit_practice.R;
import e.sky64.retrofit_practice.helper.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton add_course_btn;
    private CourseListAdapter courseListAdapter;
    private ListView myCourseListView;
    private ListView allCourseListView;
    private NestedScrollView scrollView;
    private List<Course> myCourseList;
    private List<Course> allCourseList;
    private Courses courses;


    private void initUI() {
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);

        if(SharedPrefManager.getInstance(this).getUser().getIs_student()==0){
            // 관리자 계정일 때 나타나는 강의 추가 버튼임
            add_course_btn = (FloatingActionButton) findViewById(R.id.add_course_btn);
            // 강의 추가 버튼 보이도록 수정.
            add_course_btn.setVisibility(View.VISIBLE);
            // 버튼때문에 다른 내용이 가려지게 되어서 스크롤이 내려갈 때 안보이도록 설정함.
            scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollY>oldScrollY) {
                        add_course_btn.hide();
                    } else {
                        add_course_btn.show();
                    }
                }
            });
        }
        myCourseListView = (ListView) findViewById(R.id.listview_my_course);
        allCourseListView = (ListView) findViewById(R.id.listview_all_course);

        // 툴바 title 없이 설정함.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        initUI();

        getCourses();

        // 등록된 강의를 누르면 강의 번호와 함께 강의 상세 페이지로 이동함.
        myCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
                intent.putExtra("course_no", String.valueOf(myCourseList.get(i).getCourseNo()));
                startActivity(intent);
            }
        });


        // 등록되지 않은 강의를 누르면 강의를 등록할 수 있는 showRegisterCourse 함수가 실행됨.
        allCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showRegisterCourse(String.valueOf(allCourseList.get(i).getCourseNo()));
            }
        });


    }



    // Course 정보를 가져옴.
    private void getCourses() {
        // Retrofit에서 제공하는 기본 uri에 연결하여 Json 데이터를 Gson으로 가져올 수 있도록 한다
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // api 인터페이스 생성
        Api api = retrofit.create(Api.class);

        // 호출을 위한 object를 생성. api 인터페이스에서 정의한 내용을 가져오게 된다
        Call<Courses> call = api.getCourse(SharedPrefManager.getInstance(this).getUser().getId());


        call.enqueue(new Callback<Courses>() {

            // call 한 후의 처리를 결정하는 메서드
                @Override
                public void onResponse(Call<Courses> call, Response<Courses> response) {
                    // 전체 결과를 가져옴.
                    courses = response.body();

                    // 등록된 강의가 있는지 확인.
                    boolean is_my_course = courses.getIsMyCourse();

                    if(is_my_course) { // 내 강의가 있을 경우
                        myCourseList = courses.getMyCourse();

                        courseListAdapter = new CourseListAdapter(CourseListActivity.this, myCourseList, SharedPrefManager.getInstance(CourseListActivity.this).getUser().getIs_student());
                        myCourseListView.setAdapter(courseListAdapter);
                        setListViewHeightBaseOnChildren(myCourseListView);
                    } else if(SharedPrefManager.getInstance(CourseListActivity.this).getUser().getIs_student()==1) { // 등록된 강의가 없고 학생이면 등록된 강의가 없음을 알려준다.
                        myCourseList = new ArrayList<>();
                        myCourseList.add(new Course("", "등록된 강의가 없습니다.", ""));
                        courseListAdapter = new CourseListAdapter(CourseListActivity.this, myCourseList, 1);
                        myCourseListView.setAdapter(courseListAdapter);
                        setListViewHeightBaseOnChildren(myCourseListView);
                    }

                    // 전체 강의
                    allCourseList = courses.getAllCourse();

                    courseListAdapter = new CourseListAdapter(CourseListActivity.this, allCourseList,SharedPrefManager.getInstance(CourseListActivity.this).getUser().getIs_student());
                    allCourseListView.setAdapter(courseListAdapter);
                    setListViewHeightBaseOnChildren(allCourseListView);
                }

                // 실패시 처리하는 방법을 정하는 메서드
                @Override
                public void onFailure(Call<Courses> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    // add_course_btn(강의 추가) 버튼을 눌렀을 때 AddCourseActivity로 이동함.
    public void ClickAddCourse(View view) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }


    // 리스트뷰 높이를 강의의 개수만큼 조절하는 함수
    public void setListViewHeightBaseOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter==null){
            return;
        }

        int totalHeight = 0;

        for(int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    // 강의를 등록할 때 실행되는 함수
    public void showRegisterCourse(final String course_no){
        // alert창 실행함.
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("강의 등록");
        alert.setMessage("키를 입력해주세요.");

        // editText를 만들어서 키를 입력할 수 있는 text창을 만듦.
        final EditText edit_key = new EditText(this);
        alert.setView(edit_key);

        // 입력 버튼을 누르면 입력한 key가 맞는지 확인하는 checkKey 함수를 실행함.
        alert.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String course_key = edit_key.getText().toString();
                checkKey(course_key, course_no);
            }
        });

        // 취소 버튼을 누를 때.
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    // 강의 등록할 때 입력한 key가 맞는지 확인.
    public void checkKey(String course_key, String course_no) {
        // Retrofit에서 제공하는 기본 uri에 연결하여 Json 데이터를 Gson으로 가져올 수 있도록 한다
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // api 인터페이스 생성
        Api api = retrofit.create(Api.class);

        Call<Result> call = api.registerCourse(course_key, course_no, SharedPrefManager.getInstance(this).getUser().getId());

        call.enqueue(new Callback<Result>() {

            // call 한 후의 처리를 결정하는 메서드
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                    // 강의가 등록된 리스트를 새로 업데이트하기 위해서.
                    Intent intent = new Intent(CourseListActivity.this, CourseListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();
                } else { // 에러가 존재하는 경우
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            // 실패시 처리하는 방법을 정하는 메서드
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 툴바의 옵션 메뉴 설정해줌
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    // 옵션 메뉴 중 무언가 선택했을 때 실행하는 것
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            // 내 정보 수정을 눌렀을 때
            case R.id.toolbar_edit_user:
                Toast.makeText(getApplicationContext(), "변경", Toast.LENGTH_SHORT).show();
                return true;
            // 설정을 눌렀을 때
            default:
                Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}

