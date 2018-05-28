package com.playtowin.hungryusapps.spinnergame.Dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;

public class PaypalAccountDetails extends DialogFragment
{
    private static final String TAG = "PaytmAccountDetails";
    //widgets
    private TextInputEditText mPaypalDetails;
    private CardView mSubmitBtn;

    //vars
    private FirebaseMethods firebaseMethods;


    public PaypalAccountDetails()
    {
        super();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_paypal_account_details, container, false);
        setupWidgets(view);

        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");



                String email = mPaypalDetails.getText().toString();

                if(!email.equals(""))
                {
                    firebaseMethods.addUserAccountDetail(email);

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
        mPaypalDetails = view.findViewById(R.id.paypal_account_detail);
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
