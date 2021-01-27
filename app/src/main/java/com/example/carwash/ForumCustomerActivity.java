package com.example.carwash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForumCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Dialog popAddPost;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    TextView popupTitle, popupDescription;
    ImageView popupAddButton;

    //Navigation
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    //test Uid
    //String userId = "gLIq5HDBCsPolsocU1wpeniMui43";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        //Navigation
        navigationView = findViewById(R.id.navigation_bar);
        Toolbar tb = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(tb);
        navigationView.getMenu();
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.bar_open, R.string.bar_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_history);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        iniPopup();

        View v = navigationView.getHeaderView(0);
        TextView text = v.findViewById(R.id.nav_username);
        text.setText(currentUser.getDisplayName());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
            }
        });

//        Fragment fragment = new TimelineFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragOutput, fragment );

        getSupportFragmentManager().beginTransaction().replace(R.id.fragOutput, new com.example.carwash.TimelineFragment()).commit();



    }

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_desc);
        popupAddButton = popAddPost.findViewById(R.id.popup_add);

        popupAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddButton.setVisibility(View.INVISIBLE);

                if(!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()){

                    //ID sementara currentUser.getUid()
                    com.example.carwash.Post post = new com.example.carwash.Post(popupTitle.getText().toString(), popupDescription.getText().toString(), currentUser.getUid());

                    addPost(post);

                }else{
                    showMessage("Please verify all input fields");
                    popupAddButton.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void addPost(com.example.carwash.Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Post").push();

        //Get post unique key
        String key = myRef.getKey();
        post.setPostKey(key);

        //Add post
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupAddButton.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(com.example.carwash.ForumCustomerActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_order:
                Intent openOrders = new Intent(ForumCustomerActivity.this, MainActivity.class);
                startActivity(openOrders);
                break;
            case R.id.nav_pending:
                Intent openPending = new Intent(ForumCustomerActivity.this, CustomerPending_Orders.class);
                startActivity(openPending);
                break;
            case R.id.nav_history:
                Intent openHistory = new Intent(ForumCustomerActivity.this, customer_history.class);
                startActivity(openHistory);
                break;
            case R.id.nav_forum:
                break;
            case R.id.nav_activeorder:
                Intent intent = new Intent(ForumCustomerActivity.this,CustomerActiveOrders.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                Intent openLogin = new Intent(ForumCustomerActivity.this, LoginActivity.class);
                mAuth.signOut();
                startActivity(openLogin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}