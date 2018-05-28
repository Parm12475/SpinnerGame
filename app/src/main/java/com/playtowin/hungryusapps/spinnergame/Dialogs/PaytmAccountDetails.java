package com.playtowin.hungryusapps.spinnergame.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;

public class PaytmAccountDetails extends DialogFragment
{
    private static final String TAG = "PaytmAccountDetails";
    //widgets
    private TextInputEditText mPaytmDetails;
    private CardView mSubmitBtn;

    //vars
    private FirebaseMethods firebaseMethods;



    public PaytmAccountDetails()
    {
        super();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }//onCreate

    public static boolean isValidPhoneNo(CharSequence iPhoneNo)
    {
        return !TextUtils.isEmpty(iPhoneNo) &&
                Patterns.PHONE.matcher(iPhoneNo).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_paytm_account_details, container, false);
        setupWidgets(view);

        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                String phone = mPaytmDetails.getText().toString();

                if(!phone.equals("") && isValidPhoneNo(phone))
                {
                    firebaseMethods.addUserAccountDetail(phone);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage("Our team will check your account details and your payment will be processed within 48 hours.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            startActivity( new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    });

                    builder.create();
                    builder.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setMessage("Field Empty!");
                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    });

                    builder.create();
                    builder.show();
                }//else


            }
        });

        return view;
    }//onCreateView

    private void setupWidgets(View view)
    {
        Log.d(TAG, "setupWidgets: ");
        mPaytmDetails = view.findViewById(R.id.paytm_account_detail);
        mSubmitBtn = view.findViewById(R.id.submit_btn);
        firebaseMethods = new FirebaseMethods(getContext());

    }//setupWidgets


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }//onAttach

    @Override
    public void onDetach()
    {
        super.onDetach();
    }//onDetach



}//PaytmAccountDetails
