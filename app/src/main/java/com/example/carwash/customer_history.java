package com.example.carwash;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

public class customer_history extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser currentUser;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    OrderAdapter adapter;
    RecyclerView listView;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_bar);
        Toolbar tb = findViewById(R.id.toolbar);
        listView = findViewById(R.id.historyList);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ArrayList<Order> list = new ArrayList<>();
        adapter = new OrderAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child("History").child("Customer").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    list.add(order);
                }
                adapter.notifyDataSetChanged();


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
        navigationView.setCheckedItem(R.id.nav_history);
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
                Intent openOrder = new Intent(customer_history.this,MainActivity.class);
                startActivity(openOrder);
                break;
            case R.id.nav_activeorder:
                Intent openActive = new Intent(customer_history.this, CustomerActiveOrders.class);
                startActivity(openActive);
                break;
            case R.id.nav_pending:
                Intent openPending = new Intent(customer_history.this, CustomerPending_Orders.class);
                startActivity(openPending);
                break;
            case R.id.nav_history:
                break;
            case R.id.nav_forum:
                Intent openForum = new Intent(customer_history.this, ForumActivity.class);
                startActivity(openForum);
                break;
            case R.id.nav_logout:
                Intent openLogin = new Intent(customer_history.this,LoginActivity.class);
                startActivity(openLogin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}