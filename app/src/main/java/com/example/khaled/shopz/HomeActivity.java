package com.example.khaled.shopz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.shopz.Interface.ItemClickListener;
import com.example.khaled.shopz.Model.Item;
import com.example.khaled.shopz.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    //Firebase
    FirebaseDatabase mDatabase;
    DatabaseReference mItemsReference;
    DatabaseReference mSuperMarketReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    Item clickItem;

    String name;
    String phone;
    String profileImage;

    AlertDialog.Builder signOutBuilder;
    Dialog deleteItemDialoge;
    Dialog makeChoiceBuilder;

    //Views
    RecyclerView itemRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;

    //navigation views
    ProgressDialog progressDialog;
    ImageView navImg;
    TextView navName;
    TextView navEmail;


    TextView choiceDisplayTv;
    TextView choiceEditTv;
    TextView choiceDelteTv;
    
    Button yesDeleteBtn;
    Button noDeleteBtn;

    ImageButton editBtn, deleteBtn;


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    mAuth.signOut();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //predefined views
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navheader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        //nav views
        navImg = (ImageView) navheader.findViewById(R.id.nav_img);
        navName = (TextView) navheader.findViewById(R.id.nav_name);
        navEmail = (TextView) navheader.findViewById(R.id.nav_email);


        //intailize firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mItemsReference = mDatabase.getReference("SuperMarkets").child(currentUser.getUid()).child("items");
        mSuperMarketReference = mDatabase.getReference("SuperMarkets").child(currentUser.getUid());




       //load navigation
        loadNavigation();


        //create view
        itemRecyclerView = (RecyclerView) findViewById(R.id.items_list);
        itemRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        itemRecyclerView.setLayoutManager(layoutManager);

        //load menu of items
        loadmenu();


        //sign out dialoge
        signOutBuilder = new AlertDialog.Builder(HomeActivity.this);
        signOutBuilder.setMessage("Are you sure, you want to Sign out ?")
                      .setPositiveButton("Yes", dialogClickListener)
                      .setNegativeButton("No", dialogClickListener);

        //delete dialoge
        delteDialoge();

        //make choice after click
        makeChoice();




    }

    private void delteDialoge() {

        deleteItemDialoge = new Dialog(HomeActivity.this);
        deleteItemDialoge.setTitle("Delete ?");
        deleteItemDialoge.setContentView(R.layout.delete_item_dialoge);

        yesDeleteBtn = (Button) deleteItemDialoge.findViewById(R.id.delte_yes_button);
        noDeleteBtn = (Button) deleteItemDialoge.findViewById(R.id.delte_no_button);

        yesDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //yes delete
            }
        });
        noDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //no delete
            }
        });
    }

    private void makeChoice() {
        //choice action
        makeChoiceBuilder = new Dialog(HomeActivity.this);
        makeChoiceBuilder.setContentView(R.layout.make_choice_builder);
        makeChoiceBuilder.setTitle("Choice ..");

        choiceDelteTv = (TextView) makeChoiceBuilder.findViewById(R.id.delete_choice_tv);
        choiceEditTv = (TextView) makeChoiceBuilder.findViewById(R.id.edit_choice_tv);
        choiceDisplayTv = (TextView) makeChoiceBuilder.findViewById(R.id.display_choice_tv);

        choiceDisplayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display item
                Toast.makeText(HomeActivity.this, "display item", Toast.LENGTH_SHORT).show();
            }
        });


        choiceEditTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit item
                Toast.makeText(HomeActivity.this, "edit item", Toast.LENGTH_SHORT).show();

            }
        });

        choiceDelteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete item

                deleteItemDialoge.show();
            }
        });



    }

    private void loadNavigation() {

        mSuperMarketReference.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    profileImage = dataSnapshot.getValue().toString();

                    if (profileImage != null) {
                        if (!profileImage.isEmpty()) {
                            Picasso.with(HomeActivity.this)
                                    .load(profileImage)
                                    .into(navImg);
                        } else {
                            navImg.setImageDrawable(getResources().getDrawable(R.drawable.user_account_photo));

                        }
                    } else {
                        navImg.setImageDrawable(getResources().getDrawable(R.drawable.user_account_photo));

                    }
                } else {
                    navImg.setImageDrawable(getResources().getDrawable(R.drawable.user_account_photo));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSuperMarketReference.child("ownerName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    name = dataSnapshot.getValue().toString();
                    navName.setText(name);
                } else {
                    navName.setText("fake account");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String email = null;
        if (currentUser != null) {
            email = currentUser.getEmail();
            Log.e(TAG, "onCreate: current user data " + currentUser.toString() + "\n" + currentUser.getEmail() + "\n" + currentUser.getDisplayName());
        }
        if (email != null && !email.isEmpty()) {
            navEmail.setText(email);

        } else {

            mSuperMarketReference.child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {

                        phone = dataSnapshot.getValue().toString();
                        Log.e(TAG, "onDataChange: " + phone);
                        navEmail.setText(phone);
                    } else {
                        navEmail.setText("fake account");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void loadmenu() {

        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>
                (Item.class,
                        R.layout.items_list_item,
                        ItemViewHolder.class,
                        mItemsReference) {

            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Item model, int position) {

                viewHolder.itemName.setText(model.getName());
                viewHolder.itemPrice.setText("Price : " + model.getPrice() + " $");
                viewHolder.itemDescription.setText(model.getDescription());

                Log.e(TAG, "populateViewHolder: menu  :" + model.toString());


                if (!model.getImage().isEmpty()) {
                    Picasso.with(getBaseContext()).load(model.getImage())
                            .into(viewHolder.itemImage);
                } else {
                    Picasso.with(getBaseContext())
                            .load(R.drawable.plusbutton)
                            .into(viewHolder.itemImage);
                }

                Log.e(TAG, "populateViewHolder: imaaaaage " + model.getImage());

                clickItem = model;


                viewHolder.setItemclickListener(new ItemClickListener() {


                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {

                        makeChoiceBuilder.show();
                    }
                });
            }


        };
        itemRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_new_item) {
            //go to create new item
            startActivity(new Intent(HomeActivity.this, AddNewItemActivity.class));
            return true;
        } else if (id == R.id.action_notification) {
            //handle notification
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_new_item) {
            startActivity(new Intent(HomeActivity.this, AddNewItemActivity.class));

        } else if (id == R.id.nav_oder_notifications) {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_edit) {
            Toast.makeText(this, "edits ", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_sign_out) {
            signOutBuilder.show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
