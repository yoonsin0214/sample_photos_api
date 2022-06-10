package com.wys.myphotosapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.photos.types.proto.MediaItem;

import java.util.ArrayList;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.FilterViewHolder> {
    private ArrayList<MediaItem> imageUrls;
    private Context context;

    public FilterRecyclerViewAdapter(ArrayList<MediaItem> imageUrlList , Context context) {
        this.imageUrls = imageUrlList;
        this.context = context;
    }

    @NonNull
    @Override
    public int getItemCount() {  // item의 갯수?
        return imageUrls.size();
    }
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewholder가 만들어지는 시점에 호출. 재사용된다면 생성되지 않아서 service를 하나 생성해줘야함
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_photo, parent, false); // 최상위 layout

        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {   //binding data와 dataholoder나 xml layout과 결합되는 경우를 의미. on이 붙으면 자동으로 결합된다.
        ImageView imageview = holder.imageView;
        MediaItem item = imageUrls.get(position);

        Glide.with(context)
                .load(item.getBaseUrl())
                .into(imageview);
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.photo_imageView);

        }
    }
}