package com.kartikeyashukla.qartks.wescore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";

    private Button profileLogOut;
    private TextView profileEmail;

    private TextView myScore;
    private Button submitScore;

    private FirebaseAuth fbAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private FirebaseUser loggedInUser;

    private SeekBar scorebar;
    private long scoreAchieved;
    private long scoreGiven;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        loggedInUser = fbAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        profileLogOut = (Button) findViewById(R.id.profile_logout);
        profileEmail = (TextView) findViewById(R.id.profile_email);
        profileEmail.setText(new StringBuilder().append("Hi ").append(loggedInUser.getEmail()).toString());

        myScore = (TextView) findViewById(R.id.score);
        scorebar = (SeekBar) findViewById(R.id.scoreBar);

        scorebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scoreGiven = getScoreMapping(progress);
                submitScore.setText("Score!" + " " + scoreGiven);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG, "Start Seeking");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        submitScore = (Button) findViewById(R.id.submit_score);


        submitScore.setOnClickListener(this);
        profileLogOut.setOnClickListener(this);
    }

    private long getScoreMapping(int progress) {
        switch (progress) {
            case 0:
                return -100;
            case 1:
                return -50;
            case 2:
                return 50;
            case 3:
                return 100;
            case 4:
                return 500;
            case 5:
                return 1000;
        }

        return 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbReference.child(loggedInUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreAchieved = (long) dataSnapshot.child("score").getValue();
                myScore.setText(String.valueOf(scoreAchieved));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == profileLogOut) {
            fbAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (v == submitScore) {
            submitYourScore();
            submitScore.setText("Score!");
        }
    }

    private void submitYourScore() {
        dbReference.child(loggedInUser.getUid()).child("score").setValue(scoreAchieved + scoreGiven);
    }
}
