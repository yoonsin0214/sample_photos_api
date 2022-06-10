package com.wys.myphotosapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    StartCardFragment startCardFragment;
    AllPhotoFragment allPhotoFragment;
    AlbumFragment albumFragment;
    FilterFragment filterFragment;
    FavoriteFragment favoriteFragment;

    Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OAuthLogin();
            }
        });

        startCardFragment = new StartCardFragment();
        allPhotoFragment = new AllPhotoFragment();
        albumFragment = new AlbumFragment();
        filterFragment = new FilterFragment();
        favoriteFragment = new FavoriteFragment();

    }

    private void OAuthLogin(){
        Intent OAuthLogin = new Intent(MainActivity.this, GoogleOAuthActivity.class);
        startActivity(OAuthLogin);
        findViewById(R.id.button_login).setVisibility(View.INVISIBLE);
        onFragmentChange(1);
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
                        .replace(R.id.container, albumFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, filterFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, favoriteFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().remove(startCardFragment).commit();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null).commit();
    }
}