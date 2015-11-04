package com.gmail.pdnghiadev.ex6datasource.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.pdnghiadev.ex6datasource.R;
import com.gmail.pdnghiadev.ex6datasource.model.Children;
import com.gmail.pdnghiadev.ex6datasource.ultils.DateConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public class RedditAdapter extends RecyclerView.Adapter<RedditAdapter.RedditViewHolder>{
    private List<Children> mChildren;
    private int color;

    public RedditAdapter(List<Children> mChildren, int color) {
        this.mChildren = mChildren;
        this.color = color;
    }

    @Override
    public RedditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View redditView = inflater.inflate(R.layout.item_post, parent, false);

        // Return a new holder instance
        RedditViewHolder viewHolder = new RedditViewHolder(redditView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RedditViewHolder holder, int position) {
        DateConverter dateConverter = new DateConverter();
        Children  children = mChildren.get(position);

        holder.mScore.setText(String.valueOf(children.getScore()));
        holder.mAuthor.setText(children.getAuthor());
        holder.mSubreddit.setText(children.getSubreddit());
        if (children.isStickyPost()){
            holder.mTitle.setTextColor(color);
            holder.mTitle.setText(children.getTitle());
        }else {
            holder.mTitle.setText(children.getTitle());
        }

        holder.mCountComment.setText(String.valueOf(children.getCommentCount()));
        holder.mCreateUTC.setText(dateConverter.displayTime(children.getCreateUTC()));

    }

    @Override
    public int getItemCount() {
        return mChildren.size();
    }

    public static class RedditViewHolder extends RecyclerView.ViewHolder{
        public TextView mScore, mAuthor, mSubreddit, mTitle, mCountComment, mCreateUTC;

        public RedditViewHolder(View itemView) {
            super(itemView);

            mScore = (TextView) itemView.findViewById(R.id.txt_score);
            mAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            mSubreddit = (TextView) itemView.findViewById(R.id.txt_subreddit);
            mTitle = (TextView) itemView.findViewById(R.id.txt_title);
            mCountComment = (TextView) itemView.findViewById(R.id.txt_count_comment);
            mCreateUTC = (TextView) itemView.findViewById(R.id.txt_createdUTC);
        }
    }
}
