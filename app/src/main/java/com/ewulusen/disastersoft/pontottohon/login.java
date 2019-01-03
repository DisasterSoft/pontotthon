package com.ewulusen.disastersoft.pontottohon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class login  extends AppCompatActivity {
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    public TextView stat;
    public Button tovabb,singout,singin,singin2;
    String id;
    DatabaseReference mDatabase;
    databaseHelper userDB;
    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();
        stat=findViewById(R.id.statuslabel);
        tovabb=findViewById(R.id.revoke_access_button);
        singout=findViewById(R.id.sign_out_button);
        singin=findViewById(R.id.sign_in_button);
        singin2=findViewById(R.id.sign_in_button2);
        tovabb.setVisibility(View.INVISIBLE);
        singout.setVisibility(View.INVISIBLE);
        userDB=new databaseHelper(this);
        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bejelentkezés","");
                signIn();
            }
        });
        singout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                tovabb.setVisibility(View.INVISIBLE);
                singout.setVisibility(View.INVISIBLE);
                singin2.setVisibility(View.VISIBLE);
                singin.setVisibility(View.VISIBLE);
                stat.setText(getString(R.string.plssingin));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            singin.setVisibility(View.INVISIBLE);
            stat.setText(getString(R.string.next_from_login)+" "+user.getDisplayName());
            singin.setVisibility(View.INVISIBLE);
            singin2.setVisibility(View.INVISIBLE);
            tovabb.setVisibility(View.VISIBLE);
            singout.setVisibility(View.VISIBLE);
            tovabb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    FirebaseUser userF = mAuth.getCurrentUser();
                    String email = userF.getEmail();
                    String name = userF.getDisplayName();
                    userDB.login(email, name);
                    Intent intent2 = null;
                    intent2 = new Intent(login.this, mainscreen.class);
                    intent2.putExtra("datas", email);
                    startActivity(intent2);
                    finish();
                }

        });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
       // Log.d(TAG, "firebaseAuthWithGoogleZoli:" + acct.getId());
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            FirebaseUser userF = mAuth.getCurrentUser();
                            String email=userF.getEmail();
                            String name=userF.getDisplayName();
                            userDB.login(email,name);
                            Intent intent2 = null;
                            intent2=new Intent(login.this, mainscreen.class);
                            intent2.putExtra("datas", email);
                            startActivity(intent2);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //starting the activity for result
        Log.d("intent",""+signInIntent);

    }
    @Override
    protected void onStop() {
        super.onStop();


    }
}
/*
   mDatabase.child("user_"+id).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            if(dataSnapshot.exists() && user.id.equals(id)){
                                                Date now = new Date();
                                                SimpleDateFormat dateFormatter = new SimpleDateFormat("y-M-d' 'HH:m:s");
                                                mDatabase.child("user_"+id).child("last_login").setValue(dateFormatter.format(now).toString());
                                                Intent intent2 = null;
                                                intent2=new Intent(login.this, mainscreen.class);
                                                intent2.putExtra("datas", id);
                                                startActivity(intent2);
                                                finish();
                                            }else {
                                                finish();
                                                startActivity(new Intent(login.this, profile.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.d("hiba",""+databaseError);
                                        }
                                    });
 */