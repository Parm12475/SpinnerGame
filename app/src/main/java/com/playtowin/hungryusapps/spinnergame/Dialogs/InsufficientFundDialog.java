package com.playtowin.hungryusapps.spinnergame.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.playtowin.hungryusapps.spinnergame.R;

public class InsufficientFundDialog extends DialogFragment
{

    private static final String TAG = "InsufficientFundDialog";
    private TextView mCancelDialog;


    public InsufficientFundDialog()
    {

    }//InsufficientFundDialog




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }//onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_insufficient_fund, container, false);

//        mCancelDialog = view.findViewById(R.id.dialogCancel);
//        mCancelDialog.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.d(TAG, "onClick: Closing Dialog...");
//                getDialog().dismiss();
//            }
//        });

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


}
