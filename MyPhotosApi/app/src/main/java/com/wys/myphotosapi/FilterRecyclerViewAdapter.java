package com.wys.myphotosapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.FilterViewHolder> {
    Context context;
    ArrayList<PhotosItem> items = new ArrayList<PhotosItem>();

    public FilterRecyclerViewAdapter(Context context) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public int getItemCount() {  // item의 갯수?
        return items.size();
    }
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {  // viewholder가 만들어지는 시점에 호출. 재사용된다면 생성되지 않아서 service를 하나 생성해줘야함
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_filter, parent, false); // 최상위 layout

        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {   //binding data와 dataholoder나 xml layout과 결합되는 경우를 의미. on이 붙으면 자동으로 결합된다.
        PhotosItem item = items.get(position); //몇번째 뷰인지를 보여주는것
        holder.setItem(item);
    }

    public void addItem(PhotosItem item) {
        items.add(item);
    }

    public void addItems(ArrayList<PhotosItem> items) {
        this.items = items;
    }

    public PhotosItem getItem(int position) {
        return items.get(position);
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.search_editText);

        }
            public void setItem(PhotosItem item) {//내가 만든 이미지 리소스를 viewholder에 선언
            textView.setText(item.getName());

        }

    }




}
