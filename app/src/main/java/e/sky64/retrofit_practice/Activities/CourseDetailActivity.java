package e.sky64.retrofit_practice.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import e.sky64.retrofit_practice.R;

/**
 * Created by 630su on 2018-02-19.
 */

public class CourseDetailActivity extends AppCompatActivity {

    FloatingActionButton fab, fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen = false;
    String course_no;
    int is_student;
    //리스트 목록에 띄울 것들
    static final String[] LIST_MENU = {"공지사항", "과제", "강의자료"};

    /*
    이 메소드는 펼쳐지는 Floating Button과 관련됨
     */
    private void showFABMenu() {
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }
    /*
      이 메소드는 닫혀지는 Floating Button과 관련됨
       */
    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
//만약 뒤로가기 버튼을 눌렀다면
    @Override
    public void onBackPressed() {
        if (isFABOpen) { //fab버튼이 펼쳐져 있었을 경우, 다시 닫는다
            closeFABMenu();
        } else { //그것이 아니라면, onBackPressed()메소드를 기존과 같이 실행
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);


//course_no를 intent로 받아온다.
        Intent intent = getIntent();
        course_no = intent.getStringExtra("course_no");

        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);
        fabLayout3 = (LinearLayout) findViewById(R.id.fabLayout3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabBGLayout = findViewById(R.id.fabBGLayout);
         //fab 버튼을 눌렀을때, 닫혀있다면 펼치고 펼쳐져있다면 닫는 메소드 실행




        //만약 로그인한 사람이 교수님계정이라면 fab버튼을 보여준다. 이는 곧 파일 업로드, 과제 작성, 게시글 작성이 가능하다는 의미이다.
        //하지만 학생계정이라면 이를 숨겨서 게시글 작성이나 파일 업로드 접근 권한이 없도록 하였다. 이는 임시로 전역변수를 이용하여 구현하였다.
        //추후에 세션을 이용하도록 변경할 것.
//        GlobalUserApplication userApplication = (GlobalUserApplication) getApplication();
//        is_student=userApplication.getIsStudent();

        if(is_student==0){  //만약 교수님인 경우
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isFABOpen) {
                        showFABMenu();
                    } else {
                        closeFABMenu();
                    }
                }
            });

            fabBGLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeFABMenu();
                }
            });
            //fab1버튼은 파일업로드를 하는 곳으로 넘어가게 된다.
            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), FileUploadActivity.class);
                    Intent intent2 = getIntent();
                    String id = intent2.getStringExtra("id");
                    intent.putExtra("id", id);
                    //course 번호대로 분류

                    startActivity(intent);
                }
            });
            //fab2버튼은 과제 생성 페이지로 넘어가게 된다.
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CourseDetailActivity.this,AddAssignmentActivity.class);
                    intent.putExtra("course_number",course_no);
                    startActivity(intent);


                }
            });
            //fab3버튼은 공지사항을 작성하는 페이지로 넘어가게 된다.
            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), NoticeWriteActivity.class);
                    intent.putExtra("course_number", course_no);
                    startActivity(intent);
                }
            });

        }else{  //만약 학생인 경우
            fab.setVisibility(View.INVISIBLE);  //버튼 숨김

        }
        //여기까지가 FloatingButton관련.

        //이 아래에서부턴 LIST관련.

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);

        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                //첫번째 리스트를 클릭하면 공지사항을 보여주는 페이지로 넘어가게 된다.
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                    intent.putExtra("course_number", course_no);
                    startActivity(intent);
                } else if (position == 1) { //두번째 리스트를 클릭하면 과제를 보여주는 페이지로 넘어가게 된다.
                    Intent intent = new Intent(getApplicationContext(), AssignmentListActivity.class);
                    intent.putExtra("course_number", course_no);
                    startActivity(intent);


                } else { //세번째 리스트를 클릭하면 자료를 다운받을 수 있는 페이지로 넘어가게 된다.
                    Toast.makeText(getApplicationContext(), "강의 자료 다운", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}


