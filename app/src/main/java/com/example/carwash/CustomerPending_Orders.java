package com.example.carwash;

import android.app.AlertDialog;
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

public class CustomerPending_Orders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar tb;
    RecyclerView listView;
    OrderAdapter adapter;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_bar);
        tb = findViewById(R.id.toolbar);
        listView = findViewById(R.id.pending_List);

        ArrayList<Order> list = new ArrayList<>();
        adapter = new OrderAdapter(getApplicationContext(), list);
        listView.setAdapter(adapter);

        listView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("Customer").child(mUser.getUid());
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

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Pending_Orders.this);
//                View ordView = getLayoutInflater().inflate(R.layout.popup,null);
//
//
//                builder.setView(ordView);
//                Button  reject = ordView.findViewById(R.id.button_reject);
//                Button  accept = ordView.findViewById(R.id.button_accept);
//                dialog = builder.create();
//                dialog.show();
//
//                reject.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                accept.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent openOrder = new Intent(Pending_Orders.this, Orders.class);
//                        Order currOrder = list.get(position);
//                        FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").setValue(currOrder);
//                        reference.child(String.valueOf(currOrder.getId())).removeValue();
//
//                        startActivity(openOrder);
//                    }
//                });
//
//            }
//        });
//

        setSupportActionBar(tb);




        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.bar_open, R.string.bar_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_pending);
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
            case R.id.nav_pending:
                break;
            case R.id.nav_order:
                Intent openOrder = new Intent(CustomerPending_Orders.this, MainActivity.class);
                startActivity(openOrder);
                break;
            case R.id.nav_activeorder:
                Intent openActive = new Intent(CustomerPending_Orders.this, CustomerActiveOrders.class);
                startActivity(openActive);
                break;
            case R.id.nav_history:
                Intent openHistory = new Intent(CustomerPending_Orders.this, customer_history.class);
                startActivity(openHistory);
                break;
            case R.id.nav_forum:
                Intent openForum = new Intent(CustomerPending_Orders.this, ForumActivity.class);
                startActivity(openForum);
                break;
            case R.id.nav_logout:
                Intent openLogin = new Intent(CustomerPending_Orders.this,LoginActivity.class);
                startActivity(openLogin);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

////
//    class customAdapter extends BaseAdapter {
//        Context c;
//        ArrayList<Order> list;
//
//        public customAdapter(Context c, ArrayList<Order> list) {
//            this.c = c;
//            this.list = list;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Order getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = LayoutInflater.from(c).inflate(R.layout.order_list, parent, false);
//            }
//
//            TextView card_name = (TextView)findViewById(R.id.cardname);
//            TextView location = (TextView)findViewById(R.id.card_location);
//            TextView price = (TextView)findViewById(R.id.card_price);
//            TextView pack = (TextView)findViewById(R.id.pack);
//
//            final Order s = this.getItem(this.getCount()-1);
//
//            card_name.setText(s.getName());
//            location.setText(s.getLocation());
//            price.setText(s.getCost());
//            pack.setText(s.getPackageName());
//
//            return convertView;
//
//        }
//    }
}


