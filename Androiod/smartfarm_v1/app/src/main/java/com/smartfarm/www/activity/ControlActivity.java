package com.smartfarm.www.activity;

/*안드로이드 제어코드
1001:워터펌프 활성화  | 1000:워터펌프 비활성화
2001:환풍기 활성화    | 2000:환풍기 비활성화
3001:LED 활성화       | 3000:LED 비활성화
4***:자동모드 토양수분량 설정
5***:자동모드 습도 조절
9001:자동모드 ON      | 9000:자동모드 OFF
*/

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smartfarm.www.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ControlActivity extends Fragment {

    LinearLayout autoLayout, manualLayout;
    EditText show_temp_change, show_humidity_change, show_soil_change;
    Button temp_up, temp_down, humidity_up, humidity_down, soil_up, soil_down, auto_change_apply, changeAuto, changeManual, pump_on, pump_off, fan_on, fan_off, LED_on, LED_off;

    //임시 온습도 수분 디폴트값
    int temp = 20;
    int humidity = 30;
    int soil = 20;

    Socket socket;      //소켓 객체 생성
    ConnectRaspi connectRaspi;    //소켓통신을 위한 쓰레드객체

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.control_page,container,false);

        changeAuto = view.findViewById(R.id.change_auto);
        changeManual = view.findViewById(R.id.change_manual);
        autoLayout = view.findViewById(R.id.auto_layout);
        manualLayout = view.findViewById(R.id.manual_layout);
        temp_up = view.findViewById(R.id.temp_up);
        temp_down = view.findViewById(R.id.temp_down);
        humidity_up = view.findViewById(R.id.humidity_up);
        humidity_down = view.findViewById(R.id.humidity_down);
        soil_up = view.findViewById(R.id.soil_up);
        soil_down = view.findViewById(R.id.soil_down);
        auto_change_apply = view.findViewById(R.id.auto_change_apply);
        show_temp_change = view.findViewById(R.id.show_temp_change);
        show_humidity_change = view.findViewById(R.id.show_humidity_change);
        show_soil_change = view.findViewById(R.id.show_soil_change);
        pump_on = view.findViewById(R.id.pump_on);
        pump_off = view.findViewById(R.id.pump_off);
        fan_on = view.findViewById(R.id.fan_on);
        fan_off = view.findViewById(R.id.fan_off);
        LED_on = view.findViewById(R.id.LED_on);
        LED_off = view.findViewById(R.id.LED_off);

        //자동 모드 버튼클릭리스너
        changeAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoLayout.setVisibility(View.VISIBLE); //자동모드 레이아웃을 보여줌
                manualLayout.setVisibility(View.GONE); //수동모드 레이아웃을 없앰
                connectRaspi = new ConnectRaspi("9001");    //소켓통신 송신
                connectRaspi.start();
            }
        });
        //수동 모드 버튼클릭리스너
        changeManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoLayout.setVisibility(View.GONE);
                manualLayout.setVisibility(View.VISIBLE);
                connectRaspi = new ConnectRaspi("9000");    //소켓통신 송신
                connectRaspi.start();
            }
        });
        //온도 상승 하락 버튼 리스너
        temp_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp++;
                show_temp_change.setText(String.valueOf(temp));
            }
        });
        temp_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp <=0) {
                } else{
                    temp--;
                    show_temp_change.setText(String.valueOf(temp));
                }
            }
        });
        humidity_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                humidity++;
                show_humidity_change.setText(String.valueOf(humidity));
            }
        });
        humidity_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(humidity <= 0){
                } else{
                    humidity--;
                    show_humidity_change.setText(String.valueOf(humidity));
                }
            }
        });
        soil_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soil++;
                show_soil_change.setText(String.valueOf(soil));
            }
        });
        soil_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(soil <= 0){
                }else{
                    soil--;
                    show_soil_change.setText(String.valueOf(soil));
                }
            }
        });

        //스프링쿨러 on/off
        pump_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRaspi = new ConnectRaspi("1001");    //소켓통신 송신
                connectRaspi.start();
            }
        });
        pump_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRaspi = new ConnectRaspi("1000");    //소켓통신 송신
                connectRaspi.start();
            }
        });

        //팬 on/off
        fan_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRaspi = new ConnectRaspi("2001");    //소켓통신 송신
                connectRaspi.start();
            }
        });
        fan_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRaspi = new ConnectRaspi("2000");    //소켓통신 송신
                connectRaspi.start();
            }
        });

        //조명 on/off
        LED_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRaspi = new ConnectRaspi("3001");    //소켓통신 송신
                connectRaspi.start();
            }
        });
        LED_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectRaspi = new ConnectRaspi("3000");    //소켓통신 송신
                connectRaspi.start();
            }
        });

        //소켓으로 쏘세요 쏘는부분임
        auto_change_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String temp_str = show_temp_change.getText().toString(); //온도 값 가져오기
                String humidity_str = show_humidity_change.getText().toString();    //습도 값 가져오기
                String soil_str = show_soil_change.getText().toString();            //수분 값 가져오기

                connectRaspi = new ConnectRaspi("40" + soil_str);       //소켓통신 송신(기대 토양수분량)
                connectRaspi.start();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                connectRaspi = new ConnectRaspi("50" + humidity_str);   //소켓통신 송신(기대 습도)
                connectRaspi.start();
            }
        });

        return view;
    }

    public void onStop() {
        super.onStop();
        try {
            socket.close();     //종료시 소켓도 닫아주어야한다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ConnectRaspi extends Thread {     //소켓통신을 위한 스레드
        private String ip = "192.168.0.7";  // 서버의 IP 주소
        private int port = 9999;            // PORT번호를 꼭 라즈베리파이와 맞추어 주어야한다.
        private String sendMessage;         //송신할 데이터

        ConnectRaspi(String sendMessage) {
            this.sendMessage = sendMessage; //쓰레드를 생성할 때 코드를 입력받음
        }

        public void run() {
            try {   //소켓 생성
                InetAddress serverAddr = InetAddress.getByName(ip); //IP주소를 가져온다.
                socket = new Socket(serverAddr, port);              //소켓에 IP와 포트번호 할당

                //데이터 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                out.println(sendMessage);
                socket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}