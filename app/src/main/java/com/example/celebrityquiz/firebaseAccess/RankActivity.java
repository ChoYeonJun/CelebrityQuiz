package com.example.celebrityquiz.firebaseAccess;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.celebrityquiz.Quiz;
import com.example.celebrityquiz.R;
import com.example.celebrityquiz.SolutionAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        List<Record> recordList = (List<Record>) getIntent().getSerializableExtra("recordList");
        for(Record record: recordList){
            Log.d("firebase", record.getUsername());
            Log.d("firebase", record.getElapsedTime());
            Log.d("firebase", record.getTotalQuizNum());
        }
        RankAdapter rankAdapter = new RankAdapter(this, recordList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rankAdapter);


    }

}
