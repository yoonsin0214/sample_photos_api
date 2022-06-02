package com.wys.myphotosapi;

import android.content.Context;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.collect.ImmutableList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;


public class GoogleOAuthFactory {
    private static final String TAG = "YS GoogleOAuthFactory";

    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final int LOCAL_RECEIVER_PORT = 9999;
    public static final String USER_ID = "user";
    public static final String ANDROID_VER = android.os.Build.VERSION.RELEASE;
    public static final String CUSTOM_USER_AGENT = "--user-agent=Mozilla/5.0 (Linux; Android " + ANDROID_VER + ") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36";
    public static final List<String> REQUIRED_SCOPES = ImmutableList.of( "https://www.googleapis.com/auth/photoslibrary.readonly",
                                                                            "https://www.googleapis.com/auth/photoslibrary.appendonly",
                                                                            "https://www.googleapis.com/auth/photoslibrary.readonly.appcreateddata",
                                                                            "https://www.googleapis.com/auth/photoslibrary.edit.appcreateddata",
                                                                            "https://www.googleapis.com/auth/photoslibrary",
                                                                            "https://www.googleapis.com/auth/photoslibrary.sharing");


    public static Credential getCredential(Context context) throws IOException, GeneralSecurityException {
            try {
                GoogleAuthorizationCodeFlow flow = getGoogleAuthorizationCodeFlow(context);
                Credential credential = flow.loadCredential(USER_ID);
                if (isValidCredential(credential)) {
                    return credential;
                }
            } catch (Throwable th) {
                Log.e(TAG, "Fail to get credentials", th);
            }
            return null;
        }

    public static GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow(Context context) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Load client secrets.
        InputStream in = context.getResources().openRawResource(R.raw.credentials_desktop);
        if (in == null) {
            throw new FileNotFoundException("Credential Resource not found.");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, REQUIRED_SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(context.getFilesDir().getAbsolutePath())))
                .setAccessType("offline")
                .build();
        return flow;
    }

    public static boolean isValidCredential(Credential credential) {
         return (credential != null
                && (credential.getRefreshToken() != null
                || credential.getExpiresInSeconds() == null
                || credential.getExpiresInSeconds() > 60));
    }


}