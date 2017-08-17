package com.jiwonkim.soccermanager.Main.Search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiwonkim.soccermanager.Main.ViewHolder.SearchViewHolder;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-12.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{
    ArrayList<SearchListData> itemDatas;
    View.OnClickListener clickListener;

    public SearchAdapter(ArrayList<SearchListData> itemDatas, View.OnClickListener clickListener){
        this.itemDatas = itemDatas;
        this.clickListener = clickListener;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_search,parent,false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);
        view.setOnClickListener(clickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.searchImage.setImageResource(itemDatas.get(position).img);
        holder.searchId.setText(itemDatas.get(position).id);
        holder.searchName.setText(itemDatas.get(position).name);
        holder.searchTeam.setText(itemDatas.get(position).team);
    }

    @Override
    public int getItemCount() {
        return itemDatas != null ? itemDatas.size() : 0;
    }
}
