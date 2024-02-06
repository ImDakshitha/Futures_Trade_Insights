package com.example.cryptofuturestrader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewSignUp extends AppCompatActivity {

    private TextInputLayout mEtFname,mEtEmail,mEtPassword,mEtRepeatPassword;
    private Button mBtnSignUp;
    private TextView mBtnLogin;
    private FirebaseAuth mFireBaseAuth;

    private FirebaseUser firebaseuser;
    private ProgressDialog dialog;

    private DatabaseReference rootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signup_new);

        mEtFname=findViewById(R.id.input_name_signup);
        mEtEmail =findViewById(R.id.input_sign_in_email);
        mEtPassword=findViewById(R.id.input_sign_in_password);
        mEtRepeatPassword=findViewById(R.id.input_sign_in_repeat_password);

        mBtnSignUp=findViewById(R.id.btn_signup_new);

//        dialog = new ProgressDialog(this);


        mFireBaseAuth =FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance("https://trading-app-43dfe-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        mBtnSignUp.setOnClickListener(view -> {
            String fname = mEtFname.getEditText().getText().toString();
            String email = mEtEmail.getEditText().getText().toString();
            String password = mEtPassword.getEditText().getText().toString();
            String repeatPassword = mEtRepeatPassword.getEditText().getText().toString();

            if(!email.isEmpty()&&!password.isEmpty()&&!repeatPassword.isEmpty()){
                if (password.equals(repeatPassword)) {
                    if(password.length()>6){

                        mFireBaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    firebaseuser = mFireBaseAuth.getCurrentUser();

                                    User myUserInsertObj = new User(email,fname);
                                    rootReference.child(firebaseuser.getUid()).setValue(myUserInsertObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Values Stored in DB", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                                    startActivity(new Intent(NewSignUp.this, NewMainActivity.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(NewSignUp.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewSignUp.this,"Error:"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                Toast.makeText(NewSignUp.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }else{
                        Toast.makeText(NewSignUp.this, "Password Must be 6 digits", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                }
            }
            else{
                //No Blank Fields
                Toast.makeText(NewSignUp.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();

            }

        });
        mBtnLogin = findViewById(R.id.btn_signup_login);
        mBtnLogin.setOnClickListener(view -> startActivity(new Intent(NewSignUp.this,NewLogin.class)));


    }

}