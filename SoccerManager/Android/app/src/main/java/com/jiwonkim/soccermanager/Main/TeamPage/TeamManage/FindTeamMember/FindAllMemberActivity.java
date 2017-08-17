package com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamMember;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.Login.UserData;
import com.jiwonkim.soccermanager.Main.Search.SearchAdapter;
import com.jiwonkim.soccermanager.Main.Search.SearchListData;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;

public class FindAllMemberActivity extends AppCompatActivity {
    TextView teamMemberText;
    RecyclerView recyclerViewMemberList;
    LinearLayoutManager layoutManager;
    ArrayList<UserData> memberItemDatas;
    ArrayList<SearchListData> searchListDatas;

    SearchAdapter adapter;

    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_all_member);
        service = ApplicationController.getInstance().getNetworkService();

        teamMemberText = (TextView)findViewById(R.id.find_member_team_name);
        teamMemberText.setText(loginUserData.myTeamName);
        recyclerViewMemberList = (RecyclerView)findViewById(R.id.recycler_find_all_member);
        recyclerViewMemberList.setHasFixedSize(true);
        recyclerViewMemberList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMemberList.setLayoutManager(layoutManager);

        memberItemDatas = new ArrayList<UserData>();
        searchListDatas = new ArrayList<SearchListData>();

        adapter = new SearchAdapter(searchListDatas, clickListener);
        recyclerViewMemberList.setAdapter(adapter);

        Call<FindTeamMemberResult> requestTeamMemberList = service.getMyTeamMemberResult(loginUserData.myTeamName);
        requestTeamMemberList.enqueue(new Callback<FindTeamMemberResult>() {
            @Override
            public void onResponse(Call<FindTeamMemberResult> call, Response<FindTeamMemberResult> response) {
                if(response.isSuccessful()){
                    if(response.body().status.equals("success")){
                        for(int i=0; i<response.body().resultData.size(); i++){
                            memberItemDatas.add(response.body().resultData.get(i));
                            searchListDatas.add(new SearchListData(R.drawable.man, memberItemDatas.get(i).id, memberItemDatas.get(i).name, memberItemDatas.get(i).location));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(FindAllMemberActivity.this, response.body().reason, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FindTeamMemberResult> call, Throwable t) {
                Toast.makeText(FindAllMemberActivity.this, "서버의 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = recyclerViewMemberList.getChildPosition(v);
//            Toast.makeText(FindAllMemberActivity.this, (position+1)+"번째 유저 item", Toast.LENGTH_SHORT).show();

            LayoutInflater dialog = LayoutInflater.from(getApplicationContext());
            final View dialogLayout = dialog.inflate(R.layout.custom_dialog, null);
            final Dialog myDialog = new Dialog(FindAllMemberActivity.this);

            // 다이얼로그 타이틀제거, 투명
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            myDialog.setContentView(dialogLayout);

            final TextView dialogId = (TextView)dialogLayout.findViewById(R.id.dialog_id);
            final TextView dialogName = (TextView)dialogLayout.findViewById(R.id.dialog_name);
            final TextView dialogBirth = (TextView)dialogLayout.findViewById(R.id.dialog_birth);
            final TextView dialogCountry = (TextView)dialogLayout.findViewById(R.id.dialog_country);
            final TextView dialogPosition = (TextView)dialogLayout.findViewById(R.id.dialog_position);
            final TextView dialogTeam = (TextView)dialogLayout.findViewById(R.id.dialog_team);
            final TextView dialogSpeed = (TextView)dialogLayout.findViewById(R.id.dialog_speed);
            final TextView dialogAcc = (TextView)dialogLayout.findViewById(R.id.dialog_acc);
            final TextView dialogHealth = (TextView)dialogLayout.findViewById(R.id.dialog_health);
            final TextView dialogAgil = (TextView)dialogLayout.findViewById(R.id.dialog_agil);
            final TextView dialogAver = (TextView)dialogLayout.findViewById(R.id.dialog_aver);

            dialogId.setText(memberItemDatas.get(position).id);
            dialogName.setText(memberItemDatas.get(position).name);
            dialogBirth.setText(memberItemDatas.get(position).birth);
            dialogCountry.setText(memberItemDatas.get(position).location);
            dialogPosition.setText(memberItemDatas.get(position).preferredPosition);
            dialogTeam.setText(memberItemDatas.get(position).myTeamName);
            dialogSpeed.setText(memberItemDatas.get(position).mySpeed);
            dialogAcc.setText(memberItemDatas.get(position).acceleration);
            dialogHealth.setText(memberItemDatas.get(position).health);
            dialogAgil.setText(memberItemDatas.get(position).agility);
            if(memberItemDatas.get(position).mySpeed !=null) {
                float total = (float) (Integer.parseInt(dialogSpeed.getText().toString()) + Integer.parseInt(dialogAcc.getText().toString()) + Integer.parseInt(dialogHealth.getText().toString()) + Integer.parseInt(dialogAgil.getText().toString())) / 4;
                dialogAver.setText(String.valueOf(total));
            } else{
                dialogAver.setText("0");
            }

            // 다이얼로그 크기 조정
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            Window window = myDialog.getWindow();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);

            myDialog.show();
        }
    };
}
