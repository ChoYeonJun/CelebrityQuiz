package com.example.celebrityquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class StartActivity extends MainActivity {
    private Button btn_setting;
    private Button btn_Start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btn_setting = findViewById(R.id.btn_setting);
        btn_Start = findViewById(R.id.btn_Start);
        btn_setting.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent); // 액티비티 이동.
            }

            });
        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, QuizActivity.class);
                int level = intent.getIntExtra("level", 0);
                seconds = intent.getIntExtra("seconds", 30);
                intent.putExtra("level", level);
                intent.putExtra("seconds", seconds);
                startActivity(intent);

            }
        });
        }




}