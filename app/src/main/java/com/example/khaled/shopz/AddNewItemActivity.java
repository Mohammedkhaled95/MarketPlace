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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.khaled.shopz.Model.Item;
import com.example.khaled.shopz.Model.SuperMarket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNewItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final String TAG = "AddNewItemActivity";
    String createdID;

    private Uri imageUri;



    //Views
    ImageView productImg;
    EditText productNameEt, productPriceEt, productDescriptionEt;
    ImageButton addProductBtn;
    ProgressDialog progressDialog;

    //FireBase
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mItemRefernce;
    StorageReference mStorageRef;
    FirebaseUser current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);


        //intialize views
        productImg = (ImageView) findViewById(R.id.product_image);
        productNameEt = (EditText) findViewById(R.id.product_name);
        productPriceEt = (EditText) findViewById(R.id.product_price);
        productDescriptionEt = (EditText) findViewById(R.id.product_describtion);
        addProductBtn = (ImageButton) findViewById(R.id.add_product_btn);
        progressDialog = new ProgressDialog(AddNewItemActivity.this);
                progressDialog.setMessage("Please Wait .. ");




        //intialize firebase
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mItemRefernce = mDatabase.getReference("SuperMarkets").child(current_user.getUid()).child("items");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");


        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


            }
        });


        addProductBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (!isValidInputs(productImg, productNameEt, productPriceEt, productDescriptionEt)) {
                    progressDialog.dismiss();
                    Toast.makeText(AddNewItemActivity.this, "invalid inputs", Toast.LENGTH_SHORT).show();
                } else {
                    //save product data to firebase

                    saveNewProduct();
                }
            }
        });

    }


    private boolean isValidInputs(ImageView proImg, EditText name, EditText price, EditText description) {
        if (proImg != null
                && name.getText().toString() != null && !name.getText().toString().isEmpty()
                && price.getText().toString() != null && !price.getText().toString().isEmpty()
                && description.getText().toString() != null && !description.getText().toString().isEmpty()) {
            return true;

        }


        return false;

    }

    private void saveNewProduct() {

        Item product = new Item();

        product.setDescription(productDescriptionEt.getText().toString());
        product.setName(productNameEt.getText().toString());
        product.setPrice(productPriceEt.getText().toString());
        product.setImage("");
        //superMarket.setImageURL(imageUri.toString());

         createdID = mItemRefernce.push().getKey();
        mItemRefernce.child(createdID).setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNewItemActivity.this, "product Data Saved", Toast.LENGTH_SHORT).show();


                        mStorageRef.child("itemsIamges").child(createdID + ".jpeg").putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Log.e(TAG, "onSuccess: "+taskSnapshot.toString() );
                                            Toast.makeText(AddNewItemActivity.this, "image saved", Toast.LENGTH_SHORT).show();

                                            mStorageRef.child("itemsIamges").child(current_user.getUid() + ".jpeg").getDownloadUrl()
                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String downloadedURI = uri.toString();
                                                            mItemRefernce.child(createdID).child("image").setValue(downloadedURI);
                                                            progressDialog.dismiss();

                                                            startActivity(new Intent(AddNewItemActivity.this,HomeActivity.class));
                                                            finish();
                                                        }
                                                    });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewItemActivity.this, "image saving failed", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onFailure: " + e.getMessage() + "\n" + e.toString());

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewItemActivity.this, "failure save..", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: " + e.toString());
                        Log.e(TAG, "onFailure: " + e.getMessage());
                    }
                });
        ;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE) {

            imageUri = data.getData();
            productImg.setImageURI(imageUri);


        }


    }

}


