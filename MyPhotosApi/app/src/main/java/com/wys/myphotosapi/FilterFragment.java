package com.wys.myphotosapi;

import static com.wys.myphotosapi.GoogleOAuthFactory.JSON_FACTORY;
import static com.wys.myphotosapi.GoogleOAuthFactory.LOCAL_RECEIVER_PORT;
import static com.wys.myphotosapi.GoogleOAuthFactory.REQUIRED_SCOPES;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.ContentCategory;
import com.google.photos.library.v1.proto.ContentFilter;
import com.google.photos.library.v1.proto.Filters;
import com.google.photos.types.proto.MediaItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;


public class FilterFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    FilterRecyclerViewAdapter adapter;
    Credential credential;
    private PhotosLibraryClient photosLibraryClient;
    PhotosLibrarySettings settings;
    ArrayList<MediaItem> imageUrlList;
    CheckBox checkbox1, checkbox2, checkbox3;
    Filters filters;
    ContentFilter contentFilter;
    MediaItem item;

    int uncheck = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup filter_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_filter, container, false);

        recyclerView = (RecyclerView) filter_recyclerview.findViewById(R.id.filter_recyclerView);
        checkbox1 = (CheckBox) filter_recyclerview.findViewById(R.id.selfies);
        checkbox2 = (CheckBox) filter_recyclerview.findViewById(R.id.food);
        checkbox3 = (CheckBox) filter_recyclerview.findViewById(R.id.landscape);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨

        imageUrlList = new ArrayList<MediaItem>();

        try {
            credential = authorize();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + credential.getExpirationTimeMilliseconds());

        Credentials credentials = GoogleCredentials.create(new AccessToken(credential.getAccessToken(), expireTime));

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



        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox1.isChecked()) {
                    setFilterPhotos(R.id.selfies, true);
                } else {
                    setFilterPhotos(uncheck, false );
                }
            }
        });

        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox2.isChecked()) {
                    setFilterPhotos(R.id.food,true);
                } else {
                    setFilterPhotos(uncheck, false );
                }
            }
        });

        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox3.isChecked()){
                    setFilterPhotos(R.id.landscape,true);
                }else {
                    setFilterPhotos(uncheck, false );
                }
            }
        });

            return filter_recyclerview;
   }

    public void setFilterPhotos(int resId, boolean checked) {
        switch (resId) {
            case R.id.selfies:
                contentFilter = ContentFilter.newBuilder()
                        .addIncludedContentCategories(ContentCategory.SELFIES)
                        .build();
                break;
            case R.id.food:
                contentFilter = ContentFilter.newBuilder()
                        .addIncludedContentCategories(ContentCategory.FOOD)
                        .build();

                break;
            case R.id.landscape:
                contentFilter = ContentFilter.newBuilder()
                        .addIncludedContentCategories(ContentCategory.LANDSCAPES)
                        .build();
                break;

            default:
                contentFilter = ContentFilter.newBuilder().clear().build();
                break;
        }

        if (checked == true) {
            // Create a new Filters object
            filters = Filters.newBuilder()
                    .setContentFilter(contentFilter)
                    .build();

            InternalPhotosLibraryClient.SearchMediaItemsPagedResponse response = photosLibraryClient.searchMediaItems(filters);
            for (MediaItem item : response.iterateAll()) {
                imageUrlList.add(item);
            }
            adapter = new FilterRecyclerViewAdapter(imageUrlList, getActivity());
            recyclerView.setAdapter(adapter);
        } else {
            Filters filters = Filters.newBuilder()
                    .clearContentFilter()
                    .build();
            imageUrlList.clear();
            adapter.notifyDataSetChanged();
        }
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
        //return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user").setAccessToken(credential.getAccessToken());
    }

}
