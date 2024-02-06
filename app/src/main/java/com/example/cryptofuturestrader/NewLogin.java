package com.example.cryptofuturestrader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewLogin extends AppCompatActivity {

    private Button mBtnLogIn;
    private TextView mRlSignUp;
    private TextInputLayout mEtEmail,mEtPassword;
    private FirebaseAuth mFireBaseAuth ;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_new);

        mEtEmail=findViewById(R.id.input_login_email);
        mEtPassword=findViewById(R.id.input_login_password);
        mBtnLogIn=findViewById(R.id.btn_go);

        mFireBaseAuth = FirebaseAuth.getInstance();

        mBtnLogIn.setOnClickListener((view -> {
            String email = mEtEmail.getEditText().getText().toString();
            String password =mEtPassword.getEditText().getText().toString();

            if(!email.isEmpty()&&!password.isEmpty()){
                mFireBaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(NewLogin.this,NewMainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(NewLogin.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewLogin.this, "Error:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(NewLogin.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(NewLogin.this, "Please Enter Values", Toast.LENGTH_SHORT).show();
            }

        }));

        mRlSignUp = findViewById(R.id.btn_login_signup);
        mRlSignUp.setOnClickListener(view -> startActivity(new Intent(NewLogin.this,NewSignUp.class)));

    }

}