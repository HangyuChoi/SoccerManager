package com.jiwonkim.soccermanager.Main.TeamPage.Formation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamMember.FindTeamMemberResult;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;

public class FormationActivity extends AppCompatActivity implements View.OnTouchListener{
    Button squad1,squad2,squad3;
    RecyclerView recyclerPlayer;
    LinearLayoutManager linearLayoutManager;
    ArrayList<PlayerListData> itemDatas;
    FormationAdapter formationAdapter;
    float oldXvalue, oldYvalue;
    boolean createPlayer = false;
    PlayerListData player;
    LinearLayout fwArea, mfArea, cfArea, gkArea;
    int tag = 1000;
    NetworkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);
        service = ApplicationController.getInstance().getNetworkService();

        recyclerPlayer = (RecyclerView)findViewById(R.id.recycler_player_list);
        recyclerPlayer.setHasFixedSize(true);
//        recyclerPlayer.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.HORIZONTAL));

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerPlayer.setLayoutManager(linearLayoutManager);

        itemDatas = new ArrayList<PlayerListData>();
        Call<FindTeamMemberResult> requestTeamMember = service.getMyTeamMemberResult(loginUserData.myTeamName);
        requestTeamMember.enqueue(new Callback<FindTeamMemberResult>() {
            @Override
            public void onResponse(Call<FindTeamMemberResult> call, Response<FindTeamMemberResult> response) {
                if(response.isSuccessful()){
                    if(response.body().status.equals("success")){
                        for(int i=0; i< response.body().resultData.size();i++){
                            itemDatas.add(new PlayerListData(R.drawable.man,response.body().resultData.get(i).name));
                            formationAdapter = new FormationAdapter(itemDatas,touchListener);
                            recyclerPlayer.setAdapter(formationAdapter);
                        }
                    } else {
                        Toast.makeText(FormationActivity.this, response.body().reason, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FindTeamMemberResult> call, Throwable t) {
                Toast.makeText(FormationActivity.this, "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
//        itemDatas.add(new PlayerListData(R.drawable.man,"김지원"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"최한규"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"김영범"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"박형준"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"장지원"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"백인호"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"윤진성"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"김현"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"주정태"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"김광호"));
//        itemDatas.add(new PlayerListData(R.drawable.man,"염규성"));

//        formationAdapter = new FormationAdapter(itemDatas,touchListener);
//        recyclerPlayer.setAdapter(formationAdapter);
    }

    public View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i("액션 이벤트 ", "이벤트에 들어옴");
            final int widthsize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            final int heightsize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());

            final int position = recyclerPlayer.getChildPosition(v);

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.i("액션 이벤트 ", "클릭");
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if(event.getY()<0){
                    Log.i("액션 이벤트 ", "나감");
                    Log.i("아이디",""+itemDatas.get(position).name);
                    Log.i("createPlayer 상태 ", ""+createPlayer);
//                    if(createPlayer == false){
//                        createPlayer= true;
                        Log.i("액션 이벤트 ", "진입");
                        player = itemDatas.get(position);
                        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                        RelativeLayout rootView = (RelativeLayout)findViewById(R.id.ground);
                        LinearLayout targetView = new LinearLayout(FormationActivity.this);
                        targetView.setLayoutParams(new LinearLayout.LayoutParams(widthsize,heightsize));
                        targetView.setOrientation(LinearLayout.VERTICAL);
                        targetView.setTag(tag);
                        tag++;
                        ImageView imageView = new ImageView(FormationActivity.this);
                        imageView.setImageResource(R.drawable.man);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(widthsize,widthsize));
                        imageView.setTag(tag);
                        tag++;

                        TextView textView = new TextView(FormationActivity.this);
                        textView.setText(player.name.toString());
                        textView.setTextSize(13);
                        textView.setTextColor(Color.WHITE);

                        targetView.addView(imageView);
                        targetView.addView(textView);
                        rootView.addView(targetView);
                        targetView.setOnTouchListener(FormationActivity.this);
                        Log.i("액션 이벤트", "리스너 등록");

                        itemDatas.remove(position);
                        formationAdapter.notifyDataSetChanged();
                        Log.i("액션 이벤트 ", "이벤트 완료");
                        Log.i("createPlayer 상태 ", ""+createPlayer);
//                    }
                }

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i("액션 이벤트 ", "뗌");
//                createPlayer =false;
            }
            return true;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("바깥이벤트", "들어옴");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("바깥이벤트", "클릭");

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.i("바깥이벤트 ", "이동중");
            v.setX(event.getRawX()-100);
            v.setY(event.getRawY()-250);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            int itemTag = (int)v.getTag();
            ImageView item = (ImageView)v.findViewWithTag(itemTag+1);
            Log.i("바깥이벤트 ", "뗌");
            Log.i("멈춘 위치 값 ","X: " +event.getRawX() +", Y: " +event.getRawY());
            Log.i("바깥이벤트 ", "아이템의 아이디 - " +v.getTag());
            if(event.getRawY()<650){    // 공격수 포지션
                if(event.getRawX() <300){
                    item.setImageResource(R.drawable.lw);
                } else if(event.getRawX() >700){
                    item.setImageResource(R.drawable.rw);
                } else {
                    item.setImageResource(R.drawable.cf);
                }
            } else if(event.getRawY() <1300){       // 미드필더 포지션
                if(event.getRawX()<300){
                    item.setImageResource(R.drawable.lm);
                } else if(event.getRawX()>700){
                    item.setImageResource(R.drawable.rm);
                } else {
                    if(event.getRawY()<810){
                        item.setImageResource(R.drawable.cam);
                    } else if(event.getRawY() > 1050){
                        item.setImageResource(R.drawable.cmd);
                    } else {
                        item.setImageResource(R.drawable.cm);
                    }
                }
            } else if(event.getRawY() < 1600){      // 수비수 포지션
                if(event.getRawX() <300){
                    item.setImageResource(R.drawable.lb);
                } else if(event.getRawX()>700){
                    item.setImageResource(R.drawable.rb);
                } else {
                    item.setImageResource(R.drawable.cb);
                }
            } else{     // 골키퍼 포지션
                item.setImageResource(R.drawable.gk);
            }
        }
        return true;
    }

}
