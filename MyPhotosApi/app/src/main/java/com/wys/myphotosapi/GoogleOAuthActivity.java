package com.wys.myphotosapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleOAuthActivity extends Activity {
    private static final String TAG = "GoogleOAuthActivity";

    private Credential credential;
    private static final int MSG_UPDATE_AUTH_STATUS = 0;
    private static final int MSG_OPEN_AUTH_WEB_VIEW = 1;

    private static final boolean FEATURE_USE_INTERNAL_WEB_VIEW = true;

    private enum AuthStatus {
        STATUS_NOT_AUTH,
        STATUS_AUTH,
    }

    private Credential Credential;
    private Button ButtonAuth;
    private WebView WebView;

    public static final String ACTION_SUCCESS_AUTH_GOOGLE_API = "com.wys.photosapi.action.SUCCESS_AUTH_GOOGLE_API";

    private Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_UPDATE_AUTH_STATUS) {
                onAuthStatusChanged((AuthStatus) msg.obj);
            } else if (msg.what == MSG_OPEN_AUTH_WEB_VIEW) {
                String url = (String) msg.obj;
                WebView.loadUrl(url);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_google_oauth);

        WebView = findViewById(R.id.webview);
        setWebView();

        ButtonAuth = findViewById(R.id.btn_auth);
        ButtonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOAuth();
            }
        });

        try {
            credential = GoogleOAuthFactory.getUserCredentials(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sendAuthUpdateMessage(credential == null ? AuthStatus.STATUS_NOT_AUTH : AuthStatus.STATUS_AUTH);
    }

    private void setWebView() {
        WebView webView = WebView;
        if (webView == null) {
            return;
        }
        if (FEATURE_USE_INTERNAL_WEB_VIEW) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setUserAgentString(GoogleOAuthFactory.CUSTOM_USER_AGENT);
            webSettings.setJavaScriptEnabled(true);
            // Enable Cookies
            CookieManager.getInstance().setAcceptCookie(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
        } else {
            webView.setVisibility(View.GONE);
        }
    }

    private void onAuthStatusChanged(AuthStatus status) {
        if (status == AuthStatus.STATUS_AUTH) {
            ButtonAuth.setEnabled(false);
            setResult(Activity.RESULT_OK);
            Toast.makeText(this, "Authorized.", Toast.LENGTH_SHORT).show();
            sendBroadcast(new Intent(ACTION_SUCCESS_AUTH_GOOGLE_API));
            finish();
        } else {
            ButtonAuth.setEnabled(true);
        }
    }

    private void sendAuthUpdateMessage(AuthStatus status) {
        Handler handler = mUIHandler;
        if (handler != null) {
            Message msg = new Message();
            msg.what = MSG_UPDATE_AUTH_STATUS;
            msg.obj = status;
            handler.sendMessage(msg);
        }
    }

    private void runOAuth() {
        new Thread() {
           @Override
           public void run () {
               try {
                   LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(GoogleOAuthFactory.PORT_REDIRECTION_LOCAL_SERVER).build();
                   AuthorizationCodeInstalledApp acia = new AuthorizationCodeInstalledApp(GoogleOAuthFactory.getGoogleAuthorizationCodeFlow(GoogleOAuthActivity.this), receiver) {
                       @Override
                       protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
                           String url = authorizationUrl.build();
                           Log.d(TAG, "authorization url : " + url);
                           if (false) {
                               Message msg = new Message();
                               msg.what = MSG_OPEN_AUTH_WEB_VIEW;
                               msg.obj = url;
                               mUIHandler.sendMessage(msg);
                           } else {
                               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                               startActivity(browserIntent);
                           }
                       }
                   };
                   credential = acia.authorize(GoogleOAuthFactory.USER_ID);
                   if (GoogleOAuthFactory.isValidCredential(credential)) {
                       sendAuthUpdateMessage(AuthStatus.STATUS_AUTH);
                   } else {
                       sendAuthUpdateMessage(AuthStatus.STATUS_NOT_AUTH);
                   }
               } catch (Throwable th) {
                   Log.e(TAG, "Fail to authorization.", th);
                   sendAuthUpdateMessage(AuthStatus.STATUS_NOT_AUTH);
               }
           }
        }.start();
    }
}
