package com.example.carwash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectPackages extends AppCompatActivity {

    TextView alldata;
    Button packagea, packageb, packagec;
    String a = "Package A";
    String pricea = "RM 80";
    String b = "Package B";
    String priceb = "RM 60";
    String c = "Package C";
    String pricec = "RM 40";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_packages);

        packagea = findViewById(R.id.packagea);
        packageb = findViewById(R.id.packageb);
        packagec = findViewById(R.id.packagec);

        alldata = findViewById(R.id.alldata);
        final String name = getIntent().getStringExtra("Name");

        //String data = getIntent().getExtras().getString("AnyKeyName");
        packagea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPackages.this, ConfirmOrder.class);
                intent.putExtra("ProviderName",name);
                intent.putExtra("Package",a);
                intent.putExtra("Cost",pricea);
                startActivity(intent);
            }
        });

        packageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPackages.this, ConfirmOrder.class);
                intent.putExtra("ProviderName",name);
                intent.putExtra("Package",b);
                intent.putExtra("Cost",priceb);
                startActivity(intent);
            }
        });

        packagec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPackages.this, ConfirmOrder.class);
                intent.putExtra("ProviderName",name);
                intent.putExtra("Package",c);
                intent.putExtra("Cost",pricec);
                startActivity(intent);
            }
        });


    }
}