package com.smartfarm.www.network;

import com.smartfarm.www.activity.LoginActivity;
import com.smartfarm.www.data.RegisterData;
import com.smartfarm.www.data.RegisterResponse;
import com.smartfarm.www.data.LoginData;
import com.smartfarm.www.data.LoginResponse;
import com.smartfarm.www.data.VersionData;
import com.smartfarm.www.data.VersionResponse;
import com.smartfarm.www.data.EmbeddedData;
import com.smartfarm.www.data.EmbeddedResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {

    //user login
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/register")
    Call<RegisterResponse> userRegister(@Body RegisterData data);

    //mypage
    @POST("/mypage/changemyinformation")
    Call<RegisterResponse> MypageChangeMyInformation(@Body RegisterData data);

    @POST("/mypage/version")
    Call<VersionResponse> MypageVersion();

    //embedded
    @POST("/embedded/data")
    Call<EmbeddedResponse> EmbeddedSensorData();

    @POST("/embedded/recentdata")
    Call<EmbeddedResponse> EmbeddedSensorRecentData();

    @POST("/embedded/status")
    Call<EmbeddedResponse> EmbeddedSensorStatus();
}