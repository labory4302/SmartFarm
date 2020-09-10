package com.smartfarm.www.network;

import com.smartfarm.www.data.RegisterData;
import com.smartfarm.www.data.RegisterResponse;
import com.smartfarm.www.data.LoginData;
import com.smartfarm.www.data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<RegisterResponse> userJoin(@Body RegisterData data);
}