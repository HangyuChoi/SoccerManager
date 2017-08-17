package com.jiwonkim.soccermanager.Main.TeamPage.Formation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiwonkim.soccermanager.Main.ViewHolder.FormationItemViewHolder;
import com.jiwonkim.soccermanager.R;

import java.util.ArrayList;

/**
 * Created by user on 2017-06-14.
 */

public class FormationAdapter extends RecyclerView.Adapter<FormationItemViewHolder> {
    ArrayList<PlayerListData> itemDatas;
    View.OnTouchListener touchListener;

    public FormationAdapter(ArrayList<PlayerListData> itemDatas, View.OnTouchListener touchListener) {
        this.itemDatas = itemDatas;
        this.touchListener = touchListener;
    }

    @Override
    public FormationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_formation,parent,false);
        FormationItemViewHolder viewHolder = new FormationItemViewHolder(view);
        view.setOnTouchListener(touchListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FormationItemViewHolder holder, int position) {
        holder.playerImg.setImageResource(itemDatas.get(position).img);
        holder.playerName.setText(itemDatas.get(position).name);
    }

    @Override
    public int getItemCount() {
        return itemDatas != null ? itemDatas.size() : 0;
    }
}
