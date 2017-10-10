package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Welcome Page for Application. Linked to activity_welcome.xml

 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 * 10/1/17:  KLIN     Created.
 *
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button signInBtn = (Button) findViewById(R.id.signIn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSignIn = new Intent(WelcomeActivity.this, LoginActivity.class);
                WelcomeActivity.this.startActivity(goSignIn);
            }
        });

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRegister = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                WelcomeActivity.this.startActivity(goRegister);
            }
        });
    }
}
