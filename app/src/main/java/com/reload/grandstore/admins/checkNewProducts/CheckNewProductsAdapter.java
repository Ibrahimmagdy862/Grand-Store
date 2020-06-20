package com.reload.grandstore.admins.checkNewProducts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reload.grandstore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckNewProductsAdapter extends RecyclerView.Adapter<CheckNewProductsAdapter.CheckNewProductsViewHolder> {

    ArrayList<CheckNewProductsModel> arrayList;
    OnCheckNewProductsClick onCheckNewProductsClick ;

    public CheckNewProductsAdapter(ArrayList<CheckNewProductsModel> arrayList, OnCheckNewProductsClick onCheckNewProductsClick) {
        this.arrayList = arrayList;
        this.onCheckNewProductsClick = onCheckNewProductsClick;
    }

    @NonNull
    @Override
    public CheckNewProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, viewGroup, false);
        CheckNewProductsViewHolder checkNewProductsViewHolder = new CheckNewProductsViewHolder(view);
        return checkNewProductsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckNewProductsViewHolder holder, final int position) {
        holder.mProductName.setText(arrayList.get(position).getName());
        holder.mProductDescript.setText(arrayList.get(position).getDescription());
        holder.mProductPrice.setText("Price: " + arrayList.get(position).getPrice() + "L.E");

        holder.mProductState.setVisibility(View.VISIBLE);
        holder.mProductState.setText(arrayList.get(position).getState());

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.mProductImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCheckNewProductsClick.onItemClick(arrayList.get(position).getPid());

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CheckNewProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView mProductName, mProductDescript, mProductPrice , mProductState;
        private ImageView mProductImage;

        public CheckNewProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductName = itemView.findViewById(R.id.product_Name);
            mProductDescript = itemView.findViewById(R.id.product_Description);
            mProductPrice = itemView.findViewById(R.id.product_Price);
            mProductState  = itemView.findViewById(R.id.product_State);
            mProductImage = itemView.findViewById(R.id.product_Image);

        }
    }

    public interface OnCheckNewProductsClick{
        void onItemClick(String itemPid);
    }

}
