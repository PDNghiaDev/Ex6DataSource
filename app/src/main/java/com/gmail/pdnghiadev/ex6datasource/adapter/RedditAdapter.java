package com.gmail.pdnghiadev.ex6datasource.adapter;

import android.support.v7.widget.RecyclerView;
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
 * Class Adapter
 */
public class RedditAdapter extends RecyclerView.Adapter<RedditAdapter.RedditViewHolder> {
    private List<Children> listChildrend;
    private int isSticky;
    private int isNotSticky;

    public RedditAdapter(List<Children> mChildren, int isSticky, int isNotSticky) {
        this.listChildrend = mChildren;
        this.isSticky = isSticky;
        this.isNotSticky = isNotSticky;
    }

    @Override
    public RedditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        return new RedditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RedditViewHolder holder, int position) {
        DateConverter dateConverter = new DateConverter();
        Children children = listChildrend.get(position);

        holder.mScore.setText(String.valueOf(children.getScore()));
        holder.mAuthor.setText(children.getAuthor());
        holder.mSubreddit.setText(children.getSubreddit());
        if (children.isStickyPost()) {
            holder.mTitle.setTextColor(isSticky);
        } else {
            holder.mTitle.setTextColor(isNotSticky);
        }
        holder.mTitle.setText(children.getTitle());
        String comment = String.valueOf(children.getCommentCount())
                + " Comments • reddit • "
                + dateConverter.displayTime(children.getCreateUTC());
        holder.mCountComment.setText(comment);
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
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view) {
            super(view);

            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }

}
