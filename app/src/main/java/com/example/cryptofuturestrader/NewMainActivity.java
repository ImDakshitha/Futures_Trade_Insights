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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NewMainActivity extends AppCompatActivity {
    private FirebaseAuth mFireBaseAuth;
    private TextView mTvUser;
    private Button mBtnLogOut,mBtnAPI;
    private DatabaseReference reference;
    private FirebaseUser loggedUser;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_new);
        bottomNavigationView =findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_api:
                        startActivity(new Intent(getApplicationContext(),APIConfig.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
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
        mTvUser =findViewById(R.id.tv_fname);
        mBtnLogOut=findViewById(R.id.btn_logout_new);
        loggedUser = mFireBaseAuth.getCurrentUser();



        mBtnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFireBaseAuth.signOut();
                Intent intent = new Intent(NewMainActivity.this, NewLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mFirebaseUser = mFireBaseAuth.getCurrentUser();
        if(mFirebaseUser!=null){
            reference = FirebaseDatabase.getInstance("https://trading-app-43dfe-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child(loggedUser.getUid());
            String logged_uid = loggedUser.getUid();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String fname = snapshot.child("fname").getValue(String.class);
                    mTvUser.setText(fname);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        else {
            Toast.makeText(NewMainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NewMainActivity.this, NewLogin.class));

        }
    }

}