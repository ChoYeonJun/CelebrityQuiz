package com.example.celebrityquiz.firebaseAccess;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.celebrityquiz.Quiz;
import com.example.celebrityquiz.R;
import com.example.celebrityquiz.SolutionAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RankActivity extends AppCompatActivity {
    List<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

//        List<Record> recordList = (List<Record>) getIntent().getSerializableExtra("recordList");
//
        loadCurrentGame();
        RankAdapter rankAdapter = new RankAdapter(this, recordList);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(rankAdapter);


    }

    public void loadCurrentGame() {
        String pathName = this.getFilesDir().getPath();
        File fullName = new File(pathName + File.separator );
        String string = null;
        try {
            FileInputStream fileInputStream = openFileInput("record.json");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            string = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Record>>(){}.getType();
        List<Record> list = gson.fromJson(string, type);
        recordList = list;

        Collections.sort(recordList, new Record.RecordElapsedTimeComparator());
        Collections.sort(recordList, new Record.RecordQuizNumComparator());
    }



}
