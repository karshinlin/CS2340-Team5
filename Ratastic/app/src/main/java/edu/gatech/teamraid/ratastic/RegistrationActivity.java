package edu.gatech.teamraid.ratastic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.gatech.teamraid.ratastic.Model.User;

/**
 * Registration Page for Application. Linked to activity_registration.xml

 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 * 10/1/17:  KLIN     Created.
 * 10/9/17:  KLIN     Debugged Firebase Database calls to store all users/admins under child "users"
 *
 */

public class RegistrationActivity extends AppCompatActivity {

    /**
     * Firebase Auth instance variables
     */
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * User related instance variables
     */
    private String name;
    private String email;

    /**
     * Firebase Database instance variables
     */
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = mFirebaseDatabase.getReference("users");
    private static final String TAG = "RegistrationActivity";


    /**
     * Default onCreate
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final EditText passwordField = (EditText) findViewById(R.id.passwordField);
        final EditText nameField = (EditText) findViewById(R.id.nameField);
        final EditText emailField = (EditText) findViewById(R.id.emailField);
        Button submit = (Button) findViewById(R.id.submit);
        final CheckBox adminCheckbox =  (CheckBox) findViewById(R.id.checkBox);
        mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if (user.getDisplayName() == null) {
                        //Update display name in authentication profiles
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//
//                                        }
                                    }
                                });
                        String userId = user.getUid();
                        //update database to store name, email, and usertype
                        if (adminCheckbox.isChecked()) {
                            writeNewAdmin(userId, name, email);
                        } else {
                            writeNewUser(userId, name, email);
                        }
                    }
                    //send verification email initially
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
                    //navigate back to the WelcomeActivity
                    Intent main = new Intent(RegistrationActivity.this, WelcomeActivity.class);
                    RegistrationActivity.this.startActivity(main);
                    mAuth.signOut();

//                } else {
//                    // User is signed out
                }
                // ...
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                name = nameField.getText().toString();

                //make a new user account
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
//                                if (!task.isSuccessful()) {
//                                    //Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
//                                } else {
//
//                                }
                            }
                        });
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

    /**
     * Necessary onStop method
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Creates a regular user and stores into the Firebase database
     * @param userId unique ID of user
     * @param name provided name, also Display name in Auth profile
     * @param email username/email in Auth profile
     */
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, name, email, User.UserType.USER);
        myRef.child(userId).setValue(user);
    }

    /**
     * Creates a admin user and stores into the Firebase database
     * @param userId unique ID of user
     * @param name provided name, also Display name in Auth profile
     * @param email username/email in Auth profile
     */
    private void writeNewAdmin(String userId, String name, String email) {
        User user = new User(name, name, email, User.UserType.ADMIN);
        myRef.child(userId).setValue(user);
    }



}
