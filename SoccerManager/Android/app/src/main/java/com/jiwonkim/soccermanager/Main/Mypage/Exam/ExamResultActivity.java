package com.jiwonkim.soccermanager.Main.Mypage.Exam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.Mypage.ModifyResult;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;

public class ExamResultActivity extends AppCompatActivity implements Runnable,View.OnTouchListener {
    TextView resultText, textTotal;
    RatingBar lating_speed, lating_acc, lating_heal, lating_agil, lating_total;
    int speed, acc, heal, agil;
    LinearLayout linearLayout;
    float totalCount;
    ProgressBar progress;
    Handler process;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);

        service = ApplicationController.getInstance().getNetworkService();

        resultText = (TextView)findViewById(R.id.resultText1);
        textTotal = (TextView)findViewById(R.id.text_total);
        lating_speed = (RatingBar)findViewById(R.id.lating_speed);
        lating_acc = (RatingBar)findViewById(R.id.lating_acc);
        lating_heal = (RatingBar)findViewById(R.id.lating_heal);
        lating_agil = (RatingBar)findViewById(R.id.lating_agil);
        lating_total = (RatingBar)findViewById(R.id.lating_total);
        linearLayout = (LinearLayout)findViewById(R.id.result_layout);
        progress = (ProgressBar)findViewById(R.id.progress);

        speed = getIntent().getExtras().getInt("speed");
        acc = getIntent().getExtras().getInt("acceleration");
        heal = getIntent().getExtras().getInt("health");
        agil = getIntent().getExtras().getInt("agility");
        totalCount = (float)(speed+acc+heal+agil)/4;

        loginUserData.mySpeed = String.valueOf(speed);
        loginUserData.acceleration = String.valueOf(acc);
        loginUserData.health = String.valueOf(heal);
        loginUserData.agility = String.valueOf(agil);

        Call<ModifyResult> requestModify = service.getModifyResult(loginUserData.id, loginUserData);
        requestModify.enqueue(new Callback<ModifyResult>() {
            @Override
            public void onResponse(Call<ModifyResult> call, Response<ModifyResult> response) {
                if(response.isSuccessful()){
                    if(response.body().status.equals("success")){
                        Toast.makeText(ExamResultActivity.this, "측정결과가 저장되었습니다.", Toast.LENGTH_SHORT).show();
//                        Fragment frg = null;
                    } else{
                        Toast.makeText(ExamResultActivity.this, "저장 실패 " +response.body().reason, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModifyResult> call, Throwable t) {
                Toast.makeText(ExamResultActivity.this, "서버와 통신상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        process = new Handler();
        new Thread(ExamResultActivity.this).start();

        lating_speed.setMax(5);
        lating_acc.setMax(5);
        lating_heal.setMax(5);
        lating_agil.setMax(5);
        lating_total.setMax(5);
        lating_total.setStepSize((float)0.25);

        lating_speed.setRating((float)speed);
        lating_acc.setRating((float)acc);
        lating_heal.setRating((float)heal);
        lating_agil.setRating((float)agil);
        lating_total.setRating(totalCount);
        textTotal.setText(""+totalCount);

        lating_speed.setOnTouchListener(this);
        lating_heal.setOnTouchListener(this);
        lating_acc.setOnTouchListener(this);
        lating_agil.setOnTouchListener(this);
        lating_total.setOnTouchListener(this);
        Log.d("총 점수", ""+totalCount);
    }

    @Override
    public void run() {
        try{
            Thread.sleep(3000);
            process.post(changeView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Runnable changeView = new Runnable() {
        @Override
        public void run() {
            try {
                progress.setVisibility(View.INVISIBLE);
                resultText.setText("사용자 님의 측정 결과 입니다.");
                linearLayout.setVisibility(View.VISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
