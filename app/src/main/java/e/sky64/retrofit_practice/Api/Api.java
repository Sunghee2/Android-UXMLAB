package e.sky64.retrofit_practice.Api;

import java.util.List;

import e.sky64.retrofit_practice.Models.AssignmentList;
import e.sky64.retrofit_practice.Models.Board;
import e.sky64.retrofit_practice.Models.Courses;
import e.sky64.retrofit_practice.Models.Result;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sky64 on 2018-02-18.
 */
// Api 정의
public interface Api {
    /*
        URL 이후 접근할 파일 혹은 위치 지정 + Get, Post, Put 등 어떤 방식을 이용할 것인지를 정하고
        그 데이터를 어떤 방식으로 저장할 것인지 지정

        <Retrofit url 참고사항>
        if BASE_URL = "http://192.168.22.24/UXMLAB_App_php/"
        and if @POST("public/login") then
        login == "http://192.168.22.24/UXMLAB_App_php/public/login"

        ref - https://stackoverflow.com/questions/32431525/using-call-enqueue-function-in-retrofit
    */

    //The register call
    @FormUrlEncoded
    @POST("public/register")
    Call<Result> createUser(
            @Field("id") int id,
            @Field("password") String password,
            @Field("name") String name,
            @Field("email") String email
    );


    // The signin call
    @FormUrlEncoded
    @POST("public/login")
    Call<Result> userLogin( // php->DbOperation의 함수
                            @Field("id") int id,
                            @Field("password") String password
    );

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
    Call<List<AssignmentList>> addAssignment(@Field("course_no") int course_no,
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
    Call<List<Result>> editAssignment(
            @Field("hw_no") String hw_no,
            @Field("hw_name") String hw_name,
            @Field("hw_content") String hw_content,
            @Field("hw_due") String hw_due
    );

    //4. 관리자 과제 삭제
    @FormUrlEncoded
    @POST("delete_assignment.php")
    Call<List<AssignmentList>> deleteAssignment(
            @Field("hw_no") String hw_no
    );

    // 강의 리스트
    @FormUrlEncoded
    @POST("public/course_list")
    Call<Courses> getCourse(@Field("id") int id);

    // 강의 추가
    @FormUrlEncoded
    @POST("public/add_course")
    Call<Result> addCourse(@Field("key") String key,
                           @Field("no") String no,
                           @Field("name") String name,
                           @Field("professor") String professor,
                           @Field("date") String date,
                           @Field("description") String description);

    // 강의 삭제
    @FormUrlEncoded
    @POST("public/delete_course")
    Call<Result> deleteCourse(@Field("course_no") String course_no);

    // 강의 수정할 때 읽어오는 강의 정보
    @FormUrlEncoded
    @POST("public/read_course")
    Call<Result> readCourse(@Field("course_no") String course_no);

    // 강의 수정
    @FormUrlEncoded
    @POST("public/edit_course")
    Call<Result> editCourse(@Field("origin_course_no") String origin_course_no,
                            @Field("key") String key,
                            @Field("no") String no,
                            @Field("name") String name,
                            @Field("professor") String professor,
                            @Field("date") String date,
                            @Field("description") String description);

    // 강의 등록
    @FormUrlEncoded
    @POST("public/register_course")
    Call<Result> registerCourse(@Field("course_key") String course_key,
                                @Field("course_no") String course_no,
                                @Field("id") int id);


    //공지사항 쓰기
    @FormUrlEncoded
    @POST("uxmlab_write_board.php")
    Call<List<Board>> getResultBoard(@Field("board_title") String board_title, @Field("board_content") String board_content, @Field("author") String author,
                                     @Field("course_no") String course_no
    );

    //공지사항 리스트
    @FormUrlEncoded
    @POST("uxmlab_read_board.php")
    Call<List<Board>> getBoard(@Field("course_no") int course_no);

    //공지사항 상세정보
    @FormUrlEncoded
    @POST("uxmlab_read_board_detail.php")
    Call<List<Board>> getBoardInfo(@Field("board_no") int board_no);


    //공지사항 수정
    @FormUrlEncoded
    @POST("uxmlab_board_edit.php")
    Call<List<Board>> editBoard(
            @Field("board_title") String board_title,  @Field("board_content") String board_content,   @Field("board_no") String board_no);


    //공지사항 삭제
    @FormUrlEncoded
    @POST("uxmlab_board_delete.php")
    Call<List<Board>> deleteBoard(
            @Field("board_no") String board_no);

}
