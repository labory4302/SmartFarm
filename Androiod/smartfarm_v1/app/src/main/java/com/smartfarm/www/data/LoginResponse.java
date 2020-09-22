package com.smartfarm.www.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private int userId;

    public int getCode() {
        return code;
    }

    public String getMessage() { return message; }

    public int getUserId() {
        return userId;
    }

    //////////////////////////////////////////////////////mypage

    @SerializedName("userNickName")
    private int userNickName;

    @SerializedName("userEmail")
    private int userEmail;

    @SerializedName("userLocation")
    private int userLocation;

    public int getUserNickName() {
        return userNickName;
    }

    public int getUserEmail() {
        return userEmail;
    }

    public int getUserLocation() {
        return userLocation;
    }
}
