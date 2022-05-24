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


public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    FavoriteRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup favorite_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_photo_items,container,false);

        recyclerView = (RecyclerView) favorite_recyclerview.findViewById(R.id.recyclerview);
        gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


// datapter 사용해서 photo api 써서 여기로 다운
        adapter = new FavoriteRecyclerViewAdapter(getActivity());
        adapter.addItem(new PhotosItem("MyFriend", R.drawable.shark2));
        adapter.addItem(new PhotosItem("MyFriend", R.drawable.shark2));
        adapter.addItem(new PhotosItem("MyFriend", R.drawable.shark2));
        adapter.addItem(new PhotosItem("MyFriend", R.drawable.shark));
        recyclerView.setAdapter(adapter);



        return favorite_recyclerview;
    }



}
