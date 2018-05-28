package com.playtowin.hungryusapps.spinnergame;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.playtowin.hungryusapps.spinnergame.Authentication.LoginActivity;
import com.playtowin.hungryusapps.spinnergame.Utils.BottomNavigationViewHelper;
import com.playtowin.hungryusapps.spinnergame.Utils.Constants;
import com.playtowin.hungryusapps.spinnergame.Utils.FirebaseMethods;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener
{
    private static final String TAG = "MainActivity";
    private Context mContext = MainActivity.this;
    private int ACTIVITY_NUM = 0;
    public static final String APP_PNAME = "com.playtowin.hungryusapps.spinnergame";


    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //firebase database
    DatabaseReference myRef;
    FirebaseMethods methods;

    //widgets
    private TextView mPoints;
    private NavigationView mNavigationView;
    private AlertDialog.Builder builder ;
    private TextView mName;
    private CardView mPlay,mBonus,mHistory,mDailyCheck,mPayment;


    //vars
    private long user_score = 0;
    private String user_name = "";


    //Navigation Drawer
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    //adds
    private RewardedVideoAd rewardedVideoAd;

    private void setupWidgets()
    {
        myRef = FirebaseDatabase.getInstance().getReference();
       // mGridView = findViewById(R.id.mainGrid);
        mPoints = findViewById(R.id.points);
        mNavigationView = findViewById(R.id.navigation_menu);
        builder = new AlertDialog.Builder(this);
        methods = new FirebaseMethods(mContext);

        mPlay = findViewById(R.id.play);
        mBonus = findViewById(R.id.bonus);
        mHistory = findViewById(R.id.history);
        mDailyCheck = findViewById(R.id.daily_check);
        mPayment = findViewById(R.id.payment);

    }//setupWidgets

    private void getPointsCount()
    {
        Log.d(TAG, "getPointsCount: Getting Points from DB");

        FirebaseUser user = mAuth.getCurrentUser();

        Log.d(TAG, "getPointsCount: User ID: "+user.getUid());

        if(user != null)
        {
            Query query = myRef.child(getString(R.string.dbname_users))
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            query.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Log.d(TAG, "onDataChange: DataSnapshot: "+dataSnapshot);

                    if(dataSnapshot.getValue() != null)
                    {
                        String score = dataSnapshot.child(getString(R.string.field_score)).getValue().toString();

                        Log.d(TAG, "onDataChange: User Score From DB: "+score);
                        user_score = Long.parseLong(score);
                        Log.d(TAG, "onDataChange: User Score: "+user_score);

                        mPoints.setText(String.valueOf(user_score));

                        String name = dataSnapshot.child(getString(R.string.field_name)).getValue().toString();
                        user_name = name;
                        Log.d(TAG, "onDataChange: Name: "+name);
                        mName.setText(name);
                        Log.d(TAG, "onDataChange: Points: "+mPoints.getText());
                        Log.d(TAG, "onDataChange: Name: "+user_name);
                    }//if
                    else
                    {
                        Log.d(TAG, "onDataChange: Datasnapshot NULL");
                    }//else

                }//onDataChange

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }//onCancelled
            });
        }//if


    }//getCoinsCount

    private void setUpToolbar()
    {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }//setUpToolbar

    private void setupNavigationDrawer()
    {
        navigationView = findViewById(R.id.navigation_menu);
        View header = navigationView.inflateHeaderView(R.layout.header);
        mName = header.findViewById(R.id.user_name);
        mName.setText(user_name);
        //mName = (TextView) navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        startActivity(new Intent(mContext,MainActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.nav_rate_app:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.nav_feedback:
                        Intent intent = new Intent(mContext,FeedbackActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.nav_contact_us:
                        startActivity(new Intent(mContext,ContactUsActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.nav_terms_conditions:
                        startActivity(new Intent(MainActivity.this,TermsConditionsActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                    case R.id.nav_logout:
                        Intent intent5 = new Intent(mContext, SignOutActivity.class);
                        mContext.startActivity(intent5);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        break;
                }//switch

                return false;

            }//onNavigationItemSelected
        });

        setUpToolbar();
    }//setupNavigationDrawer

    private void handleListenerOnPlayCardview()
    {
        Log.d(TAG, "handleListenerOnPlayCardview: ");
        mPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Navigating to Spinner Fragment!");

                Intent intent = new Intent(mContext,SpinnerActivity.class);
                intent.putExtra("user_score",String.valueOf(user_score));
                intent.putExtra("user_name",user_name);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }//handleListenerOnPlayCardview


    private void handleListenerOnBonusCardview()
    {
        Log.d(TAG, "handleListenerOnBonusCardview: ");

        mBonus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadRewardedAdds();
                startRewardedVideoAdd();
            }
        });
    }//handleListenerOnBonusCardview

    private void handleListenerOnHistoryCardview()
    {
        Log.d(TAG, "handleListenerOnHistoryCardview: ");

        mHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Navigating to History Fragment");
                Intent i = new Intent(mContext,HistoryActivity.class);
                i.putExtra("user_score",String.valueOf(user_score));
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }//handleListenerOnHistoryCardview

    private void handleListenerOnDailyCheckInCardview()
    {
        Log.d(TAG, "handleListenerOnDailyCheckInCardview: ");

        mDailyCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadRewardedAdds();
                startRewardedVideoAdd();
            }
        });
    }//handleListenerOnDailyCheckInCardview

    private void handleListenerOnPaymentCardview()
    {
        mPayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext,WithdrawActivity.class);
                intent.putExtra("user_score",String.valueOf(user_score));
                intent.putExtra("user_name",user_name);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }//handleListenerOnPaymentCardview





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String app_id = getString(R.string.test_admob_app_id);


        setupFirebaseAuth();
        setupWidgets();



        //initialize Mobile SDK
        MobileAds.initialize(this, app_id);

        //initialize rewarded video adds
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        rewardedVideoAd.setRewardedVideoAdListener(MainActivity.this);


        if(mAuth.getCurrentUser() != null)
        {
            mPoints.setText("wait...");
            getPointsCount();
        }//if

        setupNavigationDrawer();
        setUpToolbar();
        //setSingleEventOnGridView(mGridView);
        setupBottomNavigationView();

        getNotificationChannel();
        handleListenerOnPlayCardview();
        handleListenerOnBonusCardview();
        handleListenerOnHistoryCardview();
        handleListenerOnDailyCheckInCardview();
        handleListenerOnPaymentCardview();


    }//onCreate

    private void getNotificationChannel()
    {
        NotificationManager manager =(NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID,
                                    Constants.CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});

            manager.createNotificationChannel(mChannel);
        }//if
    }//getNotificationChannel


    private void startRewardedVideoAdd()
    {
        Log.d(TAG, "startRewardedVideoAdd: ");

        if(rewardedVideoAd.isLoaded())
        {
            rewardedVideoAd.show();
        }//if

    }//startRewardedVideoAdd


    private void loadRewardedAdds()
    {
        Log.d(TAG, "loadRewardedAdds: Loading Rewarded Adds...");

        if(!rewardedVideoAd.isLoaded())
        {
            rewardedVideoAd.loadAd(getString(R.string.test_admob_rewarded_id),new AdRequest.Builder().build());

        }//if

    }//loadRewardedAdds



    //Responsible for Bottom Navigation View Setup
    private void setupBottomNavigationView()
    {
        Log.d(TAG, "setupBottomNavigationView: setting Up Bottom Navigation View !");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);

        BottomNavigationViewHelper.setupNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this,bottomNavigationViewEx);



        int selectedItemId = bottomNavigationViewEx.getSelectedItemId();
        MenuItem selectedItem = bottomNavigationViewEx.getMenu().findItem(selectedItemId);


    }//setupBottomNavigationView

       /*----------------------------------------------Firebase------------------------------------------------------------ */

    //set up the firebase auth object
    private void setupFirebaseAuth()
    {

        Log.d(TAG, "setupFirebaseAuth()-> Setting Up firebase Authentication");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged: CurrentUser : "+user);

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

    private void checkCurrentUser(FirebaseUser user)
    {
        Log.d(TAG, "checkCurrentUser() -> Checking if user is logged in ");
        if(user != null)
            Log.d(TAG, "checkCurrentUser() -> Current UserID : "+user.getUid());
        if(user == null)
        {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }//if
    }//checkCurrentUser


    @Override
    public void onStart()
    {
        Log.d(TAG, "onStart() ");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());

    }//onStart

    @Override
    protected void onPostResume()
    {
        Log.d(TAG, "onPostResume: ");
        rewardedVideoAd.resume(this);
        super.onPostResume();
    }

    @Override
    protected void onPause()
    {
        Log.d(TAG, "onPause: ");
        rewardedVideoAd.pause(this);
        super.onPause();

    }

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
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();

       finish();

    }//onBackPressed

    @Override
    protected void onRestart()
    {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
    }

    @Override
    protected void onDestroy()
    {
        Log.d(TAG, "onDestroy: ");
        rewardedVideoAd.destroy(this);
        super.onDestroy();

    }


    @Override
    public void onRewardedVideoAdLoaded()
    {

    }

    @Override
    public void onRewardedVideoAdOpened()
    {

    }

    @Override
    public void onRewardedVideoStarted()
    {

    }

    @Override
    public void onRewardedVideoAdClosed()
    {
        Log.d(TAG, "onRewardedVideoAdClosed: ");
        //loadRewardedAdds();

    }//onRewardedVideoAdClosed

    @Override
    public void onRewarded(RewardItem rewardItem)
    {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Congratulations! You Got 500 Points");
        builder.setMessage("Claim Your Points");
        builder.setCancelable(false);
        builder.setPositiveButton("IGNORE", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();

            }//onClick
        });

        builder.setNegativeButton("CLAIM", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //provide 500 coins to the user score
                user_score = user_score + 500;
                myRef.child(mContext.getString(R.string.dbname_users))
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getString(R.string.field_score)).setValue(String.valueOf(user_score));


                //add spin history to user_spin db
                methods.addUserSpinInfo(500,"Rewarded Points");

            }//onClick
        });

        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }//onRewarded

    @Override
    public void onRewardedVideoAdLeftApplication()
    {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        builder.setMessage("No video available, try again later!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        builder.create();
        builder.show();
    }//onRewardedVideoAdFailedToLoad




}
