package com.studentproject.popluarmoviesstage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentproject.popluarmoviesstage2.R;
import com.studentproject.popluarmoviesstage2.database.models.Video;
import com.studentproject.popluarmoviesstage2.utils.NetworkUtils;
import com.studentproject.popluarmoviesstage2.view_holders.VideoViewHolder;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {


    private List<Video> mVideos;
    private Context mContext;

    public VideoAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_detail_video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int position) {

        final Video video = mVideos.get(position);

        videoViewHolder.mVideoType.setText(video.getType());
        videoViewHolder.mVideoName.setText(video.getName());

        videoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to load a youtube url
                Intent videoWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_BASE_URL + video.getKey()));
                mContext.startActivity(videoWebIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void setVideos(List<Video> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }

}