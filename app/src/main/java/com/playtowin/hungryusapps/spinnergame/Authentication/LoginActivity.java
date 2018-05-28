package com.playtowin.hungryusapps.spinnergame.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.ApiException;
import com.playtowin.hungryusapps.spinnergame.ForgotPasswordActivity;
import com.playtowin.hungryusapps.spinnergame.MainActivity;
import com.playtowin.hungryusapps.spinnergame.Models.User;
import com.playtowin.hungryusapps.spinnergame.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";
    private Context mContext = LoginActivity.this;

    //widgets
    private TextInputEditText mEmail,mPassword;
    private TextView mCreateAccount;
    private Button mLogin;
    private TextView mForgotPassword;
    private ProgressBar mProgressBar;
    //private GoogleSignInButton signInButton;
   // private TextView phoneAuth;

    //Google Sign In
    private static final int RC_SIGN_IN = 2;
    private GoogleApiClient mGoogleApiClient;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    DatabaseReference firebaseDatabase;

    //vars
    private String UserID;
    private ProgressDialog dialog;



    private void setupWidgets()
    {
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mCreateAccount = findViewById(R.id.create_account);
        mLogin = findViewById(R.id.login_button);
        mForgotPassword = findViewById(R.id.forgot_password);
        mProgressBar = findViewById(R.id.progressBar);
        //signInButton = findViewById(R.id.google_button);
        //phoneAuth = findViewById(R.id.phone_auth);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }//setupWidgets

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupFirebaseAuth();
        setupWidgets();


        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();


//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
//                    {
//                        Toast.makeText(mContext, "Google Sign in Failed!!", Toast.LENGTH_SHORT).show();
//                    }//onConnectionFailed
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
//                .build();

        init();

//        signInButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                dialog = new ProgressDialog(LoginActivity.this);
//                dialog.setTitle("Loading");
//                dialog.setMessage("Please Wait...");
//                dialog.show();
//
//                //signIn();
//            }//onClick
//        });

        mForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });





//        phoneAuth.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.d(TAG, "onClick: Phone Authentication");
//
//                startActivity(new Intent(mContext, PhoneAuthenticationActivity.class));
//            }
//        });//setOnClickListener

    }//onCreate


    //----------------------------------------------Firebase------------------------------------------------------------


    //Firebase Auth with Google


    private void signIn()
    {
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please Wait...");
        dialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }//signIn

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            UserID = user.getUid();
                            Log.d(TAG, "onComplete: Google Display Name: "+acct.getDisplayName());
                            Log.d(TAG, "onComplete: Google Given Name: "+acct.getGivenName());
                            Log.d(TAG, "onComplete: Google Email: "+acct.getEmail());

                            //check if user is signing for firstb time

                            Query query = FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.dbname_users))
                                    .child(user.getUid());


                            query.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    Log.d(TAG, "onDataChange: Datasnapshot: "+dataSnapshot);

                                    if(dataSnapshot.exists())
                                    {
                                        //Toast.makeText(mContext, "Not a First User", Toast.LENGTH_SHORT).show();
                                    }//if
                                    else
                                    {
                                        //Toast.makeText(mContext, "First User", Toast.LENGTH_SHORT).show();

                                        User user1 = new User(UserID,acct.getEmail(),acct.getDisplayName(),0);

                                        //add user google account info to user database
                                        firebaseDatabase.child(getString(R.string.dbname_users))
                                                .child(UserID)
                                                .setValue(user1);
                                    }//else
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            Toast.makeText(mContext, "Google Sign In Success", Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mContext, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//
//        Log.d(TAG, "onActivityResult: Google Request Code : "+requestCode);
//
//        if (requestCode == RC_SIGN_IN)
//        {
//            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if(result.isSuccess())
//            {
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//            }//if
//            else
//            {
//                Toast.makeText(mContext, "Google Login Failed", Toast.LENGTH_SHORT).show();
//            }//else
//
//        }//if
//    }//onActivityResult



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN)
//        {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try
//            {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            }
//            catch (ApiException e)
//            {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }



    private void signInWithEmailAndPassword(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.isSuccessful())
                        {
                            // Login  in success
                            Log.d(TAG, "signInWithEmail : Success");
                            Toast.makeText(mContext,R.string.auth_success,Toast.LENGTH_SHORT).show();
                            try
                            {

                                    Log.d(TAG, "onComplete: Email Verified for user : "+user.getEmail());
                                    Intent intent = new Intent(mContext,MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                            }//try
                            catch(NullPointerException e)
                            {
                                Log.d(TAG, "onComplete: NullPointer Exception : "+e.getMessage());
                            }//catch

                        }//if
                        else
                        {
                            // If Login  fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail : Failure", task.getException());
                            Toast.makeText(mContext,R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }//else

                    }//onComplete
                });//.addOnCompleteListener

    }//signInWithEmailAndPassword

    private boolean isStringNull(String s)
    {
        if(s.equals(""))
            return true;
        else
            return false;
    }//isStringNull

    private void init()
    {
        //initialize the button for logging in
        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick() Attempting to log in ");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                Log.d(TAG, "onClick: Login Email : "+email);
                Log.d(TAG, "onClick: Login password : "+password);

                if(isStringNull(email) && isStringNull(password))
                {
                    Toast.makeText(mContext,"You must fill out all fields",Toast.LENGTH_SHORT).show();
                }//if
                else
                {

                    mProgressBar.setVisibility(View.VISIBLE);
                    signInWithEmailAndPassword(email,password);

                }//else

            }//onClick
        });//setOnClickListener

        //link sign up
        mCreateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }//onClick
        });//setOnClickListener


        if(mAuth.getCurrentUser() != null)
        {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            //finish();
        }//if
    }//init

    private void checkCurrentUser(FirebaseUser user)
    {
        Log.d(TAG, "checkCurrentUser() -> Checking if user is logged in ");
        if(user != null)
            Log.d(TAG, "checkCurrentUser() -> Current UserID : "+user.getUid());
        if(user == null)
        {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }//if
    }//checkCurrentUser

    //set up the firebase auth object
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "setupFirebaseAuth() -> Setting Up Firebase Auth");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = mAuth.getCurrentUser();
                //checkCurrentUser(user);
                if(user != null)
                {//user is signed in
                    Log.d(TAG, "onAuthStateChanged: Signed In UserID : "+user.getUid());
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
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause: ");

        if(dialog != null)
        {
            dialog.dismiss();
            dialog.cancel();
        }//if

    }//onPause

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop()");
        super.onStop();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

    }//onStart

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");

        finish();
    }//onBackPressed

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }//onDestroy
}//LoginActivity
