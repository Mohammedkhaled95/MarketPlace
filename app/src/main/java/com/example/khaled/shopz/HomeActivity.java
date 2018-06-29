package com.example.khaled.shopz;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khaled.shopz.Interface.ItemClickListener;
import com.example.khaled.shopz.Model.Item;
import com.example.khaled.shopz.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    //Firebase
    FirebaseDatabase mDatabase;
    DatabaseReference mItemsReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Item clickItem;

    //Views
    RecyclerView itemRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Item,ItemViewHolder> adapter;

    //navigation views
    ProgressDialog progressDialog;
    ImageView navImg;
    TextView navName;
    TextView navEmail;



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



        //create view
        itemRecyclerView = (RecyclerView) findViewById(R.id.items_list);
        itemRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        itemRecyclerView.setLayoutManager(layoutManager);

        loadmenu();

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
                viewHolder.itemPrice.setText("Price : "+model.getPrice()+" $");
                viewHolder.itemDescription.setText(model.getDescription());

                Log.e(TAG, "populateViewHolder: menu  :"+model.toString());


                if (!model.getImage().isEmpty()){
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.itemImage);}
                        else {
                    Picasso.with(getBaseContext())
                            .load(R.drawable.plusbutton)
                            .into(viewHolder.itemImage);
                }

                Log.e(TAG, "populateViewHolder: imaaaaage "+model.getImage() );

                 clickItem = model;


                viewHolder.setItemclickListener(new ItemClickListener() {


                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {

                        Toast.makeText(HomeActivity.this, clickItem.getName(), Toast.LENGTH_SHORT).show();
                       /* Intent foodListIntent = new Intent(Home.this, FoodList.class);
                        foodListIntent.putExtra("categoryID",adapter.getRef(position).getKey());
                        startActivity(foodListIntent);*/
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
            startActivity(new Intent(HomeActivity.this,AddNewItemActivity.class));
            return true;
        }
        else if (id == R.id.action_notification){
            //handle notification
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
