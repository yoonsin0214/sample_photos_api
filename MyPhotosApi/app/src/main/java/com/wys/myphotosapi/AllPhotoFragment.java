package com.wys.myphotosapi;

import static com.wys.myphotosapi.GoogleOAuthFactory.JSON_FACTORY;
import static com.wys.myphotosapi.GoogleOAuthFactory.LOCAL_RECEIVER_PORT;
import static com.wys.myphotosapi.GoogleOAuthFactory.REQUIRED_SCOPES;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Date;


public class AllPhotoFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AllPhotoRecyclerViewAdapter adapter;
    private PhotosLibraryClient photosLibraryClient;
    TextView testtextView;
    ImageView testimageView;
    Credential credential;
    PhotosLibrarySettings settings;

    /*
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup photos_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_photo_items, container, false);

            recyclerView = (RecyclerView) photos_recyclerview.findViewById(R.id.recyclerview);
            gridLayoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨

     //datapter 사용해서 photo api 써서 여기로 다운
            adapter = new AllPhotoRecyclerViewAdapter(getActivity());
            return photos_recyclerview;
        }
    */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup photos_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_test, container, false);

        testtextView = (TextView) photos_recyclerview.findViewById(R.id.test_textView);
        testimageView = (ImageView) photos_recyclerview.findViewById(R.id.test_imageView);

        try {
            credential = authorize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime()+credential.getExpirationTimeMilliseconds());

        Credentials credentials = GoogleCredentials.create(new AccessToken(credential.getAccessToken(),expireTime));

        try {
            settings = PhotosLibrarySettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            photosLibraryClient = PhotosLibraryClient.initialize(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (GoogleOAuthFactory.isValidCredential(credential)) {
            Toast.makeText(getActivity(), "Alive credentials", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "OMG!!!!!.", Toast.LENGTH_SHORT).show();
        }
/*  확인완료 Album title url 가져오기
        PhotosLibraryClient.ListAlbumsPagedResponse response = photosLibraryClient.listAlbums();

        for (Album album : response.iterateAll()) {
            // Get some properties of an album
            String id = album.getId();
            String title = album.getTitle();
            String productUrl = album.getProductUrl();
            String coverPhotoBaseUrl = album.getCoverPhotoBaseUrl();
            // The cover photo media item id field may be empty
            String coverPhotoMediaItemId = album.getCoverPhotoMediaItemId();
            boolean isWritable = album.getIsWriteable();
            long mediaItemsCount = album.getMediaItemsCount();

            testtextView.append(productUrl + "\n");

            Glide.with(getContext()).load(productUrl).into(testimageView);

        }
*/
        try {
            // Make a request to list all media items in the user's library
            // Iterate over all the retrieved media items
            // Pagination is handled automatically
            InternalPhotosLibraryClient.ListMediaItemsPagedResponse response = photosLibraryClient.listMediaItems();
            for (MediaItem item : response.iterateAll()) {
                // Get some properties of a media item
                String id = item.getId();
                String description = item.getDescription();
                String mimeType = item.getMimeType();
                String productUrl = item.getProductUrl();
                String filename = item.getFilename();
                String PhotoBaseUrl = item.getBaseUrl();

                testtextView.append(PhotoBaseUrl + "\n");
                Glide.with(this).load(PhotoBaseUrl).into(testimageView);
            }
        } catch (ApiException e) {
            // Handle error
        }
        return photos_recyclerview;
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
}
