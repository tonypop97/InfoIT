package com.example.licentaandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentaandroid.JavaClasses.Comment;
import com.example.licentaandroid.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context mContext;
    private final List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {

        Glide.with(mContext).load(mData.get(position).getImageUrl()).into(holder.tv_img);
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_name.setText(mData.get(position).getUsername());
        holder.tv_date.setText(timestampToString((Long) mData.get(position).getTimestamp()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView tv_img;
        TextView tv_name, tv_content, tv_date;

        public CommentViewHolder(View itemView) {
            super(itemView);

            tv_img = itemView.findViewById(R.id.profileImage);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_text);
            tv_date = itemView.findViewById(R.id.comment_date);
        }
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        return currentDate;
    }

}
