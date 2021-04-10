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
import com.example.celebrityquiz.firebaseAccess.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SolutionActivity extends AppCompatActivity{

    ImageButton buttonRank;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        // Define Navigation
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Results");
        }

        // Interface instance to get values from QuizActivity
        int scoreValue = getIntent().getIntExtra("score", 0);
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

        auth = FirebaseAuth.getInstance();

    }

    private void record(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(com.example.celebrityquiz.SolutionActivity.this, "record on Ranking is failed", Toast.LENGTH_SHORT).show();
                            Log.d("record exception", "onComplete: Failed=" + task.getException().getMessage());
                        }else{
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

//                            reference.child("")
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
//                                        Intent intent = new Intent(com.example.celebrityquiz.firebaseAccess.RankActivity.this, MainActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
                                    }else{
//                                        Toast.makeText(com.example.celebrityquiz.firebaseAccess.RankActivity.this, "reference error ", Toast.LENGTH_SHORT).show();
//                                        Log.d("reference exception", "onComplete: Failed=" + task.getException().getMessage());
                                    }
                                }
                            });

                        }
                    }
                });
    }

    // Function to display well done image if user gets all correct | also settings for total value
    public void displayWellDone(int score) {

        // Set view for well done image
        ImageView imageView = findViewById(R.id.wellDoneImage);
        imageView.setVisibility(View.INVISIBLE); // set image invisible

        // display well done image if user gets all correct
        if (score == 5) imageView.setVisibility(View.VISIBLE);
    }

    public void rankButtonOnClick(View view) {

    }


}
