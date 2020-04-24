package com.aad.ntubarista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                registerStaff();
                return true;
            }
        });

        if(pref != null & !(pref.getString("userId", "").equals("")))
        {
            if(pref.getBoolean("isStaff", false))
            {
                Intent staffIntent = new Intent(Login.this, StaffMain.class);
                Login.this.startActivity(staffIntent);
                return;
            }

            Intent mainIntent = new Intent(Login.this, MainActivity.class);
            Login.this.startActivity(mainIntent);
        }

    }

    private void registerStaff()
    {

        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPass = findViewById(R.id.edtPass);
        final String email = edtEmail.getText().toString();
        final String password = edtPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Add user to DB as well as auth
                            FirebaseUser user = mAuth.getCurrentUser();
                            editor = pref.edit();
                            editor.putString("userId", user.getUid());
                            editor.apply();

                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", email);
                            userMap.put("isStaff", true);
                            //userMap.put("password", passHash.toString());

                            db.collection("users").document(user.getUid()).set(userMap);
                            redirToStaffMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void registerUser(View v)
    {

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

                        // Add user to DB as well as auth, and local
                        FirebaseUser user = mAuth.getCurrentUser();
                        editor = pref.edit();
                        editor.putString("userId", user.getUid());
                        editor.apply();

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

                            FirebaseUser user = mAuth.getCurrentUser();
                            editor = pref.edit();
                            editor.putString("userId", user.getUid());
                            editor.apply();

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

    private void redirToStaffMain()
    {
        Intent mainIntent = new Intent(Login.this, StaffMain.class);
        Login.this.startActivity(mainIntent);
    }
}
