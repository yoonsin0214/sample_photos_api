package com.wys.myphotosapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.types.proto.MediaItem;


public class AllPhotoFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AllPhotoRecyclerViewAdapter adapter;
    private PhotosLibraryClient photosLibraryClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup photos_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_photo_items,container,false);

        recyclerView = (RecyclerView) photos_recyclerview.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(),6, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


// datapter 사용해서 photo api 써서 여기로 다운
        adapter = new AllPhotoRecyclerViewAdapter(getActivity());
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
        }

        return photos_recyclerview;
    }
}
