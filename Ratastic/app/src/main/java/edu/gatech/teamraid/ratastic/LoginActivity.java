package edu.gatech.teamraid.ratastic;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.mindrot.jbcrypt.BCrypt;

import edu.gatech.teamraid.ratastic.Model.DataLogger;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginBtn = (Button) findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.usernameEdit)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordEdit)).getText().toString();
                if (validateCredentials(username, password)) {
                    Intent welcome = new Intent(LoginActivity.this, WelcomeActivity.class);
                    LoginActivity.this.startActivity(welcome);
                } else {
                    //handle unvalid login
                }
            }
        });

    }

    private boolean validateCredentials(String username, String password) {
        DataLogger credentials = new DataLogger(getApplicationContext());
        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt(4));
        SQLiteDatabase db = credentials.getReadableDatabase();
        String[] projection = {
                "Username",
                "Password"
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = "Username" + " = ?";
        String[] selectionArgs = { username };
        String sortOrder =
                "Username" + " DESC";
        Cursor cursor = db.query(
                "Credentials",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToNext();
        boolean valid = BCrypt.checkpw(password, cursor.getString(cursor.getColumnIndexOrThrow("Password")));
        cursor.close();
        return valid;
    }
}
