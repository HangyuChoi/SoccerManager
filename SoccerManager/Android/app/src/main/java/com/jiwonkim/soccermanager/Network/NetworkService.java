package com.jiwonkim.soccermanager.Network;

import com.jiwonkim.soccermanager.Main.Login.LoginData;
import com.jiwonkim.soccermanager.Main.Login.LoginResult;
import com.jiwonkim.soccermanager.Main.Login.UserData;
import com.jiwonkim.soccermanager.Main.Mypage.ModifyResult;
import com.jiwonkim.soccermanager.Main.Mypage.WithdrawalResult;
import com.jiwonkim.soccermanager.Main.Regist.MemberData;
import com.jiwonkim.soccermanager.Main.Regist.RegistResult;
import com.jiwonkim.soccermanager.Main.Search.RegistTeamData;
import com.jiwonkim.soccermanager.Main.Search.RegistTeamResult;
import com.jiwonkim.soccermanager.Main.Search.SearchUserResult;
import com.jiwonkim.soccermanager.Main.Talk.TalkSearch.TalkSearchResult;
import com.jiwonkim.soccermanager.Main.Talk.TalkWrite.TalkDatas;
import com.jiwonkim.soccermanager.Main.Talk.TalkWrite.TalkWriteResult;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.MemberQuit.MemberQuitData;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.MemberQuit.MemberQuitResult;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.MyTeamFindResult;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.TeamFindResult;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamData.TeamInfo;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.CreateTeam.TeamCreateResult;
import com.jiwonkim.soccermanager.Main.TeamPage.TeamManage.FindTeamMember.FindTeamMemberResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by user on 2017-06-12.
 */

public interface NetworkService {
    // 로그인
    @POST("member/login")
    Call<LoginResult> getLoginResult(@Body LoginData loginData);
    // Call<서버에서 받을 정보를 담은 클래스> 호출할 메소드이름 (@Body 바디에 담아서 보낼 객체)

    // 회원가입
    @POST("member/register")
    Call<RegistResult> getRegistResult(@Body MemberData memberData);

    // 회원정보 수정
    @POST("member/modify/{id}")
    Call<ModifyResult> getModifyResult(@Path("id") String id, @Body UserData userData);

    // 유저 검색
    @GET("member/findUser/{id}")
    Call<SearchUserResult> getSearchUserResult(@Path("id") String id);

    // 회원 탈퇴
    @GET("member/delete/{id}")
    Call<WithdrawalResult> getWithdrawalResult(@Path("id") String id);

    // 구단 생성
    @POST("team/create")
    Call<TeamCreateResult> getTeamCreateResult(@Body TeamInfo teamInfo);

    // 구단 정보 조회
    @GET("team/findTeam/{name}")
    Call<TeamFindResult> getTeamDataResult(@Path("name") String name);

    // 내 구단 검색
    @GET("team/myteam/{id}")
    Call<MyTeamFindResult> getMyTeamResult(@Path("id") String id);

    // 구단 탈퇴
    @POST("team/quit")
    Call<MemberQuitResult> getMemberQuitResult(@Body MemberQuitData memberQuitData);

    // 구단명 / 주장 변경

    // 구단내 팀원 정보 검색
    @GET("team/myteam/search/{name}")
    Call<FindTeamMemberResult> getMyTeamMemberResult(@Path("name") String name);

    // 구단 가입
    @POST("team/join")
    Call<RegistTeamResult> getRegistTeamResult(@Body RegistTeamData registTeamData);

    // 게시판 불러오기
    @GET("board/search/{name}")
    Call<TalkSearchResult> getTalkSearchResult(@Path("name") String name);

    // 게시글 쓰기
    @POST("board/write")
    Call<TalkWriteResult> getTalkWriteResult(@Body TalkDatas talkDatas);


}
