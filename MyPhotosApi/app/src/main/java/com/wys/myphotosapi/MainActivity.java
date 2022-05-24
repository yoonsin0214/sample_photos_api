package com.wys.myphotosapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;

    StartCardFragment startCardFragment;
    AllPhotoFragment allPhotoFragment;
    AlbumFragment albumFragment;
    FilterFragment filterFragment;
    FavoriteFragment favoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        mStatusTextView = findViewById(R.id.user_name);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()

                //OAuth 2.0 scope information for the Google Photos Library API: 우 링크에서 참고 https://developers.google.com/photos/library/guides/authorization
                .requestScopes(new Scope("https://www.googleapis.com/auth/photoslibrary.readonly"),
                        new Scope("https://www.googleapis.com/auth/photoslibrary.appendonly"),
                        new Scope("https://www.googleapis.com/auth/photoslibrary.readonly.appcreateddata"),
                        new Scope("https://www.googleapis.com/auth/photoslibrary.edit.appcreateddata"),
                        new Scope("https://www.googleapis.com/auth/photoslibrary"),
                        new Scope("https://www.googleapis.com/auth/photoslibrary.sharing"))
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        startCardFragment = new StartCardFragment();
        allPhotoFragment= new AllPhotoFragment();
        albumFragment = new AlbumFragment();
        filterFragment = new FilterFragment();
        favoriteFragment = new FavoriteFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
            onFragmentChange(1);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);//deprecated �޼��� ���� �ʿ�
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        onFragmentChange(0);
                        // [END_EXCLUDE]
                    }
                });

    }
    // [END signOut]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            mStatusTextView.setText(getString(R.string.username, account.getDisplayName()));
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.status_layout).setVisibility(View.VISIBLE);
            onFragmentChange(1); // need to check

        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.status_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    public void onFragmentChange(int index) {
        switch (index) {
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, startCardFragment).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, allPhotoFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,albumFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,filterFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,favoriteFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().remove(startCardFragment).commit();
        }
    }
}