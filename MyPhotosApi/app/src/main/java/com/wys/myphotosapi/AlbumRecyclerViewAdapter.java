package com.wys.myphotosapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.photos.types.proto.Album;

import java.util.ArrayList;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumViewHolder> {
    Context context;
    private static ArrayList<Album> albums;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private static OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public AlbumRecyclerViewAdapter(Context context, ArrayList<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public int getItemCount() {  // item의 갯수?
        return albums.size();
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewholder가 만들어지는 시점에 호출. 재사용된다면 생성되지 않아서 service를 하나 생성해줘야함
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_album, parent, false); // 최상위 layout

        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {   //binding data와 dataholoder나 xml layout과 결합되는 경우를 의미. on이 붙으면 자동으로 결합된다.
        Album album = albums.get(position); //몇번째 뷰인지를 보여주는것

        Glide.with(context)
                .load(album.getCoverPhotoBaseUrl())
                .into(holder.imageView);

        holder.textView.setText(album.getTitle());

    }

    public void addItem(Album album) {
        albums.add(album);
    }

    public void addItems(ArrayList<Album> albums) {
        this.albums = albums;
    }

    public Album getItem(int position) {
        return albums.get(position);
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.album_imageView);
            textView = (TextView) itemView.findViewById(R.id.album_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }

                    }
                }
            });

        }

    }
}
