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

public class AllPhotoRecyclerViewAdapter extends RecyclerView.Adapter<AllPhotoRecyclerViewAdapter.PhotoViewHolder> {

    private ArrayList<MediaItem> imageUrls;
    private Context context;

    public AllPhotoRecyclerViewAdapter(ArrayList<MediaItem> imageUrlList , Context context) {
        this.imageUrls = imageUrlList;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_photo, parent, false); // 최상위 layout

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

        ImageView imageview = holder.imageView;
        MediaItem item = imageUrls.get(position);

        Glide.with(context)
                .load(item.getBaseUrl())
                .into(imageview);

    }
    public int getItemCount() {
        return imageUrls.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.photo_imageView);

        }
    }
}
