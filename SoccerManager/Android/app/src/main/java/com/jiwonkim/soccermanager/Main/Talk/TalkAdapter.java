package com.jiwonkim.soccermanager.Main.Talk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiwonkim.soccermanager.Main.ViewHolder.TalkViewHolder;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-15.
 */

public class TalkAdapter extends RecyclerView.Adapter<TalkViewHolder>{
    ArrayList<TalkListData> itemDatas;

    public TalkAdapter(ArrayList<TalkListData> itemDatas) {
        this.itemDatas = itemDatas;
    }

    @Override
    public TalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_talk_read,parent,false);
        TalkViewHolder viewHolder = new TalkViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TalkViewHolder holder, int position) {
        holder.talkTitle.setText(itemDatas.get(position).title);
        holder.talkName.setText(itemDatas.get(position).name);
        holder.talkTime.setText(itemDatas.get(position).time);
        holder.talkDetail.setText(itemDatas.get(position).detail);
    }

    @Override
    public int getItemCount() {
        return itemDatas != null ? itemDatas.size() : 0;
    }
}
