package com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.MainActivity;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.MemberQuit.MemberQuitData;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.MemberQuit.MemberQuitResult;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;
import static com.jiwonkim.soccermanager.Main.MainActivity.activityList;

public class FindTeamDataActivity extends AppCompatActivity implements View.OnClickListener{
    TextView myTeamName, myTeamCaptain, myTeamCount, myTeamLocation;
    Button withdrawalBtn, teamEditBtn;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_team_data);
        service = ApplicationController.getInstance().getNetworkService();

        myTeamName = (TextView)findViewById(R.id.myTeam_name);
        myTeamCaptain = (TextView)findViewById(R.id.myTeam_captain);
        myTeamCount = (TextView)findViewById(R.id.myTeam_count);
        myTeamLocation = (TextView)findViewById(R.id.myTeam_location);
        withdrawalBtn = (Button)findViewById(R.id.myTeam_withdrawal);
        teamEditBtn = (Button)findViewById(R.id.myTeam_edit);
        withdrawalBtn.setOnClickListener(this);
        teamEditBtn.setOnClickListener(this);

        Call<MyTeamFindResult> requestMyTeamData = service.getMyTeamResult(loginUserData.id);
        requestMyTeamData.enqueue(new Callback<MyTeamFindResult>() {
            @Override
            public void onResponse(Call<MyTeamFindResult> call, Response<MyTeamFindResult> response) {
                if(response.isSuccessful()){
                    if(response.body().status.equals("success")){
                        myTeamName.setText(response.body().resultData.name);
                        myTeamCaptain.setText(response.body().resultData.captain);
                        myTeamCount.setText(response.body().resultData.count);
                        myTeamLocation.setText(response.body().resultData.location);
                    } else {
                        Toast.makeText(FindTeamDataActivity.this, response.body().reason, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyTeamFindResult> call, Throwable t) {
                Toast.makeText(FindTeamDataActivity.this, "서버 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myTeam_withdrawal:        // 탈퇴
                if(loginUserData.captain.equals("true")){
                    Toast.makeText(FindTeamDataActivity.this, "먼저 주장을 양도하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    MemberQuitData memberQuitData = new MemberQuitData();
                    memberQuitData.id = loginUserData.id;
                    memberQuitData.name = loginUserData.myTeamName;
                    memberQuitData.captain = loginUserData.captain;

                    Call<MemberQuitResult> requestMemberQuitResult = service.getMemberQuitResult(memberQuitData);
                    requestMemberQuitResult.enqueue(new Callback<MemberQuitResult>() {
                        @Override
                        public void onResponse(Call<MemberQuitResult> call, Response<MemberQuitResult> response) {
                            if(response.isSuccessful()){
                                if(response.body().status.equals("success")){
                                    activityList.get(0).finish();
                                    activityList.remove(0);
                                    loginUserData.myTeamName = null;
                                    finish();
                                    startActivity(new Intent(FindTeamDataActivity.this, MainActivity.class));
                                    Toast.makeText(FindTeamDataActivity.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FindTeamDataActivity.this, response.body().reason, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MemberQuitResult> call, Throwable t) {
                            Toast.makeText(FindTeamDataActivity.this, "서버 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;

            case R.id.myTeam_edit:              // 정보수정
                if(loginUserData.captain.equals("true")){       // 주장일때

                } else {
                    Toast.makeText(FindTeamDataActivity.this, "주장만 이용할 수 있습니다..", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
