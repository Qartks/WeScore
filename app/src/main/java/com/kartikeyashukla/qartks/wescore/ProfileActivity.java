package com.kartikeyashukla.qartks.wescore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private Button profileLogOut;
    private TextView profileEmail;
    private EditText profileName;
    private EditText profileHeadline;
    private Button profileSave;

    private FirebaseAuth fbAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser loggedInUser = fbAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        profileLogOut = (Button) findViewById(R.id.profile_logout);
        profileEmail = (TextView) findViewById(R.id.profile_email);
        profileEmail.setText(new StringBuilder().append("Hi There, ").append(loggedInUser.getEmail()).toString());

        profileName = (EditText) findViewById(R.id.profile_name);
        profileHeadline = (EditText) findViewById(R.id.profile_headline);
        profileSave = (Button) findViewById(R.id.profile_save);

        profileSave.setOnClickListener(this);
        profileLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == profileLogOut) {
            fbAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (v == profileSave) {
            saveProfileInfo();
        }
    }

    private void saveProfileInfo() {
        FirebaseUser user = fbAuth.getCurrentUser();

        String name = profileName.getText().toString().trim();
        String headline = profileHeadline.getText().toString().trim();

        ProfileData data = new ProfileData(name, headline);

        dbReference.child(user.getUid()).setValue(data);
        Toast.makeText(this, "Info Saved", Toast.LENGTH_SHORT).show();
    }
}
