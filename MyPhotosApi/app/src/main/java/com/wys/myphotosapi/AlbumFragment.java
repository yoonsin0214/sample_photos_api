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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.photos.types.proto.Album;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;


public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AlbumRecyclerViewAdapter adapter;

    Credential credential;
    PhotosLibrarySettings settings;
    private PhotosLibraryClient photosLibraryClient;
    ArrayList<Album> albums;
    public static String albumId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup album_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_photo_items, container, false);

        recyclerView = (RecyclerView) album_recyclerview.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨

        getUserCredentials();
        checkCredential();

        getGooglePhotoAlbums();

        adapter = new AlbumRecyclerViewAdapter(getActivity(), albums);
        adapter.setOnItemClickListener(new AlbumRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                albumId = albums.get(position).getId();
                Log.d("chaohreum.park", "position: "+position + ", albumsId: " + albumId);

                ((MainActivity)getActivity()).replaceFragment(AlbumContentsFragment.newInstance());
            }
        });
        recyclerView.setAdapter(adapter);

        return album_recyclerview;
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


    private void getGooglePhotoAlbums() {
        PhotosLibraryClient client = createPhotoLibraryClient();
        PhotosLibraryClient.ListAlbumsPagedResponse response = client.listAlbums();
        albums = new ArrayList<Album>();

        for (Album album : response.iterateAll()) {
            albums.add(album);
            // Get some properties of an album
            String id = album.getId();
            String title = album.getTitle();
            String productUrl = album.getProductUrl();
            String coverPhotoBaseUrl = album.getCoverPhotoBaseUrl();
            // The cover photo media item id field may be empty
            String coverPhotoMediaItemId = album.getCoverPhotoMediaItemId();
            boolean isWritable = album.getIsWriteable();
            long mediaItemsCount = album.getMediaItemsCount();

            Log.d("AlbumFragment", "title: " + title);
            Log.d("AlbumFragment", "mediaItemsCount: " + mediaItemsCount);
        }


    }
}
