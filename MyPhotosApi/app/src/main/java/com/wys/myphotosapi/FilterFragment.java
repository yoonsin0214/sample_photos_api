package com.wys.myphotosapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


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

        recyclerView = (RecyclerView) filter_recyclerview.findViewById(R.id.search_recyclerView);
        editText = (EditText) filter_recyclerview.findViewById(R.id.search_editText) ;

        gridLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(gridLayoutManager); // grid로 화면 구성됨


// datapter 사용해서 photo api 써서 여기로 다운
        adapter = new FilterRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        return filter_recyclerview;
    }

}
