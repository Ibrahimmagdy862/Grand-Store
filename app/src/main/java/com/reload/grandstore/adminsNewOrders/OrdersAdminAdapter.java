package com.reload.grandstore.adminsNewOrders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reload.grandstore.R;
import com.reload.grandstore.admins.AdminUserProductsActivity;

import java.util.ArrayList;

public class OrdersAdminAdapter extends RecyclerView.Adapter<OrdersAdminAdapter.OrdersAdminViewHolder> {

    ArrayList<OrdersModel>arrayList ;
    OnOrderClicked onOrderClicked ;
    Context mContext ;

    public OrdersAdminAdapter(ArrayList<OrdersModel> arrayList, OnOrderClicked onOrderClicked, Context mContext) {
        this.arrayList = arrayList;
        this.onOrderClicked = onOrderClicked;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrdersAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout , parent , false);
        OrdersAdminViewHolder ordersAdminViewHolder = new OrdersAdminViewHolder(view);
        return ordersAdminViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdminViewHolder holder, final int position) {
        holder.mOrderUser_Name.setText("Name : " + arrayList.get(position).getName());
        holder.mOrder_PhoneNum.setText("Phone : " +arrayList.get(position).getPhone());
        holder.mOrder_TotalAmount_Price.setText("TotalAmount : " +arrayList.get(position).getTotalAmount() + " L.E");
        holder.mOrder_Address_City.setText("Address : " +arrayList.get(position).getAddress() + " ,  " + arrayList.get(position).getCity());
        holder.mOrder_Date_Time.setText("Date : " +arrayList.get(position).getDate() + " , " + arrayList.get(position).getTime());

        holder.mShow_Order_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext , AdminUserProductsActivity.class);
                intent.putExtra("uid" , arrayList.get(position).getPhone());
                mContext.startActivity(intent);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UID = arrayList.get(position).getPhone();

                onOrderClicked.orderItemClick(UID);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrdersAdminViewHolder extends RecyclerView.ViewHolder{
        TextView mOrderUser_Name , mOrder_PhoneNum , mOrder_TotalAmount_Price , mOrder_Address_City , mOrder_Date_Time ;
        Button mShow_Order_Details ;

        public OrdersAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            mOrderUser_Name = itemView.findViewById(R.id.order_User_name);
            mOrder_PhoneNum = itemView.findViewById(R.id.order_User_PhoneNum);
            mOrder_TotalAmount_Price = itemView.findViewById(R.id.order_totalAmount_Price);
            mOrder_Address_City = itemView.findViewById(R.id.order_address_City);
            mOrder_Date_Time = itemView.findViewById(R.id.order_date_time);
            mShow_Order_Details = itemView.findViewById(R.id.show_order_details);

        }
    }

    public interface OnOrderClicked{
        void orderItemClick(String orderItem);
    }

}
