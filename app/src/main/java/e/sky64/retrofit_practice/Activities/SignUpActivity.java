package e.sky64.retrofit_practice.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import e.sky64.retrofit_practice.Api.Api;
import e.sky64.retrofit_practice.Api.ApiUrl;
import e.sky64.retrofit_practice.Models.Result;
import e.sky64.retrofit_practice.Models.User;
import e.sky64.retrofit_practice.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 630su on 2018-02-26.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // Intialize UI
    private Button buttonSignUp;
    private EditText editTextId, editTextPassword, editTextName, editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(this);
    }

    private void userSignUp() {
        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        // Initialize data from editTexts
        int id = Integer.parseInt(editTextId.getText().toString());
        String password = editTextPassword.getText().toString();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        // building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Defining retrofit api service
        Api api = retrofit.create(Api.class);

        // editText를 통해 받은 user 정보를 가지고 있는 객체 생성
        User user = new User(id, password, name, email);

        // 위에서 만든 User 객체의 정보가 담긴 request를 날려서 php 데이터를 response 받음
        Call<Result> call = api.createUser(
                user.getId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );

        // response 받는 부분
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //hiding progress dialog
                progressDialog.dismiss();

                //displaying the message from the response as toast
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                //if there is no error
                if (!response.body().getError()) {
                    //starting profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignUp) {
            userSignUp();
        }
    }

}