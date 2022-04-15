package com.example.healthx;
//Required Imports
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
public class Profile extends AppCompatActivity {
    // Required Variable(s)
    TextView fullName, email,phone,bigName;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Variables connected to resources on.xml file
        fullName = findViewById(R.id.profileName);
        phone = findViewById(R.id.profilePhone);
        email = findViewById(R.id.profileEmail);
        bigName = findViewById(R.id.bigname);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance(); //Retrieving the current instance of the database from Firebase Firestore
        userId = fAuth.getCurrentUser().getUid(); //Retrieving  current user from Firebase Authentication and obtaining user ID

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            // 'onEvent' method retrieves a snapshot of the user details and will display them in the TextView
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                bigName.setText(documentSnapshot.getString("fName"));
                phone.setText(documentSnapshot.getString("phone") );
                fullName.setText(documentSnapshot.getString("fName"));
                email.setText(documentSnapshot.getString("email"));
            }
        });
    }
    // The 'logout' method signs the user out of the application and sends them to the 'Login' class
    public void  logout (View view) {
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();

    }
    // The 'enter' method starts 'HomePage' class method and sends users to Home page
    public void  enter (View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}