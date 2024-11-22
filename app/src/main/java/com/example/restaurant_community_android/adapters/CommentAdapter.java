package com.example.restaurant_community_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_community_android.R;
import com.example.restaurant_community_android.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments = new ArrayList<>();

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.contentTextView.setText(comment.getContent());
        holder.dateTextView.setText(comment.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView dateTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.commentContentTextView);
            dateTextView = itemView.findViewById(R.id.commentDateTextView);
        }
    }
} 