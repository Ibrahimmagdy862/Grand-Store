package com.reload.grandstore.logOut;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.reload.grandstore.R;
import com.reload.grandstore.signin.SignInActivity;

import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogOutFragment extends AppCompatDialogFragment {
    View view;
    Button mLogOutBtn, mCancelBtn;

    public LogOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log_out, container, false);


        mLogOutBtn = view.findViewById(R.id.log_out);
        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                startActivity(new Intent(getActivity() , SignInActivity.class));
                getActivity().finish();
            }
        });


        mCancelBtn = view.findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return view ;
    }

}
