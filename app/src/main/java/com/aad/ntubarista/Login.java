package com.aad.ntubarista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void registerUser(View v) {

        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPass = findViewById(R.id.edtPass);
        final String email = edtEmail.getText().toString();
        final String password = edtPass.getText().toString();

        /*
        TODO NOT IN USE
        // Encrypt password
        MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] passHash = md.digest(password.getBytes("UTF-8"));
        */

        // Add to firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Add user to DB as well as auth
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", email);
                            userMap.put("isStaff", false);
                            //userMap.put("password", passHash.toString());

                            db.collection("users").document(user.getUid()).set(userMap);
                            redirToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(View v)
    {
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPass = findViewById(R.id.edtPass);
        final String email = edtEmail.getText().toString();
        final String password = edtPass.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            redirToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Wrong email or password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Navigation
    private void redirToMain()
    {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        Login.this.startActivity(mainIntent);
    }
}
