package com.example.carwash;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerActiveOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView listView;
    OrderAdapter adapter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_bar);
        Toolbar tb = findViewById(R.id.toolbar);
        listView = findViewById(R.id.orderList);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //Set Navigation Name


        View v = navigationView.getHeaderView(0);
        TextView text = v.findViewById(R.id.nav_username);
        text.setText(mUser.getDisplayName());

        ArrayList<Order> list = new ArrayList<>();
        adapter = new OrderAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").child("Customer").child(mUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    list.add(order);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        setSupportActionBar(tb);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.bar_open, R.string.bar_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_order:
                Intent openMainActivity = new Intent(CustomerActiveOrders.this, MainActivity.class);
                startActivity(openMainActivity);
                break;
            case R.id.nav_pending:
                Intent openPending = new Intent(CustomerActiveOrders.this, CustomerPending_Orders.class);
                startActivity(openPending);
                break;
            case R.id.nav_activeorder:
                break;
            case R.id.nav_history:
                Intent openHistory = new Intent(CustomerActiveOrders.this, customer_history.class);
                startActivity(openHistory);
                break;
            case R.id.nav_forum:
                Intent openForum = new Intent(CustomerActiveOrders.this, ForumCustomerActivity.class);
                startActivity(openForum);
                break;
            case R.id.nav_logout:
                Intent openLogin = new Intent(CustomerActiveOrders.this, LoginActivity.class);
                startActivity(openLogin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}