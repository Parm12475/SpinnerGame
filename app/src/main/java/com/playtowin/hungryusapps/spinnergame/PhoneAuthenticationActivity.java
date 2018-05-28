package com.playtowin.hungryusapps.spinnergame;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class PhoneAuthenticationActivity extends AppCompatActivity
{
    private static final String TAG = "PhoneAuthenticationActi";
    private Context mContext = PhoneAuthenticationActivity.this;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    DatabaseReference myRef;
    FirebaseMethods firebaseMethods;

    //widgets
    private TextInputEditText mobile;
    private Button sendCode;
    private TextInputEditText enteredCode;
    private Button signIn;

    //vars
    private String codeSent;


    private void setupWidgets()
    {
        Log.d(TAG, "setupWidgets: Setting Widgets...");

        mobile = findViewById(R.id.enterMobile);
        sendCode = findViewById(R.id.sendCode);
        enteredCode = findViewById(R.id.enterCode);
        signIn = findViewById(R.id.signIn);
    }//setupWidgets

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        setupFirebaseAuth();
        setupWidgets();

        sendCode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                sendVerificationCode();
            }//onClick
        });

        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");

                verifySignInCode();

            }
        });//setOnClickListener


        mAuth = FirebaseAuth.getInstance();
    }//onCreate

    private void verifySignInCode()
    {
        Log.d(TAG, "verifySignInCode: ");

        String code  = enteredCode.getEditableText().toString();

        Log.d(TAG, "verifySignInCode: Code Sent: "+codeSent);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }//verifySignInCode

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, "onComplete: User ID: "+user.getUid());
                            Log.d(TAG, "onComplete: User Details...");
                            Log.d(TAG, "onComplete: Email = "+user.getEmail());
                            Log.d(TAG, "onComplete: Display Name = "+user.getDisplayName());
                            Log.d(TAG, "onComplete: Mobile = "+user.getPhoneNumber());

                            startActivity(new Intent(mContext,MainActivity.class));


                            Toast.makeText(PhoneAuthenticationActivity.this, "Phone Auth Success", Toast.LENGTH_SHORT).show();
                        }//if
                        else
                            {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                // The verification code entered was invalid

                                Toast.makeText(PhoneAuthenticationActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                            }//if
                        }
                    }
                });
    }//signInWithPhoneAuthCredential

    private void sendVerificationCode()
    {
        Log.d(TAG, "sendVerificationCode: ");

        String phoneNumber = mobile.getEditableText().toString();
        Log.d(TAG, "sendVerificationCode: User Mobile Number: "+phoneNumber);



        if(phoneNumber.isEmpty())
        {
            mobile.setError("Mobile is Empty");
            mobile.requestFocus();
            Toast.makeText(this, "Phone is empty", Toast.LENGTH_SHORT).show();
            return;
        }//if

        if(phoneNumber.length() < 10)
        {
            mobile.setError("Enter a valid phone number");
            mobile.requestFocus();
            Toast.makeText(this, "Invalid Phone", Toast.LENGTH_SHORT).show();
            return;
        }//if

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                Log.d(TAG, "onVerificationCompleted: ");
            }//onVerificationCompleted

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Log.d(TAG, "onVerificationFailed: ");
            }//onVerificationFailed

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s, forceResendingToken);
                Log.d(TAG, "onCodeSent: ");

                codeSent = s;
            }
        };//OnVerificationStateChangedCallbacks


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks



    }//sendVerificationCode


    //set up the firebase auth object
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth()-> Setting Up Firebase Authentication");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged: CurrentUser : "+user);
                Log.d(TAG, "onAuthStateChanged: ");
                //check if user is logged in

                if(user != null)
                {//user is signed in
                    Log.d(TAG, "onAuthStateChanged() -> Signed In Used ID : "+user.getUid());
                }//if
                else
                {//User is signed out
                    Log.d(TAG, "onAuthStateChanged() -> Signed Out");
                }//else

            }//onAuthStateChanged
        };

    }//setupFirebaseAuth


    @Override
    public void onStart()
    {
        Log.d(TAG, "onStart() ");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }//onStart

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop()");
        super.onStop();

        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

    }//onStart


}//PhoneAuthenticationActivity
