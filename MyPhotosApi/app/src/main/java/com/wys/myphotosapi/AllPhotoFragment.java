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


public class AllPhotoFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AllPhotoRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup photos_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_photo_items,container,false);

        recyclerView = (RecyclerView) photos_recyclerview.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(),6, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


// datapter 사용해서 photo api 써서 여기로 다운
        adapter = new AllPhotoRecyclerViewAdapter(getActivity());

        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));
        adapter.addItem(new PhotosItem("name1",R.drawable.ic_launcher_foreground));

        recyclerView.setAdapter(adapter);

        return photos_recyclerview;
    }

}
