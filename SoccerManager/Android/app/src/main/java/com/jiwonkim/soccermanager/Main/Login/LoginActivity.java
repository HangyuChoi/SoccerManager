package com.jiwonkim.soccermanager.Main.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.MainActivity;
import com.jiwonkim.soccermanager.Main.Regist.RegistActivity;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText userId, userPw;
    TextView regist;
    Button loginBtn;
    NetworkService service;

    //TODO 어플 완료 시 스플래시로 이동할 것!
    public static UserData loginUserData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        startActivity(new Intent(this, SplashActivity.class));    // 스플래시
        service = ApplicationController.getInstance().getNetworkService();

        regist = (TextView)findViewById(R.id.regist);
        loginBtn = (Button)findViewById(R.id.login);
        userId = (EditText)findViewById(R.id.id);
        userPw = (EditText)findViewById(R.id.password);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registIntent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(registIntent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 포스트로 넘길 데이터 객체
                LoginData loginData = new LoginData();
                loginData.id = userId.getText().toString();
                loginData.password = userPw.getText().toString();

                Call<LoginResult> requestLogin = service.getLoginResult(loginData);
                requestLogin.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.isSuccessful()){
                            if(response.body().status.equals("success")){
                                Toast.makeText(LoginActivity.this, "로그인 성공 : " +response.body().status, Toast.LENGTH_SHORT).show();
                                loginUserData.id = response.body().resultData.id;
                                loginUserData.password = response.body().resultData.password;
                                loginUserData.name = response.body().resultData.name;
                                loginUserData.birth = response.body().resultData.birth;
                                loginUserData.location = response.body().resultData.location;
                                loginUserData.preferredPosition = response.body().resultData.preferredPosition;

                                loginUserData.myTeamName = response.body().resultData.myTeamName;
                                loginUserData.mySpeed = response.body().resultData.mySpeed;
                                if(loginUserData.mySpeed == null){
                                    loginUserData.mySpeed = "0";
                                }
                                loginUserData.acceleration = response.body().resultData.acceleration;
                                if(loginUserData.acceleration == null){
                                    loginUserData.acceleration = "0";
                                }
                                loginUserData.health = response.body().resultData.health;
                                if(loginUserData.health == null){
                                    loginUserData.health = "0";
                                }
                                loginUserData.agility = response.body().resultData.agility;
                                if(loginUserData.agility == null){
                                    loginUserData.agility = "0";
                                }
                                loginUserData.captain = response.body().resultData.captain;

                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 실패 : " +response.body().reason, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "서버와의 통신 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.d("fail", t.getMessage());
                    }
                });
//                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//                finish();
            }
        });
    }
}
