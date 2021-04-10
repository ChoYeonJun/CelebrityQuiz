package com.example.celebrityquiz.firebaseAccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.celebrityquiz.R;
import com.example.celebrityquiz.SolutionAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);


//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        SolutionAdapter solutionAdapter = new SolutionAdapter(rankList, this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(solutionAdapter);
    }

}
