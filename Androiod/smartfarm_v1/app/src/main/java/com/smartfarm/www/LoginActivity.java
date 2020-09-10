package com.smartfarm.www;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText input_password, input_id; //아이디, 비번 입력창

    ImageButton login_button, register_button; //로그인, 회원가입 페이지로 넘어가는 버튼

    Button test_login; //백도어 버튼 나중에 없애야댐

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        input_password = findViewById(R.id.input_password);
        input_id = findViewById(R.id.input_id);
        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.go_register_button);

        test_login = findViewById(R.id.test_login);// 없애야댐

        //로그인 버튼 클릭시 이벤트
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = input_id.getText().toString();
                String password = input_password.getText().toString();
                Toast.makeText(LoginActivity.this, ""+id+","+password, Toast.LENGTH_SHORT).show();
            }
        });
        //회원가입 버튼 클릭시 이벤트
       register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
       //없애야함
        test_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}