package com.smartfarm.www.data;

import com.google.gson.annotations.SerializedName;

import okhttp3.internal.Version;

public class EmbeddedData {
    @SerializedName("code")
    private int code;

    @SerializedName("userNo")
    private int userNo;

    @SerializedName("Humi")
    private int Humi;

    @SerializedName("Temp")
    private int Temp;

    @SerializedName("automode")
    private int automode;

    @SerializedName("pump")
    private int pump;

    @SerializedName("fan")
    private int fan;

    @SerializedName("led")
    private int led;

    public EmbeddedData(int code, int userNo, int humi, int temp) {
        this.code = code;
        this.userNo = userNo;
        Humi = humi;
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
