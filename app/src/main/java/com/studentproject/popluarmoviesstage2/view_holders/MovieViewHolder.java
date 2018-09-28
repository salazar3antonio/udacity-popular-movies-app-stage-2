package com.studentproject.popluarmoviesstage2.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.studentproject.popluarmoviesstage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    public final ImageView mMoviePosterImage;

    public MovieViewHolder(View itemView) {
        super(itemView);
        mMoviePosterImage = itemView.findViewById(R.id.iv_movie_poster);
    }


}
