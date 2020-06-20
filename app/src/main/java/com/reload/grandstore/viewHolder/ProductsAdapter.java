package com.reload.grandstore.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reload.grandstore.R;

import com.reload.grandstore.admins.Admin_Maintain_ProductsActivity;
import com.reload.grandstore.products.ProductsDetailsActivity;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    TotalAmount_TypeSession mTotalAmount_typeSession ;

    ArrayList<ProductsModel> arrayList;
    Context mContext ;

    public ProductsAdapter(ArrayList<ProductsModel> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        holder.mProductName.setText(arrayList.get(position).getName());
        holder.mProductDescript.setText(arrayList.get(position).getDescription());
        holder.mProductPrice.setText("Price: " + arrayList.get(position).getPrice() + "L.E");

        Picasso.get().load(arrayList.get(position).getImage()).into(holder.mProductImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(mContext, "this is : " + position, Toast.LENGTH_SHORT).show();
                mTotalAmount_typeSession = new TotalAmount_TypeSession(mContext);
                if (mTotalAmount_typeSession.getTotalAmountData().get(mTotalAmount_typeSession.KEY_Type).equals("Admin")){
                    Intent i = new Intent(mContext, Admin_Maintain_ProductsActivity.class);
                    i.putExtra("pid", arrayList.get(position).getPid());
                    mContext.startActivity(i);

                }else {
                    Intent i = new Intent(mContext, ProductsDetailsActivity.class);
                    i.putExtra("pid", arrayList.get(position).getPid());
                    mContext.startActivity(i);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView mProductName, mProductDescript, mProductPrice;
        public ImageView mProductImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductName = itemView.findViewById(R.id.product_Name);
            mProductDescript = itemView.findViewById(R.id.product_Description);
            mProductPrice = itemView.findViewById(R.id.product_Price);
            mProductImage = itemView.findViewById(R.id.product_Image);
        }

    }


    public void filterList(ArrayList<ProductsModel> filteredList) {

        arrayList = filteredList;
        notifyDataSetChanged();

    }


}
