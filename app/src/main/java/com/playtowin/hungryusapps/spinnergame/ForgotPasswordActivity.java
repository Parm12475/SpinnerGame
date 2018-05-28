package com.playtowin.hungryusapps.spinnergame;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{
    private static final String TAG = "ForgotPasswordActivity";

    private EditText mForgotEmail;
    private Button mResetPassword;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mForgotEmail = findViewById(R.id.forgot_password_email);
        mResetPassword = findViewById(R.id.password_reset_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        mResetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: ");
                String userEmail = mForgotEmail.getText().toString().trim();

                if(userEmail.equals(""))
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                }//if
                else
                {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            Log.d(TAG, "onComplete: ");

                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Password Reset Email Sent!!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                            }//if
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Error in sending password reset email!", Toast.LENGTH_SHORT).show();
                            }//else

                        }//onComplete
                    });//sendPasswordResetEmail
                }//else

            }//onClick
        });//mResetPassword.setOnClickListener
    }
}
