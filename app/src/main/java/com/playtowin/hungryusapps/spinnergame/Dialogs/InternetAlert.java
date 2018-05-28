package com.playtowin.hungryusapps.spinnergame.Dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.playtowin.hungryusapps.spinnergame.R;

public class InternetAlert extends DialogFragment
{
    private static final String TAG = "InternetAlert";

    private Button exit;


    private void setupWidgets(View view)
    {
        Log.d(TAG, "setupWidgets: ");
        exit = view.findViewById(R.id.exit);
    }//setupWidgets




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.internet_alert_dialog, container, false);
        setupWidgets(view);

        exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

        return view;
    }//onCreateView



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



}//InternetAlert
