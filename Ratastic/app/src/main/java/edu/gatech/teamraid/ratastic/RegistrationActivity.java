package edu.gatech.teamraid.ratastic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText passwordField = (EditText) findViewById(R.id.passwordField);
        final EditText nameField = (EditText) findViewById(R.id.nameField);
        final EditText usernameField = (EditText) findViewById(R.id.usernameField);
        Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String name = nameField.getText().toString();
                //TODO make DB call
                RegistrationActivity.this.finish();
            }
        });

    }
}
