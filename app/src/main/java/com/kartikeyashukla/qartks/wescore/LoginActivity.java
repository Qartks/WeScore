package com.kartikeyashukla.qartks.wescore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText signInEmail;
    private EditText signInPassword;
    private Button signInButton;
    private TextView tv_register;

    private ProgressDialog progressDialogSignIn;

    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener fbAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialogSignIn = new ProgressDialog(this);
        fbAuth = FirebaseAuth.getInstance();

        if (fbAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        signInEmail = (EditText) findViewById(R.id.signIn_email);
        signInPassword = (EditText) findViewById(R.id.signIn_password);
        signInButton = (Button) findViewById(R.id.signIn_button);
        tv_register = (TextView) findViewById(R.id.signin_here);

        signInButton.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            signInUser();
        } else if (v == tv_register) {
            // COMPLETED go to register activity
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void signInUser() {

        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialogSignIn.setMessage("Signing In...");
        progressDialogSignIn.show();

        fbAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }

                        progressDialogSignIn.dismiss();
                        signInEmail.setText("");
                        signInPassword.setText("");
                    }
                });
    }
}
