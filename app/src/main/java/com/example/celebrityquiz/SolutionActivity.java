package com.example.celebrityquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.celebrityquiz.firebaseAccess.RankActivity;
import com.example.celebrityquiz.firebaseAccess.Record;
import com.example.celebrityquiz.firebaseAccess.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SolutionActivity extends AppCompatActivity{

    ImageButton buttonRank;
    FirebaseAuth auth;
    DatabaseReference mDatabase;
    String userId;

    int elapsedTime;
    int scoreValue;

    ArrayList<Record> recordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();

        dataCheck();
        // Define Navigation
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Results");
        }

        // Interface instance to get values from QuizActivity
        scoreValue = getIntent().getIntExtra("score", 0);
        elapsedTime = getIntent().getIntExtra("elapsedTime",30);
        List<Quiz> quizList = (List<Quiz>) getIntent().getSerializableExtra("quizList");

        // Set view and display scoreValue
        TextView scoreView = findViewById(R.id.scoreTextView);
        scoreView.setText(String.valueOf(scoreValue));

        // Set score out-of view
        TextView scoreTotalView = findViewById(R.id.scoreTotalTextView);
        scoreTotalView.setText(String.valueOf(5));

        // See function
        displayWellDone(scoreValue);

        // RecycleView definitions
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        SolutionAdapter solutionAdapter = new SolutionAdapter(quizList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(solutionAdapter);

        //rank button def
        buttonRank = findViewById(R.id.buttonRank);


    }



//    private void record(final String userId, String username, String elapsedTime, String totalQuizNUm){
//        Record record = new Record(username, totalQuizNUm,elapsedTime);
//        Map<String, Object> recordValues = record.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/Records/" + userId, recordValues);
//
//        mDatabase.updateChildren(childUpdates);
//    }

    // Function to display well done image if user gets all correct | also settings for total value
    public void displayWellDone(int score) {

        // Set view for well done image
        ImageView imageView = findViewById(R.id.wellDoneImage);
        imageView.setVisibility(View.INVISIBLE); // set image invisible

        // display well done image if user gets all correct
        if (score == 5) imageView.setVisibility(View.VISIBLE);
    }
    private void collectRecords(Map<String,Object> records) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : records.entrySet()){
            //Get user map
            Map singleRecords = (Map) entry.getValue();
//            Log.d("firebase", (Map)entry.getValue());
            //Get phone field and append to list
            Record record = new Record((String)singleRecords.get("username"), (String)singleRecords.get("totalQuizNum"), (String)singleRecords.get("elapsedTime"));
//            elapseTimes.add((Long) singleRecords.get("elapsedTime"));
//            totalQuizNum.add((Long) singleRecords.get("totalQuizNum"));
//            username.add((Long) singleRecords.get("username"));
            recordList.add(record);
      }
        for(Record record: recordList){
            Log.d("firebase", record.getUsername());
            Log.d("firebase", record.getElapsedTime());
            Log.d("firebase", record.getTotalQuizNum());
        }

    }

    private void dataCheck(){
        recordList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Records");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectRecords((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }
    public void rankButtonOnClick(View view) {

        Intent intent = new Intent(com.example.celebrityquiz.SolutionActivity.this, RankActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        Collections.sort(recordList, new Record.RecordElapsedTimeComparator());
        Collections.sort(recordList, new Record.RecordQuizNumComparator());
        ArrayList<Record> list = new ArrayList<Record>(recordList);
        intent.putExtra("recordList", list);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


}
