package com.gmail.pdnghiadev.ex6datasource.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.pdnghiadev.ex6datasource.R;
import com.gmail.pdnghiadev.ex6datasource.model.Children;
import com.gmail.pdnghiadev.ex6datasource.ultils.DateConverter;

import java.util.List;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public class RedditAdapter extends RecyclerView.Adapter {
    private List<Children> listChildrend;
    private int isSticky;
    private int isNotSticky;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public RedditAdapter(List<Children> mChildren, int isSticky, int isNotSticky) {
        this.listChildrend = mChildren;
        this.isSticky = isSticky;
        this.isNotSticky = isNotSticky;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            // Inflate the custom layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            vh = new RedditViewHolder(v);
        } else {
            // Inflate the custom layout
            Log.i("TAG", "PROG VIEW");
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DateConverter dateConverter = new DateConverter();
        Children children = listChildrend.get(position);

        if (holder instanceof RedditViewHolder) {

            ((RedditViewHolder) holder).mScore.setText(String.valueOf(children.getScore()));
            ((RedditViewHolder) holder).mAuthor.setText(children.getAuthor());
            ((RedditViewHolder) holder).mSubreddit.setText(children.getSubreddit());
            if (children.isStickyPost()) {
                ((RedditViewHolder) holder).mTitle.setTextColor(isSticky);
            } else {
                ((RedditViewHolder) holder).mTitle.setTextColor(isNotSticky);
            }
            ((RedditViewHolder) holder).mTitle.setText(children.getTitle());
            ((RedditViewHolder) holder).mCountComment.setText(String.valueOf(children.getCommentCount()));
            ((RedditViewHolder) holder).mCreateUTC.setText(dateConverter.displayTime(children.getCreateUTC()));

        } else {
            Log.i("TAG", "ROG");
                    ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public void clearAdapter() {
        listChildrend.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (listChildrend != null ? listChildrend.size() : 0);
    }

    public static class RedditViewHolder extends RecyclerView.ViewHolder {
        public TextView mScore, mAuthor, mSubreddit, mTitle, mCountComment, mCreateUTC;

        public RedditViewHolder(View itemView) {
            super(itemView);

            this.mScore = (TextView) itemView.findViewById(R.id.txt_score);
            this.mAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            this.mSubreddit = (TextView) itemView.findViewById(R.id.txt_subreddit);
            this.mTitle = (TextView) itemView.findViewById(R.id.txt_title);
            this.mCountComment = (TextView) itemView.findViewById(R.id.txt_count_comment);
            this.mCreateUTC = (TextView) itemView.findViewById(R.id.txt_createdUTC);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view) {
            super(view);

            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listChildrend.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
}
