package com.studentproject.popluarmoviesstage2.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.studentproject.popluarmoviesstage2.R;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public TextView mVideoName;
    public TextView mVideoType;

    public VideoViewHolder(View itemView) {
        super(itemView);
        mVideoName = itemView.findViewById(R.id.tv_video_name);
        mVideoType = itemView.findViewById(R.id.tv_video_type);
    }

}
