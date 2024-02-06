package com.example.cryptofuturestrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class MainActivity extends AppCompatActivity {
        TextView tvAccountBal,tvTotalPnl;
        DatabaseReference reference;
        FirebaseUser mFirebaseUser;
        FirebaseAuth mFireBaseAuth;

        String secret_key, url,api_key;

        List<Balances> balancesList = new ArrayList<>();
        HashMap parameters;

        MaterialButtonToggleGroup toggleButton;
        long start_dt;
        long end_dt;

        private BottomNavigationView bottomNavigationView;
        private MutableLiveData<String> secretkeyLiveData;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bottomNavigationView = findViewById(R.id.bottom_navigator);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            tvAccountBal = findViewById(R.id.main_acc_balance);
            tvTotalPnl=findViewById(R.id.main_tv_show_pnl);
            toggleButton = findViewById(R.id.main_toggleButton);
            secretkeyLiveData = new MutableLiveData<>();


            mFireBaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFireBaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                reference = FirebaseDatabase.getInstance("https://trading-app-43dfe-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child(mFirebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.child("Secret Key").exists()&&snapshot.child("API Key").exists()) {
                                secret_key = snapshot.child("Secret Key").getValue(String.class);
                                api_key = snapshot.child("API Key").getValue(String.class);

                                System.out.println("ON Create" + secret_key);
                                secretkeyLiveData.setValue(secret_key);

                            } else {
                                Toast.makeText(com.example.cryptofuturestrader.MainActivity.this, "Set up API key and Secret Key", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(com.example.cryptofuturestrader.MainActivity.this, "Log in first", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), NewLogin.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            } else {
            }
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_api:
                            startActivity(new Intent(getApplicationContext(), APIConfig.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_profile:
                            return true;
                        case R.id.nav_trades:
                            startActivity(new Intent(getApplicationContext(), ShowTrades.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_home:
                            startActivity(new Intent(getApplicationContext(), com.example.cryptofuturestrader.MainActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.nav_diary:
                            startActivity(new Intent(getApplicationContext(),DiaryActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });
        }
        private long getTimeStamp() {
            long timestamp = System.currentTimeMillis();
            return timestamp;
        }

        private void calcPnl(long start,long end) throws Exception {
            super.onStart();

            GetParams getParams = new GetParams();
            HashMap parameters;
            parameters = getParams.grabParams("",start,end);
            Request request = new Request();
            String url = request.sendSignedRequest(parameters,secret_key);

            RetrofitClient.getRetrofitClient().getTrades(api_key,"/fapi/v1/userTrades?"+url).enqueue(new Callback<List<Trades>>() {
                @Override
                public void onResponse(Call<List<Trades>> call, Response<List<Trades>> response) {
                    if(response.isSuccessful()&&response.body() != null) {
                        List<Trades> allTrades = response.body();
                        //allCoins.getAsset().equals("USDT");
                        Float tot_pnl = 0.000000f;
                        for (int i = 0; i < allTrades.size(); i++) {
                            Float pnl = Float.parseFloat(allTrades.get(i).getRealizedPnl());
                            tot_pnl = tot_pnl + pnl;
                            tvTotalPnl.setText(String.valueOf(tot_pnl));
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Trades>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", t.getMessage());
                }

                });
        }

        @Override
        protected void onStart() {
            super.onStart();
            Request request = new Request();
            GetParams getParams = new GetParams();
            try {
                parameters = getParams.grabURL();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                secretkeyLiveData.observe(this, (secret_key) -> {
                    try {
                        url = request.sendSignedRequest(parameters, secret_key);
                        RetrofitClient.getRetrofitClient().getBalance(api_key,"/fapi/v2/balance?" + url).enqueue(new Callback<List<Balances>>() {
                            @Override
                            public void onResponse(Call<List<Balances>> call, Response<List<Balances>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    List<Balances> allCoins = response.body();
                                    //allCoins.getAsset().equals("USDT");
                                    for (int i = 0; i < allCoins.size(); i++) {
                                        if (allCoins.get(i).getAsset().equals("USDT" +
                                                "")) {
                                            String value = allCoins.get(i).getBalance();
                                            String fvalue= String.format("%.5g%n",Float.parseFloat(value));
                                            tvAccountBal.setText(fvalue);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<Balances>> call, Throwable t) {
                            }

                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //toggle button
            int buttonId = toggleButton.getCheckedButtonId();
            MaterialButton button = toggleButton.findViewById(buttonId);


            toggleButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (isChecked) {

                        if (checkedId == R.id.button1) {
                            long now = getTimeStamp();
                            start_dt = now-14400000l;
                            try {
                                calcPnl(start_dt,now);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(com.example.cryptofuturestrader.MainActivity.this, "4h ", Toast.LENGTH_SHORT).show();
                        }
                        if (checkedId == R.id.button2) {
                            long now = getTimeStamp();
                            start_dt = now-86400000l;
                            try {
                                calcPnl(start_dt,now);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(com.example.cryptofuturestrader.MainActivity.this, "1D ", Toast.LENGTH_SHORT).show();
                        }
                        if (checkedId == R.id.button3) {
                            long now = getTimeStamp();
                            start_dt = now-604800000l;
                            try {
                                calcPnl(start_dt,now);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(com.example.cryptofuturestrader.MainActivity.this, "7D", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }