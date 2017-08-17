package com.jiwonkim.soccermanager.Main.Regist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistActivity extends AppCompatActivity {
    EditText editId, editPw, editName, editBirth;
    Spinner country1, country2;
    RadioGroup positions;
    CheckBox fw, mf, cf, gk;
    Button signBtn;

    String[] country_list1;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        service = ApplicationController.getInstance().getNetworkService();

        editId = (EditText)findViewById(R.id.regist_id);
        editPw = (EditText)findViewById(R.id.regist_password);
        editName = (EditText)findViewById(R.id.regist_name);
        editBirth = (EditText)findViewById(R.id.regist_birth);

        country1 = (Spinner)findViewById(R.id.regist_country1);
        country2 = (Spinner)findViewById(R.id.regist_country2);
        positions = (RadioGroup)findViewById(R.id.regist_position);
        fw = (CheckBox)findViewById(R.id.regist_fw);
        mf = (CheckBox)findViewById(R.id.regist_mf);
        cf = (CheckBox)findViewById(R.id.regist_cf);
        gk = (CheckBox)findViewById(R.id.regist_gk);

        signBtn = (Button)findViewById(R.id.signBtn);

        country_list1 = getResources().getStringArray(R.array.country_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,country_list1);
        country1.setAdapter(adapter);
        country1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] country_list2;
                ArrayAdapter<String> adapter2;
                switch (position){
                    case 0:
                        String[] selectCon = {"지역을 선택해주세요"};
                        country2.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,selectCon));
                        break;
                    case 1:
                        country_list2 = getResources().getStringArray(R.array.seoul);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
//                        country2.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2));
                        break;
                    case 2:
                        country_list2 = getResources().getStringArray(R.array.busan);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 3:
                        country_list2 = getResources().getStringArray(R.array.daegu);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 4:
                        country_list2 = getResources().getStringArray(R.array.inchen);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 5:
                        country_list2 = getResources().getStringArray(R.array.gwangju);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 6:
                        country_list2 = getResources().getStringArray(R.array.daejeon);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 7:
                        country_list2 = getResources().getStringArray(R.array.ulsan);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 8:
                        country_list2 = getResources().getStringArray(R.array.gangwon);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 9:
                        country_list2 = getResources().getStringArray(R.array.gyeonggi);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 10:
                        country_list2 = getResources().getStringArray(R.array.gyeongnam);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 11:
                        country_list2 = getResources().getStringArray(R.array.gyeongbuk);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 12:
                        country_list2 = getResources().getStringArray(R.array.gyeongnam);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 13:
                        country_list2 = getResources().getStringArray(R.array.gyeongbuk);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 14:
                        country_list2 = getResources().getStringArray(R.array.jeju);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 15:
                        country_list2 = getResources().getStringArray(R.array.chungnam);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;
                    case 16:
                        country_list2 = getResources().getStringArray(R.array.chungbuk);
                        adapter2 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,country_list2);
                        country2.setAdapter(adapter2);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String[] selectCon = {"지역을 선택해주세요"};
                country2.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,selectCon));
            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    MemberData memberData = new MemberData();
                    memberData.id = editId.getText().toString();
                    memberData.password = editPw.getText().toString();
                    memberData.name = editName.getText().toString();
                    memberData.birth = editBirth.getText().toString();
                    memberData.location = country1.getSelectedItem().toString() +" " +country2.getSelectedItem().toString();
                    String position = "";
                    if(fw.isChecked()){
                        position += "공격수";
                    }
                    if(mf.isChecked()){
                        if(fw.isChecked()){
                            position += ",";
                        }
                        position += "미드필더";
                    }
                    if(cf.isChecked()){
                        if(fw.isChecked() || mf.isChecked()){
                            position += ",";
                        }
                        position += "수비수";
                    }
                    if(gk.isChecked()){
                        if(fw.isChecked() || mf.isChecked() || cf.isChecked()){
                            position += ",";
                        }
                        position += "골키퍼";
                    }
                    memberData.preferredPosition = position.trim();
//                    Toast.makeText(RegistActivity.this, position, Toast.LENGTH_SHORT).show();
                    Call<RegistResult> requestRegist = service.getRegistResult(memberData);
                    requestRegist.enqueue(new Callback<RegistResult>() {
                        @Override
                        public void onResponse(Call<RegistResult> call, Response<RegistResult> response) {
                            if(response.isSuccessful()){
                                if(response.body().status.equals("success")){
                                    Toast.makeText(RegistActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                Toast.makeText(RegistActivity.this, "통신 실패 : " +response.body().status, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistResult> call, Throwable t) {
                            Toast.makeText(RegistActivity.this, "서버와의 통신 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("fail", t.getMessage());
                        }
                    });

//                    Toast.makeText(RegistActivity.this, "id : " +id +"\npw : " +pw +"\nname : " +name +"\nbirth : " +birth +"\ncountry : " +country +"\nposition : " +position, Toast.LENGTH_SHORT).show();
//                    finish();
                } catch (Exception e){
                    Toast.makeText(RegistActivity.this, "입력정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
