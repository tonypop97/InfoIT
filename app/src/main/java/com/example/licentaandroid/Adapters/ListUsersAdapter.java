package com.example.licentaandroid.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentaandroid.Activities.PdfDownloadActivity;
import com.example.licentaandroid.Activities.PrivateCommentActivity;
import com.example.licentaandroid.JavaClasses.Users;
import com.example.licentaandroid.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.ViewHolder> {
    private Context mContext;
    private List<Users> mUsers;

    public ListUsersAdapter(Context mContext, List<Users> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public ListUsersAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.teacher_item, parent, false);

        return new ListUsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListUsersAdapter.ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getFullName());

        Glide.with(mContext).load(users.getImageUrl()).into(holder.profile_image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PdfDownloadActivity.class);
                intent.putExtra("id", users.getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
