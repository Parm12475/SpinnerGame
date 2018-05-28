package com.playtowin.hungryusapps.spinnergame.Utils;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.Models.AccountDetail;
import com.playtowin.hungryusapps.spinnergame.Models.Feedback;
import com.playtowin.hungryusapps.spinnergame.Models.User;
import com.playtowin.hungryusapps.spinnergame.Models.UserSpins;
import com.playtowin.hungryusapps.spinnergame.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FirebaseMethods
{

    private static final String TAG = "FirebaseMethods";
    private Context mContext ;
    private String userID;
    private String userName = "";


    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    //firebase database
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;


    public FirebaseMethods(Context context)
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  mFirebaseDatabase.getReference();
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }//if

    }//FirebaseMethods




    public void addNewUser(String email,String name)
    {
        //add data to user table
        User user = new User(userID,email, name,0);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);

    }//addNewUser

    public void registerNewEmail(final String email, String password,final String name)
    {
        Log.d(TAG, "registerNewEmail() -> Registering New User ");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Registeration is successfull

                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = user.getUid();
                            Log.d(TAG, "onComplete() -> Registered User's UserID is : "+userID);

                            addNewUser(email,name);
                            mAuth.signOut();
                            Log.d(TAG, "createUserWithEmail() -> Registeration is Successfull");

                        }//if
                        else
                        {
                            //Registeration failed
                            Log.w(TAG, "createUserWithEmail() -> Registeration failed ", task.getException().getCause());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }//else


                    }//onComplete
                });

    }//registerNewEmail


    public void updateUserScore(long final_score)
    {
        //update user score in db
        Log.d(TAG, "onClick: Final Score: "+final_score);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mContext.getString(R.string.field_score)).setValue(final_score);
    }//updateUserScore

    public void addUserSpinInfo(long spin_score,String src)
    {
        //add spin info to db user_spins

        //Get Current Date
        java.util.Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);


        UserSpins spins = new UserSpins(String.valueOf(spin_score),formattedDate,src);

        String newSpinKey = myRef.child(mContext.getString(R.string.dbname_user_spins)).push().getKey();
        Log.d(TAG, "onAnimationEnd: New Spin ID: "+newSpinKey);
        myRef.child(mContext.getString(R.string.dbname_user_spins))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newSpinKey)
                .setValue(spins);
    }//addUserSpinInfo


    public void addUserFeedback(String feedback)
    {
        //Get Current Date
        java.util.Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Feedback userFeedback = new Feedback(feedback,formattedDate);

        String newSpinKey = myRef.child(mContext.getString(R.string.dbname_user_feedback)).push().getKey();
        myRef.child(mContext.getString(R.string.dbname_user_feedback))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(newSpinKey)
                .setValue(userFeedback);

    }//addUserFeedback

    public void addUserAccountDetail(String user_account_detail)
    {
        AccountDetail detail = new AccountDetail(user_account_detail);

        //String newSpinKey = myRef.child(mContext.getString(R.string.dbname_user_account_info)).push().getKey();
        myRef.child(mContext.getString(R.string.dbname_user_account_info))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(detail);
    }
}//FirebaseMethods
