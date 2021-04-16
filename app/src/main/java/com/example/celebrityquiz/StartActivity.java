package com.example.celebrityquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.celebrityquiz.firebaseAccess.InitActivity;
import com.example.celebrityquiz.firebaseAccess.RankActivity;
import com.example.celebrityquiz.firebaseAccess.Record;
import com.example.celebrityquiz.firebaseAccess.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;


public class StartActivity extends MainActivity {
    private Button btn_setting;
    private Button btn_Start;
    private Button btn_rank;
    TextView username;
    CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        username = findViewById(R.id.username);
        profile_image = findViewById(R.id.profile_image);
        btn_setting = findViewById(R.id.btn_setting);
        btn_Start = findViewById(R.id.btn_Start);
        btn_rank = findViewById(R.id.btn_rank);
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

        btn_rank.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, RankActivity.class);
                startActivity(intent); // 액티비티 이동.
            }

        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.profile);
                }else{
                    Glide.with(StartActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void rankButtonOnClick(View view) {

        Intent intent = new Intent(com.example.celebrityquiz.StartActivity.this, RankActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


//        ArrayList<Record> list = new ArrayList<Record>(recordList);
//        intent.putExtra("recordList", list);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();;
                startActivity(new Intent(StartActivity.this, InitActivity.class));
                finish();
                return true;
        }

        return false;
    }


}