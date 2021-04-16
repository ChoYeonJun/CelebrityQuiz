package com.example.celebrityquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.celebrityquiz.firebaseAccess.Record;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    // Declare variables
    private List<Quiz> quizList;
    private int seconds;
    private int indexCurrentQuestion;

    private TextView questionView;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    private RadioButton radioButtonThree;
    private RadioButton radioButtonFour;
    private Button buttonPrevious;
    private Button buttonNext;
    private TextView textTime;
    private static CountDownTimer countDownTimer;
    //kim add
    private String hintbox1[] = {"Blackstar","Grammy Legend Award","King of Pop","WWE","China"};
    private String hintbox2[] = {"Our Song","Alipay","Los Angeles Lakers","Numver.007","George VI"};
    private String hintbox3[] = {"Science","Rocket Man","South African","NoShow","Infinity"};
    private int life = 2;
    FirebaseAuth auth;
    DatabaseReference mDatabase;

    ImageButton buttonRank;
    String userId;
    int elapsedTime;
    int scoreValue;
    final int STOP_TIME_REQUEST = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate((savedInstanceState));
        setContentView(R.layout.activity_quiz);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        // Hide toolbar
//        Objects.requireNonNull(getSupportActionBar()).hide();

        // Define Activity views
        questionView = findViewById(R.id.celebrityQuestion);
        imageView = findViewById(R.id.celebrityImage);
        radioGroup = findViewById(R.id.celebrityOption);
        radioButtonOne = findViewById(R.id.radioButtonOne);
        radioButtonTwo = findViewById(R.id.radioButtonTwo);
        radioButtonThree = findViewById(R.id.radioButtonThree);
        radioButtonFour = findViewById(R.id.radioButtonFour);
        textTime = findViewById(R.id.textTime);

        // setOnClickListener and set checked onClick for each button
        radioButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 1;
            }
        });

        radioButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 2;
            }
        });

        radioButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 3;
            }
        });

        radioButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RadioButton) view).setChecked(true);
                quizList.get(indexCurrentQuestion).userAnswer = 4;
            }
        });

        // Define button views
        buttonNext = findViewById(R.id.buttonNext);
        buttonPrevious = findViewById(R.id.buttonPrevious);

        // Access intent interface and get variables
        Intent intent = getIntent();
        int level = intent.getIntExtra("level", 0);
        seconds = intent.getIntExtra("seconds", 30);
        String string = null;

        // Safely read data from saved file
        try {
            FileInputStream fileInputStream = openFileInput("myJson");
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
        Type type = new TypeToken<List<Quiz>>(){}.getType();
        List<Quiz> list = gson.fromJson(string, type);

        // Set sublist based on user set level
        if (level == 1) {
            assert list != null;
            quizList = list.subList(0, 5);
        } else if (level == 2) {
            assert list != null;
            quizList = list.subList(5, 10);
        } else {
            assert list != null;
            quizList = list.subList(10, 15);
        }

        // initialise and set for each index in current activity as current question
        indexCurrentQuestion = 0;
        Quiz currentQuestion = quizList.get(indexCurrentQuestion);
        currentQuestionView(currentQuestion);
        buttonPrevious.setEnabled(false); // Disable previous button when current index is 0

        // See function
        startTimer();



        // When user submit quiz, stop time and start Solution Activity
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                Intent i = new Intent(QuizActivity.this, SolutionActivity.class);

                i.putExtra("elapsedTime", elapsedTime);
                i.putExtra("score", getScore());
                // Change List to ArrayList to accommodate subList
                ArrayList<Quiz> list = new ArrayList<>(quizList);
                i.putExtra("quizList", list);
                i.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );

                mDatabase.child("Records").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase record", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase record", String.valueOf(task.getResult().getValue()));
                            String value = String.valueOf(task.getResult().getValue());
                            value = value.replace("{", "");
                            value = value.replace("}", "");
                            String[] values = value.split(", ");
                            Log.d("firebase record", values[0].split("=")[1]);
                            Log.d("firebase record", values[1].split("=")[1]);
                            Log.d("firebase record", values[2].split("=")[1]);
//                    Log.d("firebase", values[3].split("=")[1]);

                            String totalQuizNum = values[0].split("=")[1];
                            String elapsedTime =  values[1].split("=")[1];
                            String username  =  values[2].split("=")[1];

                            CalculateData(username, elapsedTime, totalQuizNum);
                        }
                    }
                });
//        totalQuizNum = Integer.toString(10);
//        elapsedTime = values[2].split("=")[1];
//        username = values[3].split("=")[1];
//        Log.d("firebase", totalQuizNum + elapsedTime + username);

                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        stopTimer();
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this,StartActivity.class);
        setIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
        startActivity(setIntent);
    }
    private void CalculateData(String username, String elapsedTime, String totalQuizNum){
        int QuizNum = Integer.parseInt(totalQuizNum);
        QuizNum += getScore();
        Log.d("firebase Calculate", Integer.valueOf(getScore()).toString());
        totalQuizNum = Integer.valueOf(QuizNum).toString();
        Log.d("firebase Calculate", totalQuizNum);

        int time = Integer.parseInt(elapsedTime) ;
        time += (seconds - Integer.parseInt((String) textTime.getText()));
        elapsedTime = Integer.valueOf(time).toString();
        Log.d("firebase Calculate", elapsedTime);

        record(userId,username, elapsedTime, totalQuizNum);
    }

    private void record(final String userId, String username, String elapsedTime, String totalQuizNUm){
        Record record = new Record(username, totalQuizNUm,elapsedTime);
        Map<String, Object> recordValues = record.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Records/" + userId, recordValues);
        Log.d("firebase record", "Records updated");
        mDatabase.updateChildren(childUpdates);
    }

   
    // Start countdown. OnFinish, start Solution Activity
    public void startTimer() {
        textTime.setText(String.valueOf(seconds));
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textTime.setText(String.valueOf((int) (millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(QuizActivity.this, SolutionActivity.class);
                i.putExtra("score", getScore());
                i.putExtra("elapsedTime", seconds);
                // Change List to ArrayList to accommodate subList
                ArrayList<Quiz> list = new ArrayList<>(quizList);
                i.putExtra("quizList", list);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }

        }.start();
    }

    // Cancel timer to prevent countDown in background
    // If not defined, Solution Activity will start even when user goes back to
    // Main Activity because Quiz Activity doesn't get destroyed instantly
    public static void stopTimer() {
        countDownTimer.cancel();
    }

    // Pre-define new views before setting previous question as current question, for index < 0
    public void onButtonPrevious(View view) {
        if(indexCurrentQuestion != 0) {
            indexCurrentQuestion--;
            if(indexCurrentQuestion == 0) buttonPrevious.setEnabled(false);
            if(indexCurrentQuestion != (quizList.size() - 1)) buttonNext.setEnabled(true);
            Quiz currentQuestion = quizList.get(indexCurrentQuestion);
            currentQuestionView(currentQuestion);

            radioGroup = findViewById(R.id.celebrityOption);
            if(currentQuestion.userAnswer == 0) radioGroup.clearCheck();
            else {
                switch (currentQuestion.userAnswer) {
                    case 1: {
                        radioGroup.check(R.id.radioButtonOne);
                        break;
                    }
                    case 2: {
                        radioGroup.check(R.id.radioButtonTwo);
                        break;
                    }
                    case 3: {
                        radioGroup.check(R.id.radioButtonThree);
                        break;
                    }
                    case 4: {
                        radioGroup.check(R.id.radioButtonFour);
                        break;
                    }
                }
            }
        }
    }

    // Pre-define new views before setting next question as current question, for index > list.size()
    public void onButtonNext(View view) {
        Quiz currentQuestion = quizList.get(indexCurrentQuestion);
        if(currentQuestion.userAnswer != currentQuestion.correctAnswer) life -= 1;
        if(life == 0){
            Toast.makeText(getApplicationContext(), "failed ", Toast.LENGTH_SHORT).show();
            countDownTimer.cancel();
            finish();
        }
        if(indexCurrentQuestion != (quizList.size() - 1)) {
            indexCurrentQuestion++;
            currentQuestion = quizList.get(indexCurrentQuestion);
            if(indexCurrentQuestion == (quizList.size() - 1)) buttonNext.setEnabled(false);
            if(indexCurrentQuestion != 0) buttonPrevious.setEnabled(true);
            currentQuestionView(currentQuestion);

            radioGroup = findViewById(R.id.celebrityOption);
            if(currentQuestion.userAnswer == 0) radioGroup.clearCheck();
            else {
                switch (currentQuestion.userAnswer) {
                    case 1: {
                        radioGroup.check(R.id.radioButtonOne);
                        break;
                    }
                    case 2: {
                        radioGroup.check(R.id.radioButtonTwo);
                        break;
                    }
                    case 3: {
                        radioGroup.check(R.id.radioButtonThree);
                        break;
                    }
                    case 4: {
                        radioGroup.check(R.id.radioButtonFour);
                        break;
                    }
                }
            }
        }
    }

    public void currentQuestionView(Quiz currentQuestion) {
        questionView.setText(String.format("%s. %s", indexCurrentQuestion + 1, currentQuestion.question));
        radioButtonOne.setText(currentQuestion.one);
        radioButtonTwo.setText(currentQuestion.two);
        radioButtonThree.setText(currentQuestion.three);
        radioButtonFour.setText(currentQuestion.four);
        Glide.with(imageView.getContext()).load(currentQuestion.imageUrl).into(imageView);
    }

    // Calculate score
    public int getScore() {
        int score = 0;
        for (int i = 0; i < quizList.size(); i++) {
            if (quizList.get(i).userAnswer == quizList.get(i).correctAnswer) score++;
        }
        return score;
    }


    //힌트

    public void hintClick(View view) {
        Intent intent = getIntent();
        int level = intent.getIntExtra("level", 0);
        if(level == 1) {
            Toast.makeText(getApplicationContext(), hintbox1[indexCurrentQuestion], Toast.LENGTH_SHORT).show();
        }
        else if(level == 2) {
            Toast.makeText(getApplicationContext(), hintbox2[indexCurrentQuestion], Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), hintbox3[indexCurrentQuestion], Toast.LENGTH_SHORT).show();//indexCurrentQustion 현재 순번
        }
    }
}
