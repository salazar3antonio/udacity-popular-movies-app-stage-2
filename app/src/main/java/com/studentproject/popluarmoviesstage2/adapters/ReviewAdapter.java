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
import com.studentproject.popluarmoviesstage2.database.models.Review;
import com.studentproject.popluarmoviesstage2.view_holders.ReviewViewHolder;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {


    private List<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_detail_review_list_item, parent,false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        final Review review = mReviews.get(position);
        holder.mReviewAuthor.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent urlReviewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                mContext.startActivity(urlReviewIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
    }
}
