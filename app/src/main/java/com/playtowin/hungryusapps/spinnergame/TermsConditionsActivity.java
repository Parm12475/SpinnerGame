package com.playtowin.hungryusapps.spinnergame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class TermsConditionsActivity extends AppCompatActivity 
{
    private static final String TAG = "TermsConditionsActivity";

    private TextView mTerms;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        mTerms = findViewById(R.id.terms);

        String para = "We are a non-profitable organization and operate independently." +
                "Our game is very fair and how much a user earns that amount is credited to user account " +
                "in the next 48 hours after reviewing user account details.\n\nIf we find some errors in account details " +
                "our team will immediately reach the user for making corrections.We dont generate any revenue through app purchases, if user play game and earns credits " +
                "then user will get its reward only if our app has generated enough revenue from its installations and downloads.\n\n" +
                "Currently, we don't have any in app purchase option but it will be in future for sure.\n\n" +
                "So the only way through which our app generates revenue is through adds monetization.A user gets its share from the revenue generated through adds.\n\n" +
                "In case our app is not able to generate enough revenue, we are very sorry to the user as we will not able to process payments at the moment and it may be detailed till app generates enough revenue for users using the app.\n\n\n" +
                "We thank our users for using our app and giving us their valueable comments in review section.\n";

        mTerms.setText(para);
        mTerms.setMovementMethod(new ScrollingMovementMethod());

    }//onCreate
}
