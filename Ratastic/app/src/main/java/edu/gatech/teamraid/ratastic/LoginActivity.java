package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.teamraid.ratastic.Model.User;
import edu.gatech.teamraid.ratastic.Model.User.UserType;

/**
 * Login Page for Application. Linked to activity_login.xml

 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 * 10/1/17:  KLIN     Created.
 * 10/9/17:  KLIN     Configured Firebase database usage to capture userType
 *
 */

public class LoginActivity extends AppCompatActivity {

    /**
     * Firebase Auth instance variables
     */
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * Firebase Database instance variables
     */
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = mFirebaseDatabase.getReference("users");

    /**
     * Default onCreate
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.unverified).setVisibility(View.GONE);

        mAuth.signOut();

        /*
         * On Authentication state changed. Will update current user.
         * Logs User in and sets User.currentUser singleton.
         * Transitions to the MainActivity
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if ((user != null) && (user.isEmailVerified())) {
                    DatabaseReference currentUser = myRef.child(user.getUid());
                    currentUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            //String value = dataSnapshot.getValue(String.class);
                            try {
                                Map map = (HashMap) dataSnapshot.getValue();
                                User.setInstance(new User (user.getDisplayName(), user.getEmail(),
                                        user.getEmail(),
                                        UserType.getUserType(map.get("userType").toString())));
                            } catch (Throwable e) {
                                Log.d("FINE", "Unable to retrieve current user");
                            }
                            Intent main = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(main);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });

                } else if ((user != null) && !(user.isEmailVerified())){
                    findViewById(R.id.unverified).setVisibility(View.VISIBLE);
                    //resends verification email
                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            //Log.d(TAG, "Email sent.");
//                                        }
                                    }
                                });
                    }
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };
        final EditText email = (EditText) findViewById(R.id.emailEdit);
        final EditText password = (EditText) findViewById(R.id.passwordEdit);
        Button loginBtn = (Button) findViewById(R.id.signInBtn);
        //handler for clicking login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passText = password.getText().toString();
                if (emailText.isEmpty() || passText.isEmpty()) {
                    TextView failedView = (TextView) findViewById(R.id.failedLoginText);
                    failedView.setText(R.string.unsuccessfulLogin);
                } else {
                    TextView succeededView = (TextView) findViewById(R.id.failedLoginText);
                    succeededView.setText(R.string.successfulLogin);
                    mAuth.signInWithEmailAndPassword(emailText, passText)
                            .addOnCompleteListener(LoginActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            // If sign in fails, display a message to the user.
                                            // If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                TextView failedView = (TextView) findViewById(R.id.failedLoginText);
                                                failedView.setText(R.string.unsuccessfulLogin);
                                                //                                } else {
                                                //final FirebaseUser user = mAuth.getCurrentUser();
                                                //                                    if (user != null) {
                                                //                                    }
                                            }
                                        }
                                    });
                }
                }
        });
        Button cancelBtn = (Button) findViewById(R.id.cancelButton);
        //cancel button will bring user back to welcome page
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent canceled = new Intent(LoginActivity.this, WelcomeActivity.class);
                LoginActivity.this.startActivity(canceled);
            }
        });

    }

    /**
     * Necessary onStart method.
     * Binds the AuthListener to the Firebase Auth instance
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //NEED TO STOP THIS WHEN LOG OUT

    /**
     * Method to stop this activity
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

