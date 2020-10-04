package com.smartfarm.www.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smartfarm.www.R;
import com.smartfarm.www.appInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends Fragment {
    View view;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    TextView tb1[] = new TextView[7];
    ImageView tb2[] = new ImageView[7];

    ProgressDialog dialog= null; //dialog 데이터 로딩
    Message message = null; // 데이터 로딩 후 메인 UI 업데이트 메시지

    private final int FINISH = 999; // 핸들러 메시지 구분 ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page,container,false);

        listViewAdapter = new ListViewAdapter();

        listView = (ListView) view.findViewById(R.id.listview);

        for(int i=0; i<=6; i++){
            int resId = getResources().getIdentifier("today"+i,"id", getContext().getPackageName());
            tb1[i] = (TextView) view.findViewById(resId);
        }
        for(int i=0; i<=6; i++){
            int resId = getResources().getIdentifier("weather"+i,"id", getContext().getPackageName());
            tb2[i] = (ImageView) view.findViewById(resId);
        }




        if (appInfo.strawberry==null){
            Log.d("if", "여기 : ");
            //로딩창 실행
            dialog = new ProgressDialog(getContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("데이터 확인중");
            dialog.show();// 프로그레스바 시작`

            // 핸들러에 보내기 위한 메시지 생성 (중복 메시지 확인하고 덮어씌우거나 없으면 새로 메시지 객체 생성)
            message = mHandler.obtainMessage(); // 핸들러의 메시지 객체 획득
            message.what = FINISH;

            // 쓰레드는 View 자원들에 직접 접근이 불가
            // 쓰레드로부터 전달받은 메세지들을 '메세지 큐(Message Queue)' 를 이용해 순차적으로 관리
            // 쓰레드 / 메시지 큐 / handler 통신해서 메인 스레드가 일을 순차적으로 처리함
            // 데이터 다 들어왔는지 확인 쓰레드
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        if (appInfo.weatherMap != null) {
                            // 데이터 로딩 롼료시 UI 변경 메시지 전송

                            //메세지 큐에 데이터보냄냄
                            mHandler.sendMessage(message);
                            break;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }
            }).start();

        }else{
            Log.d("else", "여기 : ");
            getWeather();
        }



        return view;
    }

    // 데이터 전송 롼료시 UI 변경을 위한 핸들러
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH :
                    Log.d("??","dfd 시작");
                    getWeather();
                    listViewAdapter.addItem("테스트1",appInfo.cabbage);
                    listViewAdapter.addItem("테스트2",appInfo.rice);
                    listViewAdapter.addItem("테스트3",appInfo.bean);
                    listViewAdapter.addItem("테스트4",appInfo.redPepper);
                    listViewAdapter.addItem("테스트5",appInfo.strawberry);

                    listView.setAdapter(listViewAdapter);
                    dialog.cancel();
                    break ;
                // TODO : add case.
            }
        }
    } ;

    //날씨값 설정
    public void getWeather(){
        long now = System.currentTimeMillis();
        // 현재시간의 날짜 생성하기
        Date date = new Date(now);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        int time = Integer.valueOf(timeFormat.format(date));
        Log.d("시간 ", "날씨시간 : "+timeFormat);
        Log.d("시간 ", "날씨시간 : "+time);

        Map<String,String> map = appInfo.weatherMap;
        // 온도/강수량  가져오기
        Double rainfall_day[] = new Double[7];
        for (int i = 0; i < 7; i++) {
            Log.i("text : ", i+"일차 날씨 : "+map.get("test"+(i*2)));
            String temp_temp = map.get("temp" + i);
            rainfall_day[i] = Double.parseDouble(map.get("rainfall" + i));

            String temp_temp_hl[] = temp_temp.split("°C");

            Log.d("온도", "과연  "+ temp_temp);

            if(i==0 && (time>=18 && time<=24) || (time>=0 && time<=6 )) {
                float avg_temp = (Float.parseFloat(temp_temp_hl[0]));
                tb1[i].setText("" + avg_temp + "℃");
                tb2[i].setImageResource(R.drawable.sun_icon);//
                // 0일차 날씨 : 4" data-night="true
                continue;
            }else {
                float avg_temp = (Float.parseFloat(temp_temp_hl[0]) + Float.parseFloat(temp_temp_hl[1])) / 2;
                tb1[i].setText("" + avg_temp + "℃");
            }

            if(Integer.parseInt(map.get("test"+(i*2))) == 1){
                tb2[i].setImageResource(R.drawable.sun_icon);//                     tb2[i].setText("맑음");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 2){
                tb2[i].setImageResource(R.drawable.cloudy_icon);//                  tb2[i].setText("구름 조금");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 3){
                tb2[i].setImageResource(R.drawable.cloud_icon);//                   tb2[i].setText("구름 많음");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 4){
                tb2[i].setImageResource(R.drawable.cloud_icon);//                   tb2[i].setText("흐림");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 5){
                tb2[i].setImageResource(R.drawable.soft_rain_icon);//               tb2[i].setText("비 확률");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 6){
                tb2[i].setImageResource(R.drawable.rain_icon);//                    tb2[i].setText("비 조금");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 7){
                tb2[i].setImageResource(R.drawable.hard_rain_icon);//               tb2[i].setText("비옴");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 8){
                tb2[i].setImageResource(R.drawable.hard_rain_icon);//               tb2[i].setText("폭우");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 9) {
                tb2[i].setImageResource(R.drawable.sun_icon);//                     tb2[i].setText("모름");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 30) {
                tb2[i].setImageResource(R.drawable.soft_rain_icon);//               tb2[i].setText("비올듯");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 31) {
                tb2[i].setImageResource(R.drawable.rain_icon);//                    tb2[i].setText("약한 비");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 32) {
                tb2[i].setImageResource(R.drawable.hard_rain_icon);//               tb2[i].setText("비옴");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 33) {
                tb2[i].setImageResource(R.drawable.hard_rain_icon);//               tb2[i].setText("큰 소나기");
            }else if(Integer.parseInt(map.get("test"+(i*2))) == 34) {
                tb2[i].setImageResource(R.drawable.storm_rain_icon);//              tb2[i].setText("비오고 천둥도 침");
            }else{
                tb2[i].setImageResource(R.drawable.sun_icon);//                     tb2[i].setText("오류");
            }
        }
    }


}
