package com.example.cryptofuturestrader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTrades extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    PostAdapter adapter;
    DatabaseReference reference;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFireBaseAuth;
    List<Trades> tradeList = new ArrayList<>();
    Spinner spinner;
    String secret_key,api_key;
    TextView BtnView,PNL;
    Button datePickerBtn;

    Long startDate ;
    Long endDate ,dateDifferernce;

    String selected_string;

    private BottomNavigationView bottomNavigationView;
    ArrayAdapter<CharSequence> adapter_spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_trades);
        bottomNavigationView =findViewById(R.id.bottom_navigator);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        BtnView = findViewById(R.id.btn_view);
        spinner = findViewById(R.id.pair_select);

        datePickerBtn = findViewById(R.id.date_picker);
        PNL=findViewById(R.id.tv_pnl_all);

        //recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostAdapter(tradeList,PNL);
        recyclerView.setAdapter(adapter);

        //spinner

        adapter_spinner= ArrayAdapter.createFromResource(this,R.array.pairs, android.R.layout.simple_spinner_item);
        adapter_spinner.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner.setAdapter(adapter_spinner);


        mFireBaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFireBaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            reference = FirebaseDatabase.getInstance("https://trading-app-43dfe-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child(mFirebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if(snapshot.child("Secret Key").exists()&&snapshot.child("API Key").exists()) {
                            secret_key = snapshot.child("Secret Key").getValue(String.class);
                            api_key = snapshot.child("API Key").getValue(String.class);
                            System.out.println(secret_key);
                        }
                        else {
                            Toast.makeText(ShowTrades.this, "Set up API key and Secret Key", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ShowTrades.this, "Log in first", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),NewLogin.class));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
        else {
        }

        //bottom navigation bar
        bottomNavigationView.setSelectedItemId(R.id.nav_trades);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_api:
                        startActivity(new Intent(getApplicationContext(),APIConfig.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),NewMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_trades:
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


        // Setting click listener for the date picker button
        datePickerBtn.setOnClickListener(view -> DatePickerdialog());

    }

    private void DatePickerdialog() {
        // Creating a MaterialDatePicker builder for selecting a date range
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        Date date = calendar.getTime();
        long mills = date.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
        String dateOutput = format.format(date);
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.from(mills);
        constraintsBuilder.setValidator(dateValidator);
        builder.setCalendarConstraints(constraintsBuilder.build());
        builder.setCalendarConstraints(constraintsBuilder.build());


        // Building the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Retrieving the selected start and end dates
            startDate = selection.first;
            endDate = selection.second;
            dateDifferernce =endDate-startDate;

            // Formating the selected dates as strings
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            // Creating the date range string
            String selectedDateRange = startDateString + " - " + endDateString;

            // Displaying the selected date range in the TextView
            datePickerBtn.setText(selectedDateRange);
        });

        // Showing the date picker dialog
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

    }


    @Override
    protected void onStart() {
        super.onStart();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0||position==1){
                    selected_string="";
                }else{
                    selected_string = adapterView.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        BtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDate==null && endDate==null){
                    Toast.makeText(ShowTrades.this, "Please Select the Date Range", Toast.LENGTH_SHORT).show();
                }
                if (selected_string==null){
                    Toast.makeText(ShowTrades.this, "Please Select a Trading Pair", Toast.LENGTH_SHORT).show();
                }
                if(startDate!= null && endDate!=null && selected_string!=null){
                    if(dateDifferernce>=604800000){
                        Toast.makeText(ShowTrades.this, "Maximum Date DIfference is 7 days", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            fetchPosts(selected_string,startDate,endDate);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void fetchPosts(String symbol,long start,long end) throws Exception {

        GetParams getParams = new GetParams();
        HashMap parameters;
        parameters = getParams.grabParams(symbol,start,end);
        Request request = new Request();
        String url = request.sendSignedRequest(parameters,secret_key);

        RetrofitClient.getRetrofitClient().getTrades(api_key,"/fapi/v1/userTrades?"+url).enqueue(new Callback<List<Trades>>() {
            @Override
            public void onResponse(Call<List<Trades>> call, Response<List<Trades>> response) {
                if(response.isSuccessful()&&response.body() != null){
                    tradeList.clear();
                    tradeList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility((View.GONE));
                }

            }

            @Override
            public void onFailure(Call<List<Trades>> call, Throwable t) {
                Toast.makeText(ShowTrades.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", t.getMessage());
            }
        });
    }
}