package com.smartfarm.www.data;

import com.google.gson.annotations.SerializedName;

import okhttp3.internal.Version;

public class EmbeddedData {
    @SerializedName("code")
    int code;

    @SerializedName("userNo")
    int userNo;

    //현재 계측한 센서값들
    @SerializedName("recentHumi")
    int recentHumi;

    @SerializedName("Temp")
    int Temp;

    //사전에 세팅한 값들
    @SerializedName("Humi")
    int Humi;

    //현재 아두이노의 상태
    @SerializedName("automode")
    int automode;

    @SerializedName("pump")
    int pump;

    @SerializedName("fan")
    int fan;

    @SerializedName("led")
    int led;

    public EmbeddedData(int code, int userNo, int recentHumi, int temp) {
        this.code = code;
        this.userNo = userNo;
        this.recentHumi = recentHumi;
        Temp = temp;
    }

    public EmbeddedData(int code, int userNo, int humi) {
        this.code = code;
        this.userNo = userNo;
        Humi = humi;
    }

    public EmbeddedData(int code, int userNo, int automode, int pump, int fan, int led) {
        this.code = code;
        this.userNo = userNo;
        this.automode = automode;
        this.pump = pump;
        this.fan = fan;
        this.led = led;
    }
}
