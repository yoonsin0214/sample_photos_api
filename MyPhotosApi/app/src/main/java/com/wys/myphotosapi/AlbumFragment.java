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


public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AlbumRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup album_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_album,container,false);

        recyclerView = (RecyclerView) album_recyclerview.findViewById(R.id.album_recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


// datapter 사용해서 photo api 써서 여기로 다운
        adapter = new AlbumRecyclerViewAdapter(getActivity());
        adapter.addItem(new PhotosItem("MyFriend", R.drawable.album));
        recyclerView.setAdapter(adapter);



        return album_recyclerview;
    }



}
