package com.playtowin.hungryusapps.spinnergame.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.playtowin.hungryusapps.spinnergame.R;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity
{
    private static final String TAG = "RegisterActivity";
    private Context mContext = RegisterActivity.this;
    private FirebaseMethods firebaseMethods;
    private String email,name,password;


    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;



    //widgets
    EditText mName,mEmail,mPassword;
    Button mRegister;
    TextView mLogin;
    ProgressBar mProgressBar;


    private void setupWidgets()
    {
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegister = findViewById(R.id.register_button);
        mLogin = findViewById(R.id.login_link);
        mProgressBar = findViewById(R.id.progressBar);
        firebaseMethods = new FirebaseMethods(mContext);

    }//setupWidgets

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupFirebaseAuth();
        setupWidgets();
        init();


    }//onCreate

    private boolean isStringNull(String s)
    {
        if(s.equals(""))
            return true;
        else
            return false;
    }//isStringNull


    private boolean checkInputs(String email,String username,String password)
    {
        Log.d(TAG, "checkInputs() -> Checking Inputs for Registering user");
        if( isStringNull(email) || isStringNull(username) || isStringNull(password) )
        {
            Toast.makeText(mContext, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return true;
        }//if
        return true;
    }//checkInputs

    private void init()
    {
        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = mName.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                Log.d(TAG, "onClick() -> Email entered by user : "+email);
                Log.d(TAG, "onClick() -> Name entered by user : "+name);
                Log.d(TAG, "onClick() -> Password entered by user : "+password);

                mProgressBar.setVisibility(View.VISIBLE);

                if(checkInputs(email,name,password))
                {
                    firebaseMethods.registerNewEmail(email,password,name);

                }//if


            }
        });

        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }//onClick
        });//setOnClickListener
    }//init






    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth() -> Setting Up Firebase Auth");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null)
                {//user is signed in
                    Log.d(TAG, "onAuthStateChanged() -> Signed In UserID : "+user.getUid());

                    finish();
                }//if
                else
                {//User is signed out
                    Log.d(TAG, "onAuthStateChanged: Signed Out");
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
    }//onStop

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
        finish();
    }//onBackPressed

}//RegisterActivity
