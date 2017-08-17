package com.jiwonkim.soccermanager.Main.TeamPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jiwonkim.soccermanager.Main.TeamPage.Formation.FormationActivity;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.CreateTeam.TeamCreateActivity;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.FindTeamDataActivity;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamMember.FindAllMemberActivity;
import com.jiwonkim.soccermanager.R;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;

/**
 * Created by user on 2017-06-08.
 */

public class TeamPageFragment extends Fragment implements View.OnClickListener{
    Button createTeamOrTeamInfo, formationChange, teamMemberShow;
    Context context;

    public TeamPageFragment() {
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_teampage,container,false);

        createTeamOrTeamInfo = (Button)layout.findViewById(R.id.createTeamOrTeamInfo);
        formationChange = (Button)layout.findViewById(R.id.formationChange);
        teamMemberShow = (Button)layout.findViewById(R.id.teamMemberShow);

        if(loginUserData.myTeamName == null){
            createTeamOrTeamInfo.setText("구단 생성");
            createTeamOrTeamInfo.setTag(0);
            formationChange.setVisibility(View.INVISIBLE);
            teamMemberShow.setVisibility(View.INVISIBLE);
        }else {
            createTeamOrTeamInfo.setText("구단 정보");
            createTeamOrTeamInfo.setTag(1);
            formationChange.setVisibility(View.VISIBLE);
            teamMemberShow.setVisibility(View.VISIBLE);
        }

        createTeamOrTeamInfo.setOnClickListener(this);
        formationChange.setOnClickListener(this);
        teamMemberShow.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createTeamOrTeamInfo:
                    if((int)createTeamOrTeamInfo.getTag() == 0){
                        startActivity(new Intent(context, TeamCreateActivity.class));
                    } else if((int)createTeamOrTeamInfo.getTag() == 1){
                        startActivity(new Intent(context, FindTeamDataActivity.class));
                    }
                break;

            case R.id.formationChange:
                startActivity(new Intent(context, FormationActivity.class));
                break;

            case R.id.teamMemberShow:
                startActivity(new Intent(context, FindAllMemberActivity.class));
                break;
        }

    }
}
