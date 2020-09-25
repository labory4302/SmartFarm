package com.smartfarm.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartfarm.www.R;
import com.smartfarm.www.data.RegisterData;
import com.smartfarm.www.data.RegisterResponse;
import com.smartfarm.www.data.UserInformation;
import com.smartfarm.www.network.RetrofitClient;
import com.smartfarm.www.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeMyInformationChangeActivity extends AppCompatActivity {
    EditText changemyinformation_change_name,
            changemyinformation_change_nickname,
            changemyinformation_change_email,
            changemyinformation_change_id,
            changemyinformation_change_pwd,
            changemyinformation_change_location;
    Button changemyinformation_change_button;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changemyinformation_change_page);
        service = RetrofitClient.getClient().create(ServiceApi.class);
        UserInformation userInfo = UserInformation.getUserInformation();

        changemyinformation_change_name = (EditText) findViewById(R.id.changemyinformation_change_name);
        changemyinformation_change_nickname = (EditText) findViewById(R.id.changemyinformation_change_nickname);
        changemyinformation_change_email = (EditText) findViewById(R.id.changemyinformation_change_email);
        changemyinformation_change_id = (EditText) findViewById(R.id.changemyinformation_change_id);
        changemyinformation_change_pwd = (EditText) findViewById(R.id.changemyinformation_change_pwd);
        changemyinformation_change_location = (EditText) findViewById(R.id.changemyinformation_change_location);

        changemyinformation_change_name.setText(userInfo.getUserName());
        changemyinformation_change_nickname.setText(userInfo.getUserNickName());
        changemyinformation_change_email.setText(userInfo.getUserEmail());
        changemyinformation_change_id.setText(userInfo.getUserID());
        changemyinformation_change_pwd.setText(userInfo.getUserPwd());
        changemyinformation_change_location.setText(userInfo.getUserLocation());

        changemyinformation_change_button = (Button) findViewById(R.id.changemyinformation_change_button);
        changemyinformation_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = changemyinformation_change_name.getText().toString();
                final String nickname = changemyinformation_change_nickname.getText().toString();
                final String email = changemyinformation_change_email.getText().toString();
                final String id = changemyinformation_change_id.getText().toString();
                final String pwd = changemyinformation_change_pwd.getText().toString();
                final String location = changemyinformation_change_location.getText().toString();

                UserInformation userInfo1 = UserInformation.getUserInformation();
                final int no = userInfo1.getUserNo();

                changeInformation(new RegisterData(name, nickname, email, id, pwd, location, no));

                Intent intent = new Intent(getApplicationContext(), ChangeMyInformationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void changeInformation(RegisterData data){
        service.MypageChangeMyInformation(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();

                UserInformation userInfo2 = UserInformation.getUserInformation();

                userInfo2.setUserName(result.getUserName());
                userInfo2.setUserNickName(result.getUserNickName());
                userInfo2.setUserEmail(result.getUserEmail());
                userInfo2.setUserID(result.getUserID());
                userInfo2.setUserPwd(result.getUserPwd());
                userInfo2.setUserLocation(result.getUserLocation());

                Toast.makeText(ChangeMyInformationChangeActivity.this, "개인정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                if (result.getCode() == 200) {
                    finish();
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(ChangeMyInformationChangeActivity.this, "개인정보수정 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("개인정보수정 에러 발생", t.getMessage());
            }
        });
    }
}
