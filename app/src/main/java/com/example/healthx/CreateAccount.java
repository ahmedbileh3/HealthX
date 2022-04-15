package com.example.healthx;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
    // Required Variable(s)
    public static final String TAG = "TAG";
    EditText cFullName, cEmail, cPassword, cPhone;
    Button cRegisterBtn;
    TextView cLoginBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Variables connected to resources on.xml file
        cFullName    = findViewById(R.id.PersonName);
        cEmail       = findViewById(R.id.Email);
        cPhone       = findViewById(R.id.phoneNumber);
        cPassword    = findViewById(R.id.Password);
        cRegisterBtn = findViewById(R.id.button2);
        cLoginBtn    = findViewById(R.id.textView3);

        fAuth = FirebaseAuth.getInstance(); //Retrieving the current instance of the database from Firebase Authentication
        fStore = FirebaseFirestore.getInstance(); //Retrieving the current instance of the database from Firebase Firestore
        progressBar = findViewById(R.id.progressBar);

        // If user is authenticated, send them to the profile page
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),Profile.class));
            finish();
        }
        // When Login button is clicked, method 'onClick' opens the 'openLogin' method
        cLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        cRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = cEmail.getText().toString().trim();
                String password = cPassword.getText().toString().trim();
                String fullName = cFullName.getText().toString();
                String phone    = cPhone.getText().toString();
                //If text field for email is empty, return the error message
                if(TextUtils.isEmpty(email)) {
                    cEmail.setError("Email is Required");
                    return;
                }
                // If text field for phone number is empty, return the error message
                if(TextUtils.isEmpty(password)) {
                    cPhone.setError("Phone Number is Required");
                    return;
                }
                // If text field for password is empty, return the error message
                if(TextUtils.isEmpty(password)) {
                    cPassword.setError("Password is Required");
                    return;
                }
                // If password length is less than 7 characters display error message
                if(password.length() < 7) {
                    cPassword.setError("Password Must be more than 7 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // Registering User to Firebase Authentication
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    // 'onComplete' method will toast successful message if users was created and store in firebase authentication
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", fullName);
                            user.put("email", email);
                            user.put("phone", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User Profile is created for "+userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),Login.class));

                            //If user makes a mistake during registration, toast error text and get error message
                        }else {
                            Toast.makeText(CreateAccount.this, "Error !!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    // 'openLogin' method starts 'Login' class method and sends users to that activity
    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
