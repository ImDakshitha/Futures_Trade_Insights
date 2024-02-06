package com.example.cryptofuturestrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class APIConfig extends AppCompatActivity {
    private Button mBtnSetAPI,mBtnEditAPI;
    private TextInputLayout mEtAPIKey,mEtSecretKey;
    private TextView mTvCheckApi,mTvCheckSecret;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseUser firebaseuser,loggedUser;
    private DatabaseReference rootReference;

    private BottomNavigationView bottomNavigationView;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiconfig);



        mEtAPIKey=findViewById(R.id.input_api_key);
        mEtSecretKey=findViewById(R.id.input_secret_key);
        mBtnSetAPI=findViewById(R.id.btn_add_api);
        mBtnEditAPI=findViewById(R.id.btn_edit_api);


        bottomNavigationView =findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_api);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_api:
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),NewMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_trades:
                        startActivity(new Intent(getApplicationContext(),ShowTrades.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_diary:
                        startActivity(new Intent(getApplicationContext(),DiaryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        mFireBaseAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance("https://trading-app-43dfe-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        mBtnSetAPI.setOnClickListener(view -> {
            String api_key = mEtAPIKey.getEditText().getText().toString();
            String secret_key = mEtSecretKey.getEditText().getText().toString();
            firebaseuser = mFireBaseAuth.getCurrentUser();


            APIDetails APIInsertObj = new APIDetails(api_key, secret_key);

            if (!api_key.isEmpty()&&!secret_key.isEmpty()) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("API Key", api_key);
                childUpdates.put("Secret Key", secret_key);

                rootReference.child(firebaseuser.getUid()).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Values Stored in DB", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(APIConfig.this, "Error:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(APIConfig.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(APIConfig.this, "Please Enter both API and Secret Keys", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnEditAPI.setOnClickListener(view -> {
            String api_key = mEtAPIKey.getEditText().getText().toString();
            String secret_key = mEtSecretKey.getEditText().getText().toString();
            firebaseuser = mFireBaseAuth.getCurrentUser();

            APIDetails APIInsertObj = new APIDetails(api_key, secret_key);

            if (!api_key.isEmpty()&&!secret_key.isEmpty()) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("API Key", api_key);
                childUpdates.put("Secret Key", secret_key);

                rootReference.child(firebaseuser.getUid()).updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Values Stored in DB", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(APIConfig.this, "Error:" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(APIConfig.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(APIConfig.this, "Please Enter both API and Secret Keys", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser mFirebaseUser = mFireBaseAuth.getCurrentUser();
        mTvCheckApi = findViewById(R.id.tv_show_api);
        mTvCheckSecret=  findViewById(R.id.tv_show_secret);

            if (mFirebaseUser != null) {
                reference = FirebaseDatabase.getInstance("https://trading-app-43dfe-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child(mFirebaseUser.getUid());

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("API Key").exists()&&snapshot.child("Secret Key").exists()){
                            String apikey = snapshot.child("API Key").getValue(String.class);
                            String secretkey = snapshot.child("Secret Key").getValue(String.class);
                            mTvCheckApi.setVisibility(View.VISIBLE);
                            mTvCheckSecret.setVisibility(View.VISIBLE);
                            mTvCheckApi.setText("You Have Added an API KEY ");
                            mTvCheckSecret.setText("You Have Added a Secret KEY");
                            mBtnSetAPI.setVisibility(View.INVISIBLE);
                            mBtnEditAPI.setVisibility(View.VISIBLE);
                            Toast.makeText(APIConfig.this, "API and Secret Key exists", Toast.LENGTH_SHORT).show();

                        }else{
                            mTvCheckApi.setText("You have not added the APi key");
                            mTvCheckSecret.setText("You have not added the Secret key");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            else {
                Toast.makeText(APIConfig.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, NewLogin.class));
                finish();
            }
        }
    }
