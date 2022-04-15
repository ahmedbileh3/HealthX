package com.example.healthx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Required Variable(s)
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Variable connected to resources on.xml file
        button = (Button) findViewById(R.id.button);


        // When 'button' variable is clicked, open method 'openCreateAccount'
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openCreateAccount();
            }
        });

    }
    // openCreateAccount'method starts 'CreateAccount' activity using Intent
    public void openCreateAccount() {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);

    }
}