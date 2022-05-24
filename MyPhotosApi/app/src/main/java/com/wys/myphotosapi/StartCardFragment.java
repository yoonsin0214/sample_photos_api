package com.wys.myphotosapi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class StartCardFragment extends Fragment {
    MainActivity activity;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    PhotosRecyclerViewAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup first_view = (ViewGroup) inflater.inflate(R.layout.fragment_main,container,false);

        LinearLayout allphoto = (LinearLayout) first_view.findViewById(R.id.allphotos_box);
        allphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(2);
            }
        });

        LinearLayout albumlist = (LinearLayout) first_view.findViewById(R.id.album_list_box);
        albumlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(3);
            }
        });

        LinearLayout hashtag = (LinearLayout) first_view.findViewById(R.id.hashtag_box);
        hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(4);
            }
        });

        LinearLayout favorite = (LinearLayout) first_view.findViewById(R.id.favorite_box);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(5);
            }
        });

        return first_view;
    }
}
