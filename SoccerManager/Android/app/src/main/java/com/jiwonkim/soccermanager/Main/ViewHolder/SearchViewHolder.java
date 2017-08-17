package com.jiwonkim.soccermanager.Main.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiwonkim.soccermanager.R;

/**
 * Created by user on 2017-06-12.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {
    // 뷰들 public으로 선언
    public ImageView searchImage;
    public TextView searchId, searchName, searchTeam;

    public SearchViewHolder(View itemView){
        super(itemView);
        // 선언한 뷰 초기화
        searchImage = (ImageView)itemView.findViewById(R.id.recyclerSearchItemImage);
        searchId = (TextView)itemView.findViewById(R.id.recyclerSearchItemId);
        searchName = (TextView)itemView.findViewById(R.id.recyclerSearchItemName);
        searchTeam = (TextView)itemView.findViewById(R.id.recyclerSearchItemTeam);
    }
}
