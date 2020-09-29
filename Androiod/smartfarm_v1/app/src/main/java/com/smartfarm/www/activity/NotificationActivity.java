
package com.smartfarm.www.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartfarm.www.R;
import com.smartfarm.www.data.NotificationResponse;
import com.smartfarm.www.data.VersionResponse;
import com.smartfarm.www.network.RetrofitClient;
import com.smartfarm.www.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ServiceApi service;

    TextView notification_title, notification_contents;
    ListView notification_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        notification_title = findViewById(R.id.notification_title);
        notification_contents = findViewById(R.id.notification_contents);

        notification_listview = findViewById(R.id.notification_listview);
//
//        checkNotification();
    }
//    private void checkNotification() {
//        service.MypageNotification().enqueue(new Callback<NotificationResponse>() {
//            @Override
//            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
//                NotificationResponse result = response.body();
//                notification_title.setText(""+result.getNotificationTitle());
//                notification_contents.setText(""+result.getNotificationContents());
//
//                notification_listview.additem();
//            }
//
//            @Override
//            public void onFailure(Call<NotificationResponse> call, Throwable t) {
//                Toast.makeText(NotificationActivity.this, "공지사항을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
//                Log.e("공지사항을 불러오지 못했습니다.", t.getMessage());
//            }
//        });
//    }
}
