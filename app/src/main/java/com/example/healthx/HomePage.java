package com.example.healthx;
//Required imports
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    // Required Relative Layout Variable(s)

    RelativeLayout wR1,fR2,aR3,gR4,pR5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // Variables connected to resources on.xml file
        wR1=findViewById(R.id.wR1);
        fR2=findViewById(R.id.fR2);
        aR3=findViewById(R.id.aR3);
        gR4=findViewById(R.id.gR4);
        pR5=findViewById(R.id.pR5);

       // When user clicks on WR1 button, start  'WeightLogActivity' class
        wR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WeightLogActivity.class));
            }
        });
        // When user clicks on WR1 button, start  'FoodLogActivity' class
        fR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FoodLogActivity.class));
            }
        });
        // When user clicks on aR3 button, start  'StepCounter' class
        aR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StepCounter.class));
            }
        });
        // When user clicks on gR4 button, start  'GoalsLogActivity' class
        gR4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GoalsLogActivity.class));
            }
        });
        // When user clicks on PR5 button, send user back to profile page
        pR5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }

}