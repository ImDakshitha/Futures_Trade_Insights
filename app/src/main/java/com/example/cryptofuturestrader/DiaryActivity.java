package com.example.cryptofuturestrader;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.Query;


public class DiaryActivity extends AppCompatActivity {
    private FloatingActionButton fbtn;
    private BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    DiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        fbtn=findViewById(R.id.floating_ab);
        fbtn.setOnClickListener(view -> startActivity(new Intent(DiaryActivity.this,DiaryDetails.class)) );

        //bottom navigation bar
        bottomNavigationView =findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_diary);
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
                        startActivity(new Intent(getApplicationContext(),ShowTrades.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_diary:
                        return true;
                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recyclerView_diary);
        setupRecyclerView();
    }

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForDiary();
                //.orderBy("EntryPrice",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Diary> options = new FirestoreRecyclerOptions.Builder<Diary>()
                .setQuery(query,Diary.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryAdapter = new DiaryAdapter(options,this);
        recyclerView.setAdapter(diaryAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        diaryAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        diaryAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        diaryAdapter.notifyDataSetChanged();
    }
}