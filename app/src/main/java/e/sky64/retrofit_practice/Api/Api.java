package e.sky64.retrofit_practice.Api;

import java.util.List;

import e.sky64.retrofit_practice.DataPackage.AssignmentList;
import e.sky64.retrofit_practice.DataPackage.Board;
import e.sky64.retrofit_practice.DataPackage.Course;
import e.sky64.retrofit_practice.DataPackage.Courses;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.DataPackage.Users;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sky64 on 2018-02-18.
 */
// Api 정의
public interface Api {
    // 접속 ip 및 URL 지정
    //이성희
    //String BASE_URL = "http://10.0.2.2:8080/";

    //문현주
//    String BASE_URL = "http://192.168.123.108/";
    //이승연
    String BASE_URL = "http://10.0.2.2/~seungyeonlee/";

    //과제
    //과제 리스트 읽어오기
    @FormUrlEncoded
    @POST("get_assignment_list.php")
    Call<List<AssignmentList>> getAssignments(@Field("course_no") int course_no);

    //과제 상세 정보
    @FormUrlEncoded
    @POST("get_assignment_info.php")
    Call<List<AssignmentList>> getAssignmentInfo(@Field("hw_no") int hw_no);

    //-----관리자--------
    //1. 과제등록
    @FormUrlEncoded
    @POST("add_assignment.php")
    Call<List<Result>> addAssignment(@Field("course_no") int course_no,
                                     @Field("hw_name") String hw_name,
                                     @Field("hw_content") String hw_content,
                                     @Field("hw_due") String hw_due);
    //2. 과제 수정시 과제 데이터 가져오기
    @FormUrlEncoded
    @POST("edit_read_assignment.php")
    Call<List<AssignmentList>> readAssignment(@Field("hw_no") int hw_no);

    //3. 관리자 과제 수정 업데이트
    @FormUrlEncoded
    @POST("edit_assignment.php")
    Call<List<AssignmentList>> editAssignment(
            @Field("hw_no") String hw_no,
            @Field("hw_name") String hw_name,
            @Field("hw_content") String hw_content,
            @Field("hw_due") String hw_due
    );

    //4. 관리자 과제 삭제
    @FormUrlEncoded
    @POST("delete_assignment.php")
    Call<List<Result>> deleteAssignment(
            @Field("hw_no") String hw_no
    );

    // 강의 리스트
    @FormUrlEncoded
    @POST("uxmlab_course_list.php")
    Call<Courses> getCourse(@Field("id") int id);

    // Login 기능을 위한 Api form
    @FormUrlEncoded
    @POST("uxmlab_login.php")
    Call<List<Users>> getResultLogin(@Field("id") int id, @Field("password") String password);

    // 강의 추가
    @FormUrlEncoded
    @POST("uxmlab_course_add.php")
    Call<List<Result>> addCourse(@Field("key") String key, @Field("no") String no, @Field("name") String name, @Field("professor") String professor, @Field("date") String date, @Field("description") String description);

    // 강의 삭제
    @FormUrlEncoded
    @POST("uxmlab_course_delete.php")
    Call<List<Result>> deleteCourse(@Field("course_no") String course_no);

    // 강의 수정할 때 읽어오는 강의 정보
    @FormUrlEncoded
    @POST("uxmlab_course_read.php")
    Call<List<Course>> readCourse(@Field("course_no") String course_no);

    // 강의 수정
    @FormUrlEncoded
    @POST("uxmlab_course_update.php")
    Call<List<Result>> editCourse(@Field("origin_course_no") String origin_course_no, @Field("key") String key, @Field("no") String no, @Field("name") String name, @Field("professor") String professor, @Field("date") String date, @Field("description") String description);

    // 강의 등록
    @FormUrlEncoded
    @POST("uxmlab_course_register.php")
    Call<List<Result>> registerCourse(@Field("course_key") String course_key, @Field("course_no") String course_no, @Field("id") int id);


    //공지사항 쓰기
    @FormUrlEncoded
    @POST("uxmlab_write_board.php")
    Call<List<Board>> getResultBoard(@Field("board_title") String board_title, @Field("board_content") String board_content, @Field("author") String author,
                                     @Field("course_no") String course_no
    );

    //공지사항 읽기
    @FormUrlEncoded
    @POST("uxmlab_read_board.php")
    Call<List<Board>> getBoard(@Field("course_no") int course_no);


}
