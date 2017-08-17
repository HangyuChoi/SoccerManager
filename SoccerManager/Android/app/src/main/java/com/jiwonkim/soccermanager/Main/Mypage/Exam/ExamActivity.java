package com.jiwonkim.soccermanager.Main.Mypage.Exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiwonkim.soccermanager.R;

public class ExamActivity extends AppCompatActivity implements View.OnClickListener{
    Button nextBtn;
    EditText editExam1, editExam2, editExam3, editExam4;
    int speed, health, agility, acceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        nextBtn = (Button) findViewById(R.id.nextBtn);
        editExam1 = (EditText) findViewById(R.id.editExam1);
        editExam2 = (EditText) findViewById(R.id.editExam2);
        editExam3 = (EditText) findViewById(R.id.editExam3);
        editExam4 = (EditText) findViewById(R.id.editExam4);

        nextBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try{
            float examValue1 = Float.parseFloat(editExam1.getText().toString());
            float examValue2 = Float.parseFloat(editExam2.getText().toString());
            int examValue3 = Integer.parseInt(editExam3.getText().toString());
            int examValue4 = Integer.parseInt(editExam4.getText().toString());
            Log.d("결과 값", ""+examValue1 +", " +examValue2 +", " + examValue3 +", " +examValue4);
            if(examValue1<11.5){        // 속도 측정
                speed = 5;
            } else if(examValue1<12.2){
                speed = 4;
            } else if(examValue1<12.9){
                speed = 3;
            } else if(examValue1<14.4){
                speed = 2;
            } else {
                speed = 1;
            }

            if(examValue2<12.30){       // 체력 측정
                health = 5;
            } else if(examValue2<13.32){
                health = 4;
            } else if(examValue2<14.34){
                health = 3;
            } else if(examValue2<15.36){
                health = 2;
            } else {
                health = 1;
            }

            if(examValue3<36){      // 민첩성 측정
                agility = 1;
            }else if(examValue3<39){
                agility = 2;
            }else if(examValue3<42){
                agility = 3;
            }else if(examValue3<45){
                agility = 4;
            } else {
                agility = 5;
            }

            if(examValue4<250){     // 가속도 측정
                acceleration = 1;
            }else if(examValue4<266){
                acceleration = 2;
            }else if(examValue4<281){
                acceleration = 3;
            }else if(examValue4<300){
                acceleration = 4;
            }else {
                acceleration = 5;
            }

            Intent intent = new Intent(ExamActivity.this, ExamResultActivity.class);
            intent.putExtra("speed",speed);
            intent.putExtra("health",health);
            intent.putExtra("agility",agility);
            intent.putExtra("acceleration",acceleration);
            startActivity(intent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "값을 확인해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}
