package com.wys.myphotosapi;

import static com.wys.myphotosapi.GoogleOAuthFactory.JSON_FACTORY;
import static com.wys.myphotosapi.GoogleOAuthFactory.LOCAL_RECEIVER_PORT;
import static com.wys.myphotosapi.GoogleOAuthFactory.REQUIRED_SCOPES;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;


public class AlbumContentsFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AlbumContentsRecyclerViewAdapter adapter;

    Credential credential;
    PhotosLibrarySettings settings;
    private PhotosLibraryClient photosLibraryClient;
    ArrayList<MediaItem> mediaItems;


    public static AlbumContentsFragment newInstance() {
        return new AlbumContentsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup album_contents_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_photo_items, container, false);

        recyclerView = (RecyclerView) album_contents_recyclerview.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


        getUserCredentials();
        checkCredential();

        getGooglePhotoAlbumContents();

        adapter = new AlbumContentsRecyclerViewAdapter(getActivity(), mediaItems);
        recyclerView.setAdapter(adapter);

        return album_contents_recyclerview;
    }

    private Credential authorize() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        InputStream in = getActivity().getResources().openRawResource(R.raw.credentials_desktop);

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, REQUIRED_SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(getActivity().getFilesDir().getAbsolutePath())))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(LOCAL_RECEIVER_PORT).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user").setRefreshToken(credential.getRefreshToken());
    }

    private Credentials getUserCredentials() {
        try {
            credential = authorize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Credentials credentials = GoogleCredentials.create(new AccessToken(credential.getAccessToken(), null));
        return credentials;
    }

    private PhotosLibraryClient createPhotoLibraryClient() {
        try {
            settings = PhotosLibrarySettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(getUserCredentials()))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            photosLibraryClient = PhotosLibraryClient.initialize(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photosLibraryClient;
    }

    private void checkCredential() {
        if (GoogleOAuthFactory.isValidCredential(credential)) {
            Toast.makeText(getActivity(), "Alive credentials", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "OMG!!!!!.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getGooglePhotoAlbumContents() {
        PhotosLibraryClient client = createPhotoLibraryClient();
        PhotosLibraryClient.SearchMediaItemsPagedResponse response = client.searchMediaItems(AlbumFragment.albumId);
        mediaItems = new ArrayList<MediaItem>();

        for (MediaItem mediaItem : response.iterateAll()) {
            mediaItems.add(mediaItem);

            Log.d("AlbumFragment", "mediaItems " + mediaItems);
        }

    }
}