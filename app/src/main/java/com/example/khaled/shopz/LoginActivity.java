package com.example.khaled.shopz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="LoginActivity" ;
    EditText emailEt,passwordEt;
    Button loginBtn,signUpBtn;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    String email,password;
    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = (EditText) findViewById(R.id.email_edittext);
        passwordEt = (EditText) findViewById(R.id.password_edittext);
        progressBar = (ProgressBar)findViewById(R.id.login_progressBar);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait .. ");


        loginBtn= (Button) findViewById(R.id.login_button);
        signUpBtn = (Button) findViewById(R.id.signup_button);

        mAuth = FirebaseAuth.getInstance();


        //action for sign up button
        signUpBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                //sign up activity

                Intent signUPIntent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(signUPIntent);

            }
        });

        //action for log in button
        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                progressDialog.show();

                 {
                     email = emailEt.getText().toString();
                     password = passwordEt.getText().toString();
                     Log.e(TAG, "onClick: "+email+"  "+password );

                    mAuth.signInWithEmailAndPassword(email, password)

                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.e(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Intent loginIntent =new Intent(LoginActivity.this , HomeActivity.class);
                                        loginIntent.putExtra("currentUser",user.getEmail());

                                        startActivity(loginIntent);
                                        progressDialog.dismiss();

                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.e(TAG, "signInWithEmail:failure", task.getException());
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "failed authentication", Toast.LENGTH_SHORT).show();


                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: "+e.toString()+"\n"+e.getMessage() );
                        }
                    });
                }
               /* if (isValidInputs()) else
                {
                    Toast.makeText(LoginActivity.this, "Not Valid inputs", Toast.LENGTH_SHORT).show();
                }*/
                //handle login process
            }



        });


    }

    public boolean isValidInputs() {
        if (email != null && !email.isEmpty()
                && password != null && !password.isEmpty())
        {
            return true;
        }

        return false;
    }
}
