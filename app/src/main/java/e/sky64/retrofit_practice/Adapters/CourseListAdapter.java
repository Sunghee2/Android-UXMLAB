package e.sky64.retrofit_practice.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import e.sky64.retrofit_practice.Activities.CourseDetailActivity;
import e.sky64.retrofit_practice.Activities.CourseListActivity;
import e.sky64.retrofit_practice.Activities.EditCourseActivity;
import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.Course;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 630su on 2018-02-19.
 */

public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private int isStudent;

    private LinearLayout courseInfoLayout;

    private TextView courseNumTextView;
    private TextView courseNameTextView;
    private TextView professorTextView;

    private ImageButton edit_course_btn;
    private ImageButton delete_course_btn;



    // 강의 등록, 추가, 수정, 삭제 후에 반영된 리스트를 다시 보여주기 위해서 사용
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }


    // 생성자
    public CourseListAdapter(Context context, List<Course> courseList, int isStudent) {
        this.context = context;
        this.courseList = courseList;
        this.isStudent = isStudent;
    }

    @Override
    public int getCount() {
        return this.courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.course_list, null);

        courseNumTextView = (TextView) view.findViewById(R.id.courseNumTextView);
        courseNameTextView = (TextView) view.findViewById(R.id.courseNameTextView);
        professorTextView = (TextView) view.findViewById(R.id.professorTextView);
        edit_course_btn = (ImageButton) view.findViewById(R.id.btn_edit_course);
        delete_course_btn = (ImageButton) view.findViewById(R.id.btn_delete_course);

        // 강의 번호, 이름, 교수명을 받아와서 보여줌.
        courseNumTextView.setText(String.valueOf(courseList.get(position).getCourseNo()));
        courseNameTextView.setText(courseList.get(position).getCourseName());
        professorTextView.setText(courseList.get(position).getProfessor());

        // 관리자 계정이면 강의 수정, 삭제 버튼을 볼 수 있도록 하고 클릭 이벤트 생성
        if(isStudent==0){
            edit_course_btn.setVisibility(View.VISIBLE);
            delete_course_btn.setVisibility(View.VISIBLE);

            edit_course_btn.setTag(position);
            edit_course_btn.setOnClickListener(editBtnClickListener);

            delete_course_btn.setTag(position);
            delete_course_btn.setOnClickListener(deleteBtnClickListener);

            // 버튼 클릭 이벤트 하려면 나머지 부분의 클릭은 동작하지 않으므로 따로 레이아웃 클릭 이벤트를 만들어줌.
            courseInfoLayout = (LinearLayout) view.findViewById(R.id.courseInfoLayout);
            courseInfoLayout.setTag(position);
            courseInfoLayout.setOnClickListener(courseInfoLayoutClickListener);
        }

        return view;
    }


    // 강의 수정 버튼을 눌렀을 때 강의 번호와 함께 EditCourseActivity로 이동함.
    Button.OnClickListener editBtnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = Integer.parseInt(view.getTag().toString());
            Intent intent = new Intent(context, EditCourseActivity.class);
            intent.putExtra("course_no", courseList.get(position).getCourseNo());
            context.startActivity(intent);
        }
    };

    // 강의 삭제 버튼이 눌렸을 때 alert창이 뜸.
    Button.OnClickListener deleteBtnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            final int position = Integer.parseInt(view.getTag().toString());

            // 강의를 삭제할 것인지 alert창을 보여줌.
            AlertDialog.Builder alert = new AlertDialog.Builder(context);

            alert.setTitle("강의 삭제");
            alert.setMessage(courseList.get(position).getCourseName()+"를 삭제하시겠습니까?");

            // 삭제 버튼을 누르면 deleteCourse 함수를 실행함.
            alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteCourse(courseList.get(position).getCourseNo());
                }
            });


            // 취소 버튼을 눌렀을 때
            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    };

    // alert창에서 삭제버튼을 눌렀을 때 실행되는 함수
    private void deleteCourse(String course_no) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Result>> call = api.deleteCourse(course_no);


        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                // 강의가 삭제되었는지 확인하는 result를 받아옴.
                int result = response.body().get(0).getResult();

                if(result==0) { // 삭제되지 않은 경우
                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                } else if (result==1) { // 강의가 삭제된 경우
                    Toast.makeText(context, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, CourseListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 관리자 계정일 때 전체 강의를 누르면 강의상세페이지로 넘어가게 함.
    Button.OnClickListener courseInfoLayoutClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = Integer.parseInt(view.getTag().toString());
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("course_no", courseList.get(position).getCourseNo());
            context.startActivity(intent);
        }
    };
}
