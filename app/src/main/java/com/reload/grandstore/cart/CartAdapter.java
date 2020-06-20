package com.reload.grandstore.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reload.grandstore.R;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    TotalAmount_TypeSession mTotalAmountSession;

    ArrayList<CartModel> arrayList;
    OncartItemclick oncartItemclick;
    Context context;

    int totalamont;

    public CartAdapter(ArrayList<CartModel> arrayList, OncartItemclick oncartItemclick, Context context) {
        this.arrayList = arrayList;
        this.oncartItemclick = oncartItemclick;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
        CartViewHolder cartViewHolder = new CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        holder.mCart_ProductName.setText(arrayList.get(position).getP_name());
        holder.mCart_ProductQuantity.setText(arrayList.get(position).getQuantity());
        holder.mCart_ProductPrice.setText(arrayList.get(position).getPrice() + " L.E");

        if (arrayList.get(position).getPrice() == null || arrayList.get(position).getQuantity() == null) {
            mTotalAmountSession.SaveTotalAmount("0" , "");
        } else {
            int oneTypeTotalAmount = (Integer.valueOf(arrayList.get(position).getQuantity())) * (Integer.valueOf(arrayList.get(position).getPrice()));
            totalamont = totalamont + oneTypeTotalAmount;
            mTotalAmountSession = new TotalAmount_TypeSession(context);
            mTotalAmountSession.SaveTotalAmount(String.valueOf(totalamont) , "");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oncartItemclick.onCartItemClick(arrayList.get(position).getPid());

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView mCart_ProductName, mCart_ProductQuantity, mCart_ProductPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            mCart_ProductName = itemView.findViewById(R.id.cart_Product_name);
            mCart_ProductQuantity = itemView.findViewById(R.id.cart_Product_quantity);
            mCart_ProductPrice = itemView.findViewById(R.id.cart_Product_Price);

        }
    }

    public interface OncartItemclick {
        void onCartItemClick(String itemPid);

    }


}
