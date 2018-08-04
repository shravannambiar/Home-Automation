package com.example.shravan.techhome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

import android.util.Log;


public class LoginPage extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private EditText editTextusername,editTextpassword;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String TAG = "LoginPage";
    private Button login;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        progressDialog=new ProgressDialog(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        editTextusername=(EditText)findViewById(R.id.editTextUsername);
        editTextpassword= (EditText) findViewById(R.id.editTextPassword);
        firebaseAuth=FirebaseAuth.getInstance();
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        }





    private void Login(){
        String email=editTextusername.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Provide an email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Provide an password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Connecting to Firebase");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            final String id=firebaseAuth.getCurrentUser().getUid();
                            databaseReference= firebaseDatabase.getReference("Users").child(id);
                            databaseReference.addValueEventListener(new ValueEventListener() {


                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    userInfo=dataSnapshot.getValue(UserInfo.class);
                                    databaseReference.removeEventListener(this);
                                    progressDialog.dismiss();
                                    gotonext();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Details are incorrect",Toast.LENGTH_LONG).show();
                        }
                    }
                });






    }

    @Override
    public void onClick(View v) {
        Login();
    }

    public void gotonext(){
        finish();
        Intent intent=new Intent(this,Device_List.class);
        intent.putExtra("userinfo",userInfo);
        startActivity(intent);
    }
}
