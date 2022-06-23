package com.example.chatroom.SignIn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatroom.Home.HomeScreen;
import com.example.chatroom.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserSignInAndDetails extends AppCompatActivity {
    String googleClientID = "99359494705-qgspbu8gatab9p534r3ug8ql43utvbl0.apps.googleusercontent.com";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    GoogleSignInOptions gso;
    GoogleSignInClient client;
    GoogleSignInAccount account;
    FirebaseAuth loginAuth = FirebaseAuth.getInstance();
    Button createAccount;
    SharedPreferences sharedPreferences;
    EditText email,password,userName;
    SignInButton signInButton;
    FirebaseUser currentUser;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in_and_details);
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initialise();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(UserSignInAndDetails.this,HomeScreen.class));
            finish();
        }
        signInButton.setOnClickListener(click -> {
            Intent signIn = client.getSignInIntent();
            startActivityForResult(signIn,3);
        });

        createAccount.setOnClickListener(click -> {

            if(userName.getText().toString().equals("")){
                Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                userName.requestFocus();
                userName.setError("Error");
                return;
            }


            if(email.getText().toString().equals("")){
                Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                email.requestFocus();
                email.setError("Error");
                return;
            }

            if(password.length() < 6){
                Toast.makeText(this, "Minimum 6 words required", Toast.LENGTH_SHORT).show();
                password.requestFocus();
                password.setError("Error");
                return;
            }

            createAccountWithEmailAndPassword(email.getText().toString(),password.getText().toString(),userName.getText().toString());
        });

    }

    private void createAccountWithEmailAndPassword(String email, String password,String userName) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserSignInAndDetails.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    GoogleSignInDB googleSignInDB = new GoogleSignInDB(userName,email);
                    databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getUid());
                    databaseReference.setValue(googleSignInDB);
                    editor.putString("email",email);
                    editor.putString("name",userName);
                    editor.apply();
                    startActivity(new Intent(UserSignInAndDetails.this,HomeScreen.class));
                    finish();
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(UserSignInAndDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    private void initialise() {
        signInButton = findViewById(R.id.sign_in_button);
        email = findViewById(R.id.editTextTextEmailAddress);
        userName = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);
        createAccount = findViewById(R.id.createAccountButtonUserSignIn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleClientID)
                .requestEmail()
                .build();
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

        client = GoogleSignIn.getClient(UserSignInAndDetails.this,gso);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == 3){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // handle google sign in results
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Sign In", Toast.LENGTH_SHORT).show();
            createFirebaseAuthID(account.getIdToken());
            getSignInInformation();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, e.getLocalizedMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFirebaseAuthID(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        loginAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        loginAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = loginAuth.getCurrentUser();
                        assert user != null;
                        loginAuth.updateCurrentUser(user);
                        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();
                        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(!snapshot.hasChild(Objects.requireNonNull(loginAuth.getUid()))){
//                                        reference.child("Users").child(Objects.requireNonNull(loginAuth.getUid())).setValue(user);
                                    databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getUid());
                                    GoogleSignInDB googleSignInDB = new GoogleSignInDB(account.getDisplayName(),account.getEmail());
                                    databaseReference.setValue(googleSignInDB);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        startActivity(new Intent(UserSignInAndDetails.this, HomeScreen.class));
//                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                    }
                });
    }

    private void getSignInInformation() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            editor.putString("email",personEmail);
            editor.putString("name",personName);
            editor.apply();
            Log.i("info",personName+ " " + personEmail + " " + personFamilyName);
        }
    }
}