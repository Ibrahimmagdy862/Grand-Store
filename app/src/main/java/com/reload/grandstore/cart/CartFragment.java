package com.reload.grandstore.cart;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reload.grandstore.R;
import com.reload.grandstore.home.HomeActivity;
import com.reload.grandstore.products.ProductsDetailsActivity;
import com.reload.grandstore.sharedPerformances.TotalAmount_TypeSession;
import com.reload.grandstore.sharedPerformances.UserSession;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements CartAdapter.OncartItemclick {
    View view ;

    TextView mtxTotalAmount , mTxMsg ;
    Button mNextProcessBtn , mSetTotalPriceBtn ;

    RecyclerView recyclerView ;
    ArrayList<CartModel> mCartList;
    CartAdapter mCartAdapt;
    DatabaseReference mDatabaseRef;

    UserSession mSession ;
    TotalAmount_TypeSession mTotalAmountSession ;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        initViews();
        getDataFromFB();

        return view;
    }

    private void initViews() {
        mSession = new UserSession(getActivity());
        mTotalAmountSession = new TotalAmount_TypeSession(getActivity());

        mtxTotalAmount = view.findViewById(R.id.total_Price);
        mTxMsg = view.findViewById(R.id.msg1);
        mNextProcessBtn = view.findViewById(R.id.next_Process_btn);
        mSetTotalPriceBtn = view.findViewById(R.id.set_TotalPrice);

        recyclerView = view.findViewById(R.id.cart_List);
        mCartList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mNextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(getActivity() , ConfirmFinalOrderActivity.class);
                q.putExtra("Total Price" , mTotalAmountSession.getTotalAmountData().get(mTotalAmountSession.KEY_TotalAmount));

                startActivity(q);

            }
        });

        mSetTotalPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Click Again to make Order " , Toast.LENGTH_SHORT).show();
                mtxTotalAmount.setText("Total Price = " + mTotalAmountSession.getTotalAmountData().get(mTotalAmountSession.KEY_TotalAmount) + " L.E");
                mSetTotalPriceBtn.setVisibility(View.GONE);
            }
        });

    }

    private void getDataFromFB() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View").child(mSession.getUserData().get(mSession.KEY_PHONE)).child("Products");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CartModel cartModel = dataSnapshot1.getValue(CartModel.class);
                    mCartList.add(cartModel);
                }
                mCartAdapt = new CartAdapter(mCartList ,CartFragment.this , getActivity());
                recyclerView.setAdapter(mCartAdapt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "something is wrong ....", Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onCartItemClick(final String itemPid) {

        CharSequence options[] = new CharSequence[]
                {
                        "Edit" , "Remove"
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cart Options: ");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    Intent intent = new Intent(getActivity() , ProductsDetailsActivity.class);
                    intent.putExtra("pid" , itemPid);
                    startActivity(intent);

                }else if (which == 1){
                    DatabaseReference  mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(mSession.getUserData().get(mSession.KEY_PHONE)).child("Products")
                            .child(itemPid);

                    mDatabaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity() , "Item is Removed ", Toast.LENGTH_SHORT).show();

                                mTotalAmountSession.SaveTotalAmount("0" , "");

                                Intent p = new Intent(getActivity() , HomeActivity.class);
                                startActivity(p);
                                getActivity().finish();
                            }

                        }
                    });

                }


            }
        });

        builder.show();

    }

    @Override
    public void onStart() {
        CheckOrderState();
        super.onStart();
    }

    private void CheckOrderState(){
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(mSession.getUserData().get(mSession.KEY_PHONE));

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){

                        mtxTotalAmount.setText("Dear : " + userName + "\n order is shipped successfully. " );
                        recyclerView.setVisibility(View.GONE);

                        mTxMsg.setVisibility(View.VISIBLE);
                        mTxMsg.setText("Congratulation , your final order has been Shipped successfully. Soon you will received your order at your door step. ");

                        mNextProcessBtn.setVisibility(View.GONE);
                        mSetTotalPriceBtn.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), "You can purchase more Products , Once You received your first final order. ", Toast.LENGTH_SHORT).show();

                    }else if (shippingState.equals("not shipped")){

                        mtxTotalAmount.setText("Shipping State = Not Shipped " );
                        recyclerView.setVisibility(View.GONE);

                        mTxMsg.setVisibility(View.VISIBLE);
                        mNextProcessBtn.setVisibility(View.GONE);
                        mSetTotalPriceBtn.setVisibility(View.GONE);

               //         Toast.makeText(getActivity(), "You can purchase more Products , Once You received your first final order. ", Toast.LENGTH_SHORT).show();


                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
