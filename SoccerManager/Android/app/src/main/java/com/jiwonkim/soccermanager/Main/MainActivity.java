package com.jiwonkim.soccermanager.Main;


import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiwonkim.soccermanager.Main.Home.HomeFragment;
import com.jiwonkim.soccermanager.Main.Mypage.MypageFragment;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamPageFragment;
import com.jiwonkim.soccermanager.Main.Search.SearchFragment;
import com.jiwonkim.soccermanager.Main.Talk.TalkFragment;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

import static com.jiwonkim.soccermanager.R.id.schedule;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView homeBtn, searchBtn, mypageBtn, scheduleBtn, talkBtn;
    ViewPager viewPager;
    int previousMenu = 0;

    private long backKeyPressedTime = 0;
    private Toast toast;
//    HomeFragment homeFragment;

    public static MediaPlayer mp;
    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
        setContentView(R.layout.activity_main);

        homeBtn = (ImageView) findViewById(R.id.home);
        searchBtn = (ImageView) findViewById(R.id.search);
        mypageBtn = (ImageView) findViewById(R.id.mypage);
        scheduleBtn = (ImageView) findViewById(schedule);
        talkBtn = (ImageView) findViewById(R.id.talk);

        homeBtn.setOnClickListener(this);
        homeBtn.setTag(0);
        searchBtn.setOnClickListener(this);
        searchBtn.setTag(1);
        mypageBtn.setOnClickListener(this);
        mypageBtn.setTag(2);
        scheduleBtn.setOnClickListener(this);
        scheduleBtn.setTag(3);
        talkBtn.setOnClickListener(this);
        talkBtn.setTag(4);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);

//        homeFragment = new HomeFragment();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeMenuWhite(previousMenu);
                changeMenuIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        mp = MediaPlayer.create(this,R.raw.music);
        mp.setLooping(true);
        mp.start();
//        startService(new Intent("com.jiwonkim.soccermanager"));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(getApplicationContext(),"\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
            toast.cancel();
        }
    }



    @Override
    public void onClick(View v) {
        int tag = (int)v.getTag();
        viewPager.setCurrentItem(tag);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter{
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    HomeFragment home = new HomeFragment();
                    home.setContext(getApplication());
                    return home;
                case 1:
                    SearchFragment search = new SearchFragment();
                    search.setContext(getApplicationContext());
                    return search;
                case 2:
                    MypageFragment mypage = new MypageFragment();
                    mypage.setContext(getApplicationContext());
                    return mypage;
                case 3:
                    TeamPageFragment teampage = new TeamPageFragment();
                    teampage.setContext(getApplicationContext());
                    return teampage;
                case 4:
                    TalkFragment talk = new TalkFragment();
                    talk.setContext(getApplicationContext());
                    return talk;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

    }
    private void changeMenuIcon(int selectMenu){
        switch (selectMenu){
            case 0:
                homeBtn.setImageResource(R.drawable.home_btn2);
                previousMenu = 0;
                break;
            case 1:
                searchBtn.setImageResource(R.drawable.search_btn2);
                previousMenu = 1;
                break;
            case 2:
                mypageBtn.setImageResource(R.drawable.mypage_btn2);
                previousMenu = 2;
                break;
            case 3:
                scheduleBtn.setImageResource(R.drawable.my_team_btn2);
                previousMenu = 3;
                break;
            case 4:
                talkBtn.setImageResource(R.drawable.talk_btn2);
                previousMenu = 4;
                break;
        }
    }

    private void changeMenuWhite(int previousMenu){
        switch (previousMenu){
            case 0:
                homeBtn.setImageResource(R.drawable.home_btn1);
            case 1:
                searchBtn.setImageResource(R.drawable.search_btn1);
            case 2:
                mypageBtn.setImageResource(R.drawable.mypage_btn1);
            case 3:
                scheduleBtn.setImageResource(R.drawable.my_team_btn1);
            case 4:
                talkBtn.setImageResource(R.drawable.talk_btn1);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(!mp.isPlaying()){
//            mp.start();
//        }
//        Log.d("액티비티 생명주기","리즘ㅁ");
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d("액티비티 생명주기","스땁");
//        stopService(new Intent("com.jiwonkim.soccermanager"));
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }

    @Override
    protected void onUserLeaveHint() {
//        mp.pause();
    }
}
