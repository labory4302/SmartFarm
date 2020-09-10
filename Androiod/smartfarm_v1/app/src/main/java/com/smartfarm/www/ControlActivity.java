package com.smartfarm.www;

import android.os.Bundle;
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

public class ControlActivity extends Fragment {

    LinearLayout autoLayout, manualLayout;
    EditText show_temp_change, show_humidity_change, show_soil_change;
    Button temp_up, temp_down, humidity_up, humidity_down, soil_up, soil_down, auto_change_apply, changeAuto, changeManual;

    //임시 온습도 수분 디폴트값
    int temp = 20;
    int humidity = 30;
    int soil = 20;

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

        //자동 모드 버튼클릭리스너
        changeAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoLayout.setVisibility(View.VISIBLE); //자동모드 레이아웃을 보여줌
                manualLayout.setVisibility(View.GONE); //수동모드 레이아웃을 없앰
            }
        });
        //수동 모드 버튼클릭리스너
        changeManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoLayout.setVisibility(View.GONE);
                manualLayout.setVisibility(View.VISIBLE);
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
        //소켓으로 쏘세요 쏘는부분임
        auto_change_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_str = show_temp_change.getText().toString(); //온도 값 가져오기
                String humidity_str = show_humidity_change.getText().toString(); //습도 값 가져오기
                String soil_str = show_soil_change.getText().toString(); //수분 값 가져오기
            }
        });

        return view;
    }

}