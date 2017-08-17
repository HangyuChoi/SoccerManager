package com.jiwonkim.soccermanager.Main.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jiwonkim.soccermanager.R;

/**
 * Created by user on 2017-06-15.
 */

public class TalkViewHolder extends RecyclerView.ViewHolder {
    public TextView talkTitle, talkName, talkTime, talkDetail;

    public TalkViewHolder(View itemView) {
        super(itemView);
        talkTitle = (TextView)itemView.findViewById(R.id.talk_read_title);
        talkName = (TextView)itemView.findViewById(R.id.talk_read_name);
        talkTime = (TextView)itemView.findViewById(R.id.talk_read_time);
        talkDetail = (TextView)itemView.findViewById(R.id.talk_read_detail);
    }
}
