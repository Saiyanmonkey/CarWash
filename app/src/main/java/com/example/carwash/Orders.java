package com.example.carwash;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Orders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView listView;
    OrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_bar);
        Toolbar tb = findViewById(R.id.toolbar);
        listView = findViewById(R.id.orderList);

        ArrayList<Order> list = new ArrayList<>();
        adapter = new OrderAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").child("ServiceProvider").child(currentUser.getUid());
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

        Button finish = findViewById(R.id.finish_order);
        Button cancel = findViewById(R.id.cancel_order);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() >0) {
                    Order currOrder = list.get(list.size() - 1);
                    FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("ServiceProvider").child(currOrder.getId()).setValue(currOrder);
                    FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("Customer").child(currOrder.getCustomerID()).child(currOrder.getId()).setValue(currOrder);


                    FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").child("ServiceProvider").child(currOrder.getProviderID()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").child("Customer").child(currOrder.getCustomerID()).removeValue();
                    Intent intent = new Intent(Orders.this, Pending_Orders.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Orders.this, "There is no Accepted Order",Toast.LENGTH_SHORT).show();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size()>0) {
                    Intent openOrder = new Intent(Orders.this, Pending_Orders.class);
                    Order currOrder = list.get(list.size() - 1);
                    FirebaseDatabase.getInstance().getReference().child("Orders").child("History").child("ServiceProvider").child(currOrder.getProviderID()).child(currOrder.getId()).setValue(currOrder);
                    FirebaseDatabase.getInstance().getReference().child("Orders").child("History").child("Customer").child(currOrder.getCustomerID()).child(currOrder.getId()).setValue(currOrder);
                    FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").removeValue();
                    startActivity(openOrder);
                }
                else {
                    Toast.makeText(Orders.this, "There is no Accepted Order",Toast.LENGTH_SHORT).show();
                }
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
                break;
            case R.id.nav_pending:
                Intent openPending = new Intent(Orders.this, Pending_Orders.class);
                startActivity(openPending);
                break;
            case R.id.nav_history:
                Intent openHistory = new Intent(Orders.this, provider_history.class);
                startActivity(openHistory);
                break;
            case R.id.nav_logout:
                Intent openLogin = new Intent(Orders.this, LoginActivity.class);
                startActivity(openLogin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}