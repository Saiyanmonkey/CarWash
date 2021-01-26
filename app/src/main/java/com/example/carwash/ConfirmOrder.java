package com.example.carwash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class ConfirmOrder extends AppCompatActivity {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    TextView package_name, carwashname, carwashaddress, price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        package_name = findViewById(R.id.package_name);
        carwashname = findViewById(R.id.carwashname);
        carwashaddress = findViewById(R.id.carwashaddress);
        price = findViewById(R.id.price);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        final String address = sp.getString("pickuplocation","");
        //final String providername = sp.getString("name","");
        final String packagename = getIntent().getStringExtra("Package");
        final String providername = getIntent().getStringExtra("ProviderName");
        final String packagecost = getIntent().getStringExtra("Cost");

        package_name.setText(packagename);
        carwashname.setText(providername);
        carwashaddress.setText(address);
        price.setText(packagecost);



        Random ran = new Random();
        int x = ran.nextInt(900)+ 100;
        final String orderid = String.valueOf(x);



        findViewById(R.id.confirmorder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Order currentOrder = new Order(orderid,currentUser.getDisplayName(),address,packagename,providername,packagecost);
                currentOrder.setCustomerID(currentUser.getUid());

                FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("ServiceProvider").child(currentOrder.getId()).setValue(currentOrder);
                FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("Customer").child(currentUser.getUid()).child(currentOrder.getId()).setValue(currentOrder);
                Intent intent = new Intent(ConfirmOrder.this, OrderSuccess.class);
                startActivity(intent);


            }
        });
    }


}


