package com.smartfarm.www.data;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("userID")
    String userID;

    @SerializedName("userPwd")
    String userPwd;

    public LoginData(String userID, String userPwd) {
        this.userID = userID;
        this.userPwd = userPwd;
    }
}
