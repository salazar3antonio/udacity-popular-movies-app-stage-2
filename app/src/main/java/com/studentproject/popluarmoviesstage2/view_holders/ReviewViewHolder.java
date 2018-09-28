package com.studentproject.popluarmoviesstage2.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.studentproject.popluarmoviesstage2.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    public TextView mReviewAuthor;
    public TextView mReviewContent;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        mReviewAuthor = itemView.findViewById(R.id.tv_review_author);
        mReviewContent = itemView.findViewById(R.id.tv_review_content);
    }
}
