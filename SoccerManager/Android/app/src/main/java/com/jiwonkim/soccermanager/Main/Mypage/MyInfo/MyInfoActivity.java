package com.jiwonkim.soccermanager.Main.Mypage.MyInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jiwonkim.soccermanager.R;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;

public class MyInfoActivity extends AppCompatActivity {
    TextView id, name, birth, country, position, team, speed, acc, health, agil, aver;
    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        id = (TextView)findViewById(R.id.myInfo_id);
        name = (TextView)findViewById(R.id.myInfo_name);
        birth = (TextView)findViewById(R.id.myInfo_birth);
        country = (TextView)findViewById(R.id.myInfo_country);
        position = (TextView)findViewById(R.id.myInfo_position);
        team = (TextView)findViewById(R.id.myInfo_team);
        speed = (TextView)findViewById(R.id.myInfo_speed);
        acc = (TextView)findViewById(R.id.myInfo_acc);
        health = (TextView)findViewById(R.id.myInfo_health);
        agil = (TextView)findViewById(R.id.myInfo_agil);
        aver = (TextView)findViewById(R.id.myInfo_aver);
        editBtn = (Button)findViewById(R.id.myInfo_edit);

        id.setText(loginUserData.id);
        name.setText(loginUserData.name);
        birth.setText(loginUserData.birth);
        country.setText(loginUserData.location);
        position.setText(loginUserData.preferredPosition);
        if(loginUserData.myTeamName == null){
            team.setText("(소속 팀이 없습니다.)");
        }else {
            team.setText(loginUserData.myTeamName);
        }
        speed.setText(loginUserData.mySpeed);
        acc.setText(loginUserData.acceleration);
        health.setText(loginUserData.health);
        agil.setText(loginUserData.agility);
        float total =(float)(Integer.parseInt(loginUserData.mySpeed) +Integer.parseInt(loginUserData.acceleration) +Integer.parseInt(loginUserData.health) +Integer.parseInt(loginUserData.agility))/4;
        aver.setText(""+total);
    }
}
