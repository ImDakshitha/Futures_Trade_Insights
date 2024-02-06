package com.example.cryptofuturestrader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DiaryDetails extends AppCompatActivity {

    TextView title;
    EditText entryPrice,exitPrice,qty;
    Button selectDateButton,selectTimeButton,addTradeButton,deleteDiaryButton;
    MaterialButtonToggleGroup  directionButton;
    Spinner spinnerPair;
    ArrayAdapter<CharSequence> adapter_spinner;
    String selected_string;
    String selected_date,selected_time,entryPriceStr,exitPriceStr,quantityStr,direction;

    String pair,direct,entry,exit,edit_qty,date,time,docId;

    boolean isEditMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);
        entryPrice=findViewById(R.id.et_entry_price);
        exitPrice =findViewById(R.id.et_exit_price);
        qty=findViewById(R.id.et_qty);
        selectDateButton=findViewById(R.id.btn_diary_date);
        selectTimeButton=findViewById(R.id.btn_diary_time);
        addTradeButton=findViewById(R.id.btn_add_trade);
        directionButton=findViewById(R.id.toggleButton);
        title=findViewById(R.id.text_title_dd);
        deleteDiaryButton=findViewById(R.id.btn_delete_trade);

        spinnerPair = findViewById(R.id.select_pair);

        //receive data
        pair=getIntent().getStringExtra("pair");
        direct=getIntent().getStringExtra("direction");
        entry=getIntent().getStringExtra("entryPrice");
        exit=getIntent().getStringExtra("exitPrice");
        date=getIntent().getStringExtra("date");
        time=getIntent().getStringExtra("time");
        edit_qty=getIntent().getStringExtra("qty");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null&&!docId.isEmpty()){
            isEditMode=true;
        }

        directionButton.setSelected(false);
        spinnerPair.setSelection(0);
        entryPrice.setText(entry);
        exitPrice.setText(exit);
        qty.setText(edit_qty);

        if(isEditMode){
            title.setText("EDIT TRADE");
            selectDateButton.setText(date);
            selected_date=date;
            selectTimeButton.setText(time);
            selected_time=time;
            deleteDiaryButton.setVisibility(View.VISIBLE);
        }
        addTradeButton.setOnClickListener((view -> addTrade()));
        deleteDiaryButton.setOnClickListener((view -> deleteDiaryFromFirestore()));

        //spinner
        adapter_spinner= ArrayAdapter.createFromResource(this,R.array.pairs, android.R.layout.simple_spinner_item);
        adapter_spinner.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinnerPair.setAdapter(adapter_spinner);

        //Date Picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");

        MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        selectDateButton.setOnClickListener(v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER")
        );
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selected_date=materialDatePicker.getHeaderText();
                selectDateButton.setText(selected_date);
            }
        });

        //toggle button
        int buttonId = directionButton.getCheckedButtonId();
        MaterialButton button = directionButton.findViewById(buttonId);


        directionButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.buttonLong) {
                        direction="LONG";
                        Toast.makeText(DiaryDetails.this, "LONG ", Toast.LENGTH_SHORT).show();
                    }
                    if (checkedId == R.id.buttonShort) {
                        direction="SHORT";
                        Toast.makeText(DiaryDetails.this, "SHORT ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //time picker
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepicker();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        spinnerPair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_string = adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void addTrade(){
        entryPriceStr = entryPrice.getText().toString();
        exitPriceStr =exitPrice.getText().toString();
        quantityStr=qty.getText().toString();

        if(direction==null||entryPriceStr==null||exitPriceStr==null||quantityStr==null||selected_string==null||selected_date==null||selected_time==null){
            Toast.makeText(DiaryDetails.this, "Please enter all the data", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Diary diaryEntry = new Diary();
            diaryEntry.setPair(selected_string);
            diaryEntry.setEntryPrice(entryPriceStr);
            diaryEntry.setExitPrice(exitPriceStr);
            diaryEntry.setQty(quantityStr);
            diaryEntry.setDate(selected_date);
            diaryEntry.setTime(selected_time);
            diaryEntry.setDirection(direction);

            saveDiarytoFirestore(diaryEntry);
        }
    }
    void timepicker(){
        //time picker
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select Start Time")
                .setHour(12)
                .setMinute(10)
                .build();
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_time = String.valueOf(picker.getHour())+":"+String.valueOf(picker.getMinute());
                selectTimeButton.setText(selected_time);
            }
        });

        picker.show(getSupportFragmentManager(), "starttime");

    }
    void saveDiarytoFirestore(Diary diary){
        DocumentReference documentReference;
        if(isEditMode){
            documentReference= Utility.getCollectionReferenceForDiary().document(docId);
        }else{
            documentReference= Utility.getCollectionReferenceForDiary().document();
        }

        documentReference.set(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DiaryDetails.this, "Trade Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(DiaryDetails.this, "Failed to add Trade", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }
    void deleteDiaryFromFirestore(){
        DocumentReference documentReference;
        documentReference= Utility.getCollectionReferenceForDiary().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DiaryDetails.this, "Trade Deleted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DiaryDetails.this, "Failed to Delete the Trade", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}