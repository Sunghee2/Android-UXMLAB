package e.sky64.retrofit_practice.Api;

import java.util.List;

import e.sky64.retrofit_practice.DataPackage.Course;
import e.sky64.retrofit_practice.DataPackage.Courses;
import e.sky64.retrofit_practice.DataPackage.LoginRequest;
import e.sky64.retrofit_practice.DataPackage.Result;
import e.sky64.retrofit_practice.DataPackage.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sky64 on 2018-02-18.
 */
// Api 정의
public interface Api {
    // 접속 ip 및 URL 지정
    String BASE_URL = "http://10.0.2.2:8080/";

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


    //과제
    //과제등록
    @FormUrlEncoded
    @POST("add_assignment.php")
    Call <List<Result>> addAssignment(
            @Field("hw_name") String hw_name, @Field("hw_content") String hw_content, @Field("hw_due") String hw_due
    );

}
