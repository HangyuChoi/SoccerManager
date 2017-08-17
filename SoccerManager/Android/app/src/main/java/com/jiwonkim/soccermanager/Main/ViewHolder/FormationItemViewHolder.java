package com.jiwonkim.soccermanager.Main.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiwonkim.soccermanager.R;

/**
 * Created by user on 2017-06-14.
 */

public class FormationItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView playerImg;
    public TextView playerName;

    public FormationItemViewHolder(View itemView) {
        super(itemView);
        playerImg = (ImageView)itemView.findViewById(R.id.recycler_playerImg);
        playerName = (TextView)itemView.findViewById(R.id.recycler_playerName);
    }
}
