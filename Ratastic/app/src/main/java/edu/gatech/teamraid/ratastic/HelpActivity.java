package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class HelpActivity extends AppCompatActivity {
    /**
     * Firebase Auth instance variables
     */
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final EditText email = (EditText) findViewById(R.id.emailForReset);
        Button sendReset = (Button) findViewById(R.id.sendReset);
        sendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty()) {
                    mAuth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        final TextView successfulSend = (TextView) findViewById(R.id.resetEmailSent);
                                        successfulSend.setVisibility(View.VISIBLE);

                                    } else {
                                        final TextView successfulSend = (TextView) findViewById(R.id.resetEmailNotSent);
                                        successfulSend.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                } else {
                    final TextView successfulSend = (TextView) findViewById(R.id.resetEmailNotSent);
                    successfulSend.setVisibility(View.VISIBLE);
                }

            }
        });


        Button backBtn = (Button) findViewById(R.id.backButtonForReset);
        //back button will bring user back to welcome page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent canceled = new Intent(HelpActivity.this, WelcomeActivity.class);
                HelpActivity.this.startActivity(canceled);
            }
        });
    }
}
