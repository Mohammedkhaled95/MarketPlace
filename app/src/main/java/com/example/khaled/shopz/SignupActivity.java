package com.example.khaled.shopz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.khaled.shopz.Model.Item;
import com.example.khaled.shopz.Model.SuperMarket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    //constants
    private static final String TAG = "SignupActivity";
    private static final int PICK_IMAGE = 1;

    private Uri imageUri;


    EditText ownerNameEt,emailEt,passwordEt,supermarketNameEt,supermarketPhoneEt,supermarketLocationEt;
    Button saveBtn;
    ImageView supermarketImage;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference superMarket_table;
    StorageReference mStorageRef;
    FirebaseUser current_user;



    String ownerName;
    String email;
    String password;
    String supermarketName;
    String supermarkertAddress;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //intialize views
        ownerNameEt = (EditText) findViewById(R.id.owner_name_editText);
        supermarketNameEt = (EditText) findViewById(R.id.supernarket_name_editText);
        supermarketPhoneEt = (EditText) findViewById(R.id.supermarket_phone_editText);
        supermarketLocationEt = (EditText) findViewById(R.id.supermarket_location_editText);
        emailEt = (EditText) findViewById(R.id.email_editText);
        passwordEt = (EditText) findViewById(R.id.password_editText);
        saveBtn = (Button)findViewById(R.id.save_button);
        supermarketImage = (ImageView) findViewById(R.id.supermarket_imageView);

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Creating account, please wait");


        //intialize firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        superMarket_table = firebaseDatabase.getReference("SuperMarkets");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");



        supermarketImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


            }
        });







        //signup and save data of supermarket
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //intialize String (user data)
                ownerName = ownerNameEt.getText().toString();
                email = emailEt.getText().toString();
                password = passwordEt.getText().toString();
                supermarketName = supermarketNameEt.getText().toString();
                supermarkertAddress = supermarketLocationEt.getText().toString();
                phoneNumber = supermarketPhoneEt.getText().toString();


                progressDialog.show();
                if ( !isValidInputs()){//supermarketImage == null ||

                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, "Not valid inputs", Toast.LENGTH_SHORT).show();
                                }
                else
                {
                    createNewUser();


                }

                    }
        });
    }

    private void createNewUser() {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignupActivity.this, "account created", Toast.LENGTH_SHORT).show();
                            current_user = mAuth.getCurrentUser();
                            String  deviceToken = FirebaseInstanceId.getInstance().getToken();
                            saveUserData( deviceToken);

                            progressDialog.dismiss();
                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Log.e(TAG, "onComplete: "+task.toString() );
                            Log.e(TAG, "onComplete: "+task.getException().toString() );
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void saveUserData(String deviceToken) {

        final SuperMarket superMarket = new SuperMarket();

        superMarket.setLocation(supermarkertAddress);
        superMarket.setSuperMarketname(supermarketName);
        superMarket.setOwnerName(ownerName);
        superMarket.setPhone(phoneNumber);
        superMarket.setImage("");
        superMarket.setRate("");
        superMarket.setDeviceToken(deviceToken);
       // superMarket.setItems("");


        superMarket_table.child(current_user.getUid()).setValue(superMarket)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();

                            String current_user_id  = current_user.getUid();

                            mStorageRef.child(current_user_id+".jpeg").putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(SignupActivity.this, "image saved", Toast.LENGTH_SHORT).show();
                                                String downloadedImage = taskSnapshot.getDownloadUrl().toString();
                                                        superMarket_table.child(current_user.getUid())
                                                                .child("image").setValue(downloadedImage);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignupActivity.this, "image saving failed", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "onFailure: "+e.getMessage()+"\n"+e.toString() );

                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "failure save..", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: "+e.toString() );
                        Log.e(TAG, "onFailure: "+e.getMessage() );
                    }
                });;







    }


    private boolean isValidInputs() {
        if (ownerName != null && !ownerName.isEmpty()
                &&email != null && !email.isEmpty()
                &&password != null && !password.isEmpty()
                &&supermarketName != null && !supermarketName.isEmpty()
                &&supermarkertAddress != null && !supermarkertAddress.isEmpty()
                &&phoneNumber != null && !phoneNumber.isEmpty())
        {
            return true;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE){

            imageUri=data.getData();
            supermarketImage.setImageURI(imageUri);


        }

    }
}
