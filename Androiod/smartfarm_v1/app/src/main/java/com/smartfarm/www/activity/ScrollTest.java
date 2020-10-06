package com.smartfarm.www.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.smartfarm.www.R;

    public class ScrollTest extends AppCompatActivity {

        Button bt11, bt22, bt33;
        HorizontalScrollView scrollView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrolltest);

        scrollView = findViewById(R.id.scroll);

        bt11 = findViewById(R.id.b1);
        bt22 = findViewById(R.id.b2);
        bt33 = findViewById(R.id.b3);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0,0);
            }
        });
        bt22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(1200,0);
            }
        });
        bt33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(2400,0);
            }
        });



    }
}
