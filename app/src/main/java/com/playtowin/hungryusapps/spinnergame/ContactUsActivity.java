package com.playtowin.hungryusapps.spinnergame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class ContactUsActivity extends AppCompatActivity
{
    private static final String TAG = "ContactUsActivity";

    private ImageView mBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mBackArrow = findViewById(R.id.backArrow);

        mBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                finish();
            }
        });



    }//onCreate
}//ContactUsActivity
