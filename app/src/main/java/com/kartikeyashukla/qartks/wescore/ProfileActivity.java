package com.kartikeyashukla.qartks.wescore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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

    private String revonUID = "8unIHi8DlcUtoM53kYJhcghg5H03";
    private String kartikeyaUID = "WrDf9k11KrQAEDfGJEPPDM6uCcF2";

    private String partnerUID;

    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

//        registerReceiver(broadcastReceiver, new IntentFilter(WS_FirebaseInstanceIdService.TOKEN_BROARDCAST));
        SharedPrefManager sharedPref = SharedPrefManager.getInstance(this);

        Log.i(TAG , FirebaseInstanceId.getInstance().getToken());

        loggedInUser = fbAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();

        if (loggedInUser.getUid().equals(revonUID)) {
            partnerUID = kartikeyaUID;
        } else {
            partnerUID = revonUID;
        }

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

        dbReference.child(partnerUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreAchieved =  (dataSnapshot.child("score").getValue() == null ? 0L : (long) dataSnapshot.child("score").getValue() );
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
        dbReference.child(partnerUID).child("score").setValue(scoreAchieved + scoreGiven);
    }
}
