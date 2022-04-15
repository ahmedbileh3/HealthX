package com.example.healthx;
// Required Imports
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodLogActivity extends AppCompatActivity {
    // Required Variable(s)
    EditText wEt, hEt, dEt;
    TextView wTv, hTv, dTv;
    DatePickerDialog.OnDateSetListener mDatesetListener;
    Button saveBtn, loadBtn;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_log);

        wEt = findViewById(R.id.wEt);
        hEt = findViewById(R.id.hEt);
        dEt = findViewById(R.id.dEt);
        saveBtn = findViewById(R.id.saveBtn);
        loadBtn = findViewById(R.id.loadBtn);
        wTv = findViewById(R.id.wTv);
        hTv = findViewById(R.id.hTv);
        dTv = findViewById(R.id.dTv);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new SweetAlertDialog(FoodLogActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00ffff"));
                pDialog.setTitleText("Loading Logs.");
                pDialog.setContentText("Please wait !");
                pDialog.setCancelable(false);
                if (pDialog != null)
                    pDialog.show();
                getExistingLogs();
            }
        });

        dEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialogue();

            }
        });

        // Method for getting date changes on date chooser dialog

        mDatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                i1++;
                dEt.setText(i2 + "/" + i1 + "/" + i);

            }
        };
    }

    // Method for fetching existing Logs


    private void getExistingLogs() {
        FirebaseFirestore.getInstance().collection("FoodLogs").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pDialog.dismissWithAnimation();
                wTv.setText("Calories : " + documentSnapshot.getString("calories") + " kcal");
                hTv.setText("Water Intake : " + documentSnapshot.getString("water intake")+ "cl");
                dTv.setText("Date   : " + documentSnapshot.getString("date"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismissWithAnimation();
                Toast.makeText(FoodLogActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method for validating user input

    private void validateData() {

        if (wEt.getText().toString().length() < 1) {
            wEt.setError("Make sure to Enter Calories!");
            return;
        }

        if (hEt.getText().toString().length() < 1) {
            hEt.setError("This field must be filled");
            return;
        }

        if (dEt.getText().toString().length() < 1) {
            dEt.setError("This field must be filled");
            return;
        }

        saveData();
    }


    // Method for saving user record in firestore database

    private void saveData() {

        pDialog = new SweetAlertDialog(FoodLogActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#00ffff"));
        pDialog.setTitleText("Saving Logs.");
        pDialog.setContentText("Please wait !");
        pDialog.setCancelable(false);
        if (pDialog != null)
            pDialog.show();

        Map wieghtMAp = new HashMap();
        wieghtMAp.put("calories", wEt.getText().toString());
        wieghtMAp.put("water intake", hEt.getText().toString());
        wieghtMAp.put("date", dEt.getText().toString());
        FirebaseFirestore.getInstance().collection("FoodLogs").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(wieghtMAp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                pDialog.setTitleText("Congratulations !")
                        .setContentText("Your Food logs has been updated successfully.").setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        pDialog.dismissWithAnimation();

                    }
                })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismissWithAnimation();
                Toast.makeText(FoodLogActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDatePickerDialogue() {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(FoodLogActivity.this, android.R.style.Theme_DeviceDefault_Dialog, mDatesetListener, year, month, day);

        //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        datePickerDialog.show();
    }

}

