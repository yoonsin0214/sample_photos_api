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

public class AlbumContentsRecyclerViewAdapter extends RecyclerView.Adapter<AlbumContentsRecyclerViewAdapter.AlbumContentsViewHolder>{
    Context context;
    ArrayList<MediaItem> mediaItems;

    public AlbumContentsRecyclerViewAdapter(Context context, ArrayList<MediaItem> mediaItems) {
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @NonNull
    @Override
    public AlbumContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_photo, parent, false);

        return new AlbumContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumContentsViewHolder holder, int position) {
        MediaItem mediaItem = mediaItems.get(position);

        Glide.with(context)
                .load(mediaItem.getBaseUrl())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    static class AlbumContentsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public AlbumContentsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.photo_imageView);
        }
    }
}
