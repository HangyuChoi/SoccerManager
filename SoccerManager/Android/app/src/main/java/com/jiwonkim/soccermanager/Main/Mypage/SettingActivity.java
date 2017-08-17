package com.jiwonkim.soccermanager.Main.Mypage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.Login.LoginActivity;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;
import static com.jiwonkim.soccermanager.Main.MainActivity.activityList;
import static com.jiwonkim.soccermanager.Main.MainActivity.mp;

public class SettingActivity extends AppCompatActivity{
    Button soundOnOff, withdrawal;
    AlertDialog.Builder dialog;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(activityList.size()<2){
            activityList.add(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        service = ApplicationController.getInstance().getNetworkService();

        soundOnOff = (Button)findViewById(R.id.musicOnOff);
        withdrawal = (Button)findViewById(R.id.withDrawal);

        soundOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    Toast.makeText(SettingActivity.this, "음악 off.", Toast.LENGTH_SHORT).show();
                    mp.pause();
                } else {
                    Toast.makeText(SettingActivity.this, "음악 on.", Toast.LENGTH_SHORT).show();
                    mp.start();
                }
            }
        });

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(SettingActivity.this);
                dialog.setTitle("정말 탈퇴하시겠습니까?");
                dialog.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Call<WithdrawalResult> requestWithdrawal = service.getWithdrawalResult(loginUserData.id);
                        requestWithdrawal.enqueue(new Callback<WithdrawalResult>() {
                            @Override
                            public void onResponse(Call<WithdrawalResult> call, Response<WithdrawalResult> response) {
                                if(response.isSuccessful()){
                                    if(response.body().status.equals("success")){
                                        Toast.makeText(SettingActivity.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                        for(int i=0; i<activityList.size();i++){
                                            activityList.get(i).finish();
                                        }
                                        activityList.removeAll(activityList);
                                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
//                                        finish();
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(SettingActivity.this, response.body().reason, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<WithdrawalResult> call, Throwable t) {

                            }
                        });
//                        Toast.makeText(SettingActivity.this, "탈퇴했다 치자.", Toast.LENGTH_SHORT).show();
//                        finish();
//                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }
}
