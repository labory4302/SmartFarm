package com.smartfarm.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartfarm.www.R;
import com.smartfarm.www.data.LoginData;
import com.smartfarm.www.data.LoginResponse;
import com.smartfarm.www.data.UserInformation;
import com.smartfarm.www.network.RetrofitClient;
import com.smartfarm.www.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText mIdView, mPasswordView;
    private Button mLoginButton, mregisterButton, testLoginbt;
    private CheckBox autoLogin_CheckBox;
    private ServiceApi service;
    private String autoLoginId, autoLoginPwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        service = RetrofitClient.getClient().create(ServiceApi.class);

        mIdView = (EditText) findViewById(R.id.login_id);
        mPasswordView = (EditText) findViewById(R.id.login_pwd);
        mLoginButton = (Button) findViewById(R.id.Login_Button);
        mregisterButton = (Button) findViewById(R.id.Register_Button);
        testLoginbt = (Button)findViewById(R.id.test_login_bt);
        autoLogin_CheckBox = (CheckBox)findViewById(R.id.autoLogin_CheckBox);

        mIdView.setPadding(100,0,0,0);
        mPasswordView.setPadding(100,0,0,0);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        autoLoginId = auto.getString("inputId", null);
        autoLoginPwd = auto.getString("inputPwd", null);

        mPasswordView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(autoLogin_CheckBox.isChecked()){
                        attemptLogin_auto();
                    } else{
                        attemptLogin_nonauto();
                    }
                }
                return false;
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoLogin_CheckBox.isChecked()){
                    attemptLogin_auto();
                } else{
                    attemptLogin_nonauto();
                }
            }
        });

        if(autoLogin_CheckBox.isChecked()){
            mIdView.setText(autoLoginId);
            mPasswordView.setText(autoLoginPwd);
            Log.d("check", autoLoginId + autoLoginPwd);
            mLoginButton.performClick();
        } else {
            //if checkbox unchecked
        }

        mregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        testLoginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin_nonauto() {
        mIdView.setError(null);
        mPasswordView.setError(null);

        String id = mIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mIdView.setError("비밀번호를 입력해주세요.");
            focusView = mIdView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = mPasswordView;
            cancel = true;
        }

        // 아이디의 유효성 검사
        if (id.isEmpty()) {
            mIdView.setError("아이디를 입력해주세요.");
            focusView = mIdView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLogin_nonauto(new LoginData(id, password));
        }
    }

    private void attemptLogin_auto() {
        mIdView.setError(null);
        mPasswordView.setError(null);

        String id = mIdView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            mIdView.setError("비밀번호를 입력해주세요.");
            focusView = mIdView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = mPasswordView;
            cancel = true;
        }

        // 아이디의 유효성 검사
        if (id.isEmpty()) {
            mIdView.setError("아이디를 입력해주세요.");
            focusView = mIdView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLogin_auto(new LoginData(id, password));
        }
    }

    private void startLogin_nonauto(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                //싱글톤 패턴에 유저정보 저장
                UserInformation userInfo = UserInformation.getUserInformation();
                userInfo.setUserName(result.getUserName());
                userInfo.setUserNickName(result.getUserNickName());
                userInfo.setUserEmail(result.getUserEmail());
                userInfo.setUserID(result.getUserID());
                userInfo.setUserPwd(result.getUserPwd());
                userInfo.setUserLocation(result.getUserLocation());
                userInfo.setUserNo(result.getUserNo());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }

    private void startLogin_auto(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                //싱글톤 패턴에 유저정보 저장
                UserInformation userInfo = UserInformation.getUserInformation();
                userInfo.setUserName(result.getUserName());
                userInfo.setUserNickName(result.getUserNickName());
                userInfo.setUserEmail(result.getUserEmail());
                userInfo.setUserID(result.getUserID());
                userInfo.setUserPwd(result.getUserPwd());
                userInfo.setUserLocation(result.getUserLocation());
                userInfo.setUserNo(result.getUserNo());

                //Auto Login function using sharedpreference
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.putString("inputId", result.getUserID());
                editor.putString("inputPwd", result.getUserPwd());
                editor.commit();
//                System.out.println(auto.getString("inputId", ""));
//                System.out.println(autoLoginId);
//                System.out.println(result.getUserID());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}