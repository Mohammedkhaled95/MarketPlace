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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="LoginActivity" ;
    private static final int REQUEST_SIGNUP = 0;

    EditText emailEt,passwordEt;
    Button loginBtn;
    TextView signUpBtn;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = (EditText) findViewById(R.id.input_email);
        passwordEt = (EditText) findViewById(R.id.input_password);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Authenticating");



        loginBtn= (Button) findViewById(R.id.btn_login);
        signUpBtn = (TextView) findViewById(R.id.link_signup);



        mAuth = FirebaseAuth.getInstance();


        //action for sign up button
        signUpBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                //sign up activity
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);



            }
        });

        //action for log in button
        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                login();

            }



        });


    }





    void loginAction(){


        {
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
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


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "signInWithEmail:failure", task.getException());
                                progressDialog.dismiss();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
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

    public void login() {
        Log.d(TAG, "Login");


        if (!validate()) {
            onLoginFailed();
            return;
        }

        progressDialog.show();

            loginBtn.setEnabled(false);



            // TODO: Implement your own authentication logic here.
            loginAction();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();



                        }
                    }, 3000);


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginBtn.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginBtn.setEnabled(true);
    }

    public boolean validate(){
        boolean valid = true;


        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("enter a valid email address");
            valid = false;
        } else {
            emailEt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEt.setError(null);
        }

        return valid;
    }


}
