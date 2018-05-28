package com.playtowin.hungryusapps.spinnergame.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.playtowin.hungryusapps.spinnergame.FeedbackActivity;
import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.BaseBackPressedListener;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.wafflecopter.charcounttextview.CharCountTextView;


public class FeedbackFragment extends Fragment
{
    private static final String TAG = "FeedbackFragment";

    private FirebaseMethods firebaseMethods;
    private String feedback;
    private CardView feedback_btn;
    private ImageView mBackArrow;
    private CharCountTextView countTextView;
    private  EditText inputFeedback;


    public FeedbackFragment()
    {
    }

    private void handleListenerOnBackArrow()
    {
        Log.d(TAG, "handleListenerOnBackArrow: ");
        mBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });
    }//handleListenerOnBackArrow

    private void setupWidgets(View view)
    {
        Log.d(TAG, "setupWidgets: ");
        firebaseMethods = new FirebaseMethods(getContext());
        feedback_btn = view.findViewById(R.id.feedback_btn);
        mBackArrow = view.findViewById(R.id.backArrow);
        inputFeedback = view.findViewById(R.id.input_feedback);
    }//setupWidgets


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView: ");

        ((FeedbackActivity)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity()));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        setupWidgets(view);
        handleListenerOnBackArrow();

        countTextView = view.findViewById(R.id.char_count);
        countTextView.setEditText(inputFeedback);
        countTextView.setCharCountChangedListener(new CharCountTextView.CharCountChangedListener()
        {
            @Override
            public void onCountChanged(int i, boolean b)
            {
                Log.d(TAG, "onCountChanged: ");
            }
        });

        feedback_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                feedback = inputFeedback.getText().toString();
                Log.d(TAG, "onCreateView: Feedback by User: "+feedback);

               if(!feedback.equals(""))
               {
                   Log.d(TAG, "onClick: ");
                   addUserFeedback();

//                   CustomAlertDialog dialog = new CustomAlertDialog();
//                   dialog.show(getFragmentManager(),"CustomAlertDialog");
//                   dialog.setTargetFragment(FeedbackFragment.this,1);
                   Toast.makeText(getContext(), "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
               }//if
                else
               {
                   Log.d(TAG, "onClick: Feedback is Null");
                   AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                   View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_input_error,null);

                   builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
                   {
                       @Override
                       public void onClick(DialogInterface dialog, int which)
                       {
                           dialog.dismiss();
                       }
                   });

                   builder.setView(view);
                   builder.show();
               }//else
            }
        });

        return view;

    }//onCreateView


    private void addUserFeedback()
    {
        Log.d(TAG, "addUserFeedback: Adding User Feedback to User!");

        feedback = inputFeedback.getText().toString();
        firebaseMethods.addUserFeedback(feedback);
    }//addUserFeedback



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();

    }


}
