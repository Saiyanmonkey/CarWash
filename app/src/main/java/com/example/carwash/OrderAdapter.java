package com.example.carwash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<Order> mList;
    Context context;


    public OrderAdapter(Context context, ArrayList<Order> mList){
        this.mList = mList;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        return new OrderAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, final int position) {


        Order order = mList.get(position);
        holder.name.setText(order.getName());
        holder.location.setText(order.getLocation());
        holder.price.setText(order.getCost());
        holder.provider.setText(order.getProvider());
        holder.package_name.setText(order.getPackageName());
        final String Name = order.getName();
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View ordView = LayoutInflater.from(context).inflate(R.layout.popup, null);

                builder.setView(ordView);
                Button reject = ordView.findViewById(R.id.button_reject);
                Button  accept = ordView.findViewById(R.id.button_accept);
                Dialog dialog = builder.create();
                dialog.show();


                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openOrder = new Intent(context, Orders.class);
                        Order currOrder = mList.get(position);
                        currOrder.setProviderID(currentUser.getUid());
                        openOrder.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").child("Customer").child(currOrder.getCustomerID()).setValue(currOrder);
                        FirebaseDatabase.getInstance().getReference().child("Orders").child("Active").child("ServiceProvider").child(currOrder.getProviderID()).setValue(currOrder);

                        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("ServiceProvider").child(currOrder.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending").child("Customer").child(currOrder.getCustomerID()).child(currOrder.getId()).removeValue();


                        context.startActivity(openOrder);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, location, price, package_name,provider;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customerName);
            provider = itemView.findViewById(R.id.provider);
            location = itemView.findViewById(R.id.locationname);
            price = itemView.findViewById(R.id.pricevalue);
            package_name = itemView.findViewById(R.id.package_name);
            view = itemView;


        }
    }
}
