package com.example.healthx;
// Required Imports
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    // Required Variable(s)
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn, forgotPasswordLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Variables connected to resources on.xml file
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.button2);
        mRegisterBtn = findViewById(R.id.textView4);
        forgotPasswordLink = findViewById(R.id.forgotPassword);

        // user login
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                // error message if email is not entered
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                // error message if password is not entered
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }
                // error message if password is less than 7 characters
                if (password.length() < 7) {
                    mPassword.setError("Password Must be >= 7 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticating the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Toast message is sent stating that the user is logged in
                            Toast.makeText(Login.this, "User is logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            // Toast message is sent if there was an error found during login
                        } else {
                            Toast.makeText(Login.this, "Error !!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                });


            }

        });
        // If user has not created an account, 'onClick' methods starts the activity 'CreateAccount' and sends user to register page
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateAccount.class));
            }
        });
        // If user forgot password and clicks on 'forgotPasswordLink' variable the 'onClick' method is initiated
        forgotPasswordLink.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Your Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the email and send reset link to the chosen email
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            //if user formatted/inputted the email address correctly, display successful toast message
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                            //if there is an indent during input of email address, the error message will be toasted
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "ERROR!!! Reset Link Is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                // if user chooses to enter 'No' during password reset, close the dialog
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();


            }
        }));


    }


}