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
        final EditText username = (EditText) findViewById(R.id.usernameEdit);
        final EditText password = (EditText) findViewById(R.id.passwordEdit);
        //addInitialUser(); //COMMENT THIS OUT AFTER YOU HAVE RAN IT ONCE!!!!!!!!!!!
        Button loginBtn = (Button) findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString();
                String passText = password.getText().toString();
                if (usernameText.equals("") || usernameText.equals("")) {
                    ((TextView) findViewById(R.id.failedLoginText)).setVisibility(View.VISIBLE);
                } else if (validateCredentials(usernameText, passText)) {
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(main);
                } else {
                    //handle unvalid login
                    ((TextView) findViewById(R.id.failedLoginText)).setVisibility(View.VISIBLE);
                }
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent canceled = new Intent(LoginActivity.this, WelcomeActivity.class);
                LoginActivity.this.startActivity(canceled);
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
        String[] selectionArgs = {username};
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
        if (cursor.getCount() > 0) {
            boolean valid = BCrypt.checkpw(password, cursor.getString(cursor.getColumnIndexOrThrow("Password")));
            cursor.close();
            return valid;
        } else {
            return false;
        }
    }

    private void addInitialUser() {
        DataLogger credentials = new DataLogger(getApplicationContext());
        String username = "admin";
        String pw_hash = BCrypt.hashpw("pass", BCrypt.gensalt(4));
        SQLiteDatabase db = credentials.getWritableDatabase();
        credentials.write(db, username, pw_hash);
    }
}

