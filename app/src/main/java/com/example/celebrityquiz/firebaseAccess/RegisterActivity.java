package com.example.celebrityquiz.firebaseAccess;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.celebrityquiz.MainActivity;
import com.example.celebrityquiz.R;
import com.example.celebrityquiz.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username,email, password;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;
    DatabaseReference referenceforRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String txt_username = username.getText().toString();
                String txt_emall = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_emall) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, "All files are required" ,Toast.LENGTH_SHORT).show();
                }else if(txt_password.length() < 6){
                    Toast.makeText(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }else{
                    register(txt_username, txt_emall, txt_password);
                }
            }
        });

    }

    private void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, "You can't register with this email or password ", Toast.LENGTH_SHORT).show();
                            Log.d("register exception", "onComplete: Failed=" + task.getException().getMessage());
                        }else{
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            referenceforRecord = FirebaseDatabase.getInstance().getReference("Records").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            HashMap<String, String> recordHashMap = new HashMap<>();
//                            recordHashMap.put("id", userid);
                            recordHashMap.put("username", username);
                            recordHashMap.put("elapsedTime", "0");
                            recordHashMap.put("totalQuizNum", "0");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
                                    }else{
                                        Toast.makeText(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, "reference error ", Toast.LENGTH_SHORT).show();
                                        Log.d("reference exception", "onComplete: Failed=" + task.getException().getMessage());
                                    }
                                }
                            });
                            //set default record
                            referenceforRecord.setValue(recordHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(com.example.celebrityquiz.firebaseAccess.RegisterActivity.this, "reference error ", Toast.LENGTH_SHORT).show();
                                        Log.d("reference exception", "onComplete: Failed=" + task.getException().getMessage());
                                    }
                                }
                            });

                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
