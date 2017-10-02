package edu.gatech.teamraid.ratastic;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.mindrot.jbcrypt.BCrypt;

import edu.gatech.teamraid.ratastic.Model.DataLogger;
import edu.gatech.teamraid.ratastic.Model.User;
import edu.gatech.teamraid.ratastic.Model.User.UserType;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((TextView) findViewById(R.id.unverified)).setVisibility(View.GONE);
        final CheckBox adminCheckBox = (CheckBox) findViewById(R.id.adminCheck);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    if (User.currentUser == null) {
                        User.currentUser = new User(user.getDisplayName(), user.getEmail(),
                                user.getEmail(),
                                adminCheckBox.isChecked()
                                        ? UserType.ADMIN : UserType.USER);
                    }
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(main);
                } else if (user != null && !user.isEmailVerified()){
                    ((TextView) findViewById(R.id.unverified)).setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };
        final EditText email = (EditText) findViewById(R.id.emailEdit);
        final EditText password = (EditText) findViewById(R.id.passwordEdit);
//        if (!userExists("user")) {
//            addInitialUser();
//        }
        Button loginBtn = (Button) findViewById(R.id.signInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passText = password.getText().toString();
                if (emailText.equals("") || passText.equals("")) {
                    ((TextView) findViewById(R.id.failedLoginText)).setVisibility(View.VISIBLE);
                }
                mAuth.signInWithEmailAndPassword(emailText, passText)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    ((TextView) findViewById(R.id.failedLoginText)).setVisibility(View.VISIBLE);
                                }

                                // ...
                            }
                        });
//                if (emailText.equals("") || emailText.equals("")) {
//                    ((TextView) findViewById(R.id.failedLoginText)).setVisibility(View.VISIBLE);
//                } else if (validateCredentials(emailText, passText)) {
//                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
//                    LoginActivity.this.startActivity(main);
//                } else {
//                    //handle unvalid login
//                    ((TextView) findViewById(R.id.failedLoginText)).setVisibility(View.VISIBLE);
//                }
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
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //NEED TO STOP THIS WHEN LOG OUT
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private boolean userExists(String username) {
        DataLogger credentials = new DataLogger(getApplicationContext());
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
        return cursor.getCount() > 0;
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
        String username = "user";
        String pw_hash = BCrypt.hashpw("pass", BCrypt.gensalt(4));
        SQLiteDatabase db = credentials.getWritableDatabase();
        credentials.write(db, username, pw_hash);
    }
}

