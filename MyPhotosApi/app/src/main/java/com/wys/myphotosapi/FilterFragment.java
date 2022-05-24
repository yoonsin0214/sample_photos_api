package com.wys.myphotosapi;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.text.TextWatcher;


public class FilterFragment extends Fragment {
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    FilterRecyclerViewAdapter adapter;

    ArrayList<PhotosItem> photosItemArrayList, filterList;
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup filter_recyclerview = (ViewGroup) inflater.inflate(R.layout.fragment_filter,container,false);

        recyclerView = (RecyclerView) filter_recyclerview.findViewById(R.id.recycler_hash);
        editText = (EditText) filter_recyclerview.findViewById(R.id.editSearch) ;

        gridLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


// datapter 사용해서 photo api 써서 여기로 다운
        adapter = new FilterRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        photosItemArrayList = new ArrayList<>();

        photosItemArrayList.add(new PhotosItem("바다1", R.drawable.sea));
        photosItemArrayList.add(new PhotosItem("거북이", R.drawable.turtle));
        photosItemArrayList.add(new PhotosItem("대장거북이", R.drawable.turtlle2));
        photosItemArrayList.add(new PhotosItem("바다2", R.drawable.sea2));
        photosItemArrayList.add(new PhotosItem("바다가고싶다", R.drawable.sea3));
        photosItemArrayList.add(new PhotosItem("아기상어가족", R.drawable.shark));
        photosItemArrayList.add(new PhotosItem("아기상어노랑", R.drawable.shark2));

        adapter.notifyDataSetChanged();

        return filter_recyclerview;
    }

}
