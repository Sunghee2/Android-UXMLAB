package e.sky64.retrofit_practice.Api;

import java.util.List;

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

    // URL 이후 접근할 파일 혹은 위치 지정 + Get, Post, Put 등 어떤 방식을 이용할 것인지를 정하고
    // 그 데이터를 어떤 방식으로 저장할 것인지 지정
    @FormUrlEncoded
    @POST("uxmlab_course_list.php")
    Call<List<Courses>> getCourse(@Field("id") int id);

    // Login 기능을 위한 Api form
    @FormUrlEncoded
    @POST("uxmlab_login.php")
    Call<List<Users>> getResultLogin(@Field("id") int id, @Field("password") String password);

    @FormUrlEncoded
    @POST("uxmlab_course_add.php")
    Call<List<Result>> addCourse(@Field("key") String key, @Field("no") String no, @Field("name") String name, @Field("professor") String professor, @Field("date") String date, @Field("description") String description);

    @FormUrlEncoded
    @POST("uxmlab_course_delete.php")
    Call<List<Result>> deleteCourse(@Field("course_no") String course_no);

    @FormUrlEncoded
    @POST("uxmlab_course_register.php")
    Call<List<Result>> registerCourse(@Field("course_key") String course_key, @Field("course_no") String course_no, @Field("id") int id);

}
