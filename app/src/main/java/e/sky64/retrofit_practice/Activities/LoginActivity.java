package e.sky64.retrofit_practice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.DataPackage.Courses;
import e.sky64.retrofit_practice.DataPackage.LoginRequest;
import e.sky64.retrofit_practice.DataPackage.Users;
import e.sky64.retrofit_practice.GlobalUserApplication;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sky64 on 2018-02-18.
 */

public class LoginActivity extends AppCompatActivity {
    // Intialize UI
    Button BtnSignIn;
    Button BtnSignUp;
    EditText InputId;
    EditText InputPW;

    // Initialize Retrofit & Api
    Retrofit retrofit;
    Api api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BtnSignIn = findViewById(R.id.btn_signin);
        BtnSignUp = findViewById(R.id.btn_signup);
        InputId = findViewById(R.id.user_id);
        InputPW = findViewById(R.id.user_pw);
    }


    public void checkLogin() {


    }
    // Login 기능을 수행한다.
    public void clickSignIn(View v) {
        // Initialize data for Input at Edittext
        final int id = Integer.parseInt(InputId.getText().toString());
        String pw = InputPW.getText().toString();

        // Gson object를 만들어서 Json을 읽어와서 Gson으로 해석할 수 있도록 함
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // Retrofit object 생성 및 api를 통한 클래스 객체 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(Api.class);

        // ID와 PW를 post 방식을 이용하여 php로 데이터를 request
        Call<List<Users>> call = api.getResultLogin(id, pw);

        // response 받는 부분
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                // Response의 결과를 loginResult에 저장
                List<Users> loginResult = response.body();
                int isStudent = loginResult.get(0).getIs_student();

                // php에서 ID, PW를 Input 받게 되었을 때 is_student가 response로 돌아옴
                // 이 때 json 형식을 맞춰주기 위해서 isStudent 2, 3을 의도적으로 response하게 php를 설정해놓았음
                if (isStudent == 2) {
                    Toast.makeText(getApplicationContext(), "아이디가 틀리거나 없습니다", Toast.LENGTH_LONG).show();
                } else if (isStudent == 3) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);

                    // test를 위해 추가한 전역변수 설정
                    GlobalUserApplication userApplication = (GlobalUserApplication) getApplication();
                    userApplication.setId(id);
                    userApplication.setIsStudent(loginResult.get(0).getIs_student());

                    startActivity(intent);
                }
            }

            // 에러처리
            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
