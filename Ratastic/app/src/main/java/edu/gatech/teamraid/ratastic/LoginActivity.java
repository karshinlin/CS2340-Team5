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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import edu.gatech.teamraid.ratastic.Model.DataLogger;
import edu.gatech.teamraid.ratastic.Model.User;
import edu.gatech.teamraid.ratastic.Model.User.UserType;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

//    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference myRef = mFirebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((TextView) findViewById(R.id.unverified)).setVisibility(View.GONE);
        final CheckBox adminCheckBox = (CheckBox) findViewById(R.id.adminCheck);

        mAuth.signOut();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Intent main = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(main);
                } else if (user != null && !user.isEmailVerified()){
                    ((TextView) findViewById(R.id.unverified)).setVisibility(View.VISIBLE);
                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Log.d(TAG, "Email sent.");

                                        }
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
                                } else {
                                    final FirebaseUser user = mAuth.getCurrentUser();
//                                    if (user != null) {
//                                        DatabaseReference countsDb = myRef.child("accountsTable");
//                                        countsDb.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot snapshot) {
//
//                                                try{
//
//                                                    User currUser  = snapshot.getChildren().iterator().next()
//                                                            .getValue(User.class);
//                                                    if (currUser != null && User.currentUser == null ) {
//                                                        User.currentUser = new User(user.getDisplayName(), user.getEmail(),
//                                                                user.getEmail(), currUser.getUserType());
//                                                    }
//                                                } catch (Throwable e) {
//                                                    Log.d("FINE", "Unable to retrieve current user");
//                                                }
//                                            }
//                                            @Override public void onCancelled(DatabaseError error) { }
//                                        });
//                                    }

                                }

                                // ...
                            }
                        });
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

}

