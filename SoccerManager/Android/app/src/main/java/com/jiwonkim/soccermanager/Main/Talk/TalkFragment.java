package com.jiwonkim.soccermanager.Main.Talk;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Application.ApplicationController;
import com.jiwonkim.soccermanager.Main.Talk.TalkSearch.TalkSearchResult;
import com.jiwonkim.soccermanager.Main.Talk.TalkWrite.TalkDatas;
import com.jiwonkim.soccermanager.Main.Talk.TalkWrite.TalkWriteResult;
import com.jiwonkim.soccermanager.Network.NetworkService;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jiwonkim.soccermanager.Main.Login.LoginActivity.loginUserData;

/**
 * Created by user on 2017-06-08.
 */

public class TalkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerRead;
    Context context;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TalkListData> itemDatas;
    TalkAdapter talkAdapter;
    Button talkWriteBtn;
    AlertDialog.Builder dialog;
    TextView talkTitle;
    NetworkService service;
    SwipeRefreshLayout refreshLayout;

    public TalkFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_talk,container,false);
        service = ApplicationController.getInstance().getNetworkService();

        refreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        talkWriteBtn = (Button)layout.findViewById(R.id.talk_write_btn);
        talkTitle = (TextView)layout.findViewById(R.id.talk_title);
        talkTitle.setText(loginUserData.myTeamName);
        recyclerRead = (RecyclerView)layout.findViewById(R.id.recyclerTalk);
        recyclerRead.setHasFixedSize(true);
        recyclerRead.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerRead.setLayoutManager(linearLayoutManager);

        itemDatas = new ArrayList<TalkListData>();
        talkAdapter = new TalkAdapter(itemDatas);
        recyclerRead.setAdapter(talkAdapter);
        ListReload();

//        itemDatas.add(new TalkListData("안녕하세요","김지원","2017-06-15 15:13:21","테스트 글 입니다."));
//        itemDatas.add(new TalkListData("테스트1","김지원","2017-06-15 15:13:21","테스트 글1 입니다."));
//        itemDatas.add(new TalkListData("테스트2","김지원","2017-06-15 15:13:22","테스트 글2 입니다."));
//        itemDatas.add(new TalkListData("칭찬에도 화내는 프로 불편러 대처법","김지원","2017-06-15 15:13:26", "사람은 ‘다른 사람으로부터 칭찬을 받으면 기분이 좋아지는 존재’, ‘자신을 칭찬해준 상대에게 호감을 가지는 존재’ 다. 이것은 상식이다. 하지만 그 상식이 적용되지 않는 경우가 적잖다. 독자 여러분도 상대방을 칭찬할 생각으로 말했는데 ‘상대방이 오히려 화를 냈다’ 거나 ‘상대방이 오히려 기분 나쁘게 받아들였다’ 는 경험이 있을 것이다."));
//        talkAdapter = new TalkAdapter(itemDatas);
//        recyclerRead.setAdapter(talkAdapter);

        talkWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(getActivity());
                LinearLayout dialogView = new LinearLayout(context);
                dialogView.setOrientation(LinearLayout.VERTICAL);
                dialog.setTitle("게시물 작성");
                final TextView textViewTitle = new TextView(context);
                textViewTitle.setText("제목");
                final EditText editTextTitle = new EditText(context);
                dialogView.addView(textViewTitle);
                dialogView.addView(editTextTitle);
                final TextView textViewDetail = new TextView(context);
                textViewDetail.setText("내용");
                final EditText editTextDetail = new EditText(context);
                dialogView.addView(textViewDetail);
                dialogView.addView(editTextDetail);
                dialog.setView(dialogView);
                dialog.setPositiveButton("게시", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TalkDatas talkDatas = new TalkDatas();
                        talkDatas.title = editTextTitle.getText().toString();
                        talkDatas.id = loginUserData.id;
                        talkDatas.writer = loginUserData.name;
                        talkDatas.teamName = loginUserData.myTeamName;
                        talkDatas.contents = editTextDetail.getText().toString();

                        Call<TalkWriteResult> requestTalkWrite = service.getTalkWriteResult(talkDatas);
                        requestTalkWrite.enqueue(new Callback<TalkWriteResult>() {
                            @Override
                            public void onResponse(Call<TalkWriteResult> call, Response<TalkWriteResult> response) {
                                if(response.isSuccessful()){
                                    if(response.body().status.equals("success")){
                                        Toast.makeText(context, "글 게시가 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                                        itemDatas.add(new TalkListData(response.body().resultData.title, response.body().resultData.writer, response.body().resultData.time, response.body().resultData.contents));
//                                        talkAdapter.notifyDataSetChanged();
                                        ListReload();
                                    } else {
                                        Toast.makeText(context, response.body().reason, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<TalkWriteResult> call, Throwable t) {
                                Toast.makeText(context, "서버 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
//                        long now = System.currentTimeMillis();
//                        Date date = new Date(now);
//                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String time = sdfNow.format(date);
//                        String detail = editTextDetail.getText().toString();
//                        itemDatas.add(new TalkListData(title,name,time,detail));


                        talkAdapter.notifyDataSetChanged();
                        dialog.dismiss();
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

        return layout;
    }

    @Override
    public void onRefresh() {
        ListReload();
        refreshLayout.setRefreshing(false);
        Toast.makeText(context, "페이지 리로드", Toast.LENGTH_SHORT).show();
    }

    public void ListReload(){
        Call<TalkSearchResult> requestSearchTalk = service.getTalkSearchResult(loginUserData.myTeamName);
        requestSearchTalk.enqueue(new Callback<TalkSearchResult>() {
            @Override
            public void onResponse(Call<TalkSearchResult> call, Response<TalkSearchResult> response) {
                if(response.isSuccessful()){
                    if(response.body().status.equals("success")){
                        itemDatas.removeAll(itemDatas);
                        for(int i = 0 ; i<response.body().resultData.size(); i++){
                            itemDatas.add(new TalkListData(response.body().resultData.get(i).title, response.body().resultData.get(i).writer, response.body().resultData.get(i).time, response.body().resultData.get(i).contents));
                        }
                        talkAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, response.body().reason, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TalkSearchResult> call, Throwable t) {
                Toast.makeText(context, "서버의 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
