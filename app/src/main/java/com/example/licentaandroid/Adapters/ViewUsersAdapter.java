package com.example.licentaandroid.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.licentaandroid.JavaClasses.Teachers;
import com.example.licentaandroid.JavaClasses.Users;
import com.example.licentaandroid.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUsersAdapter extends FirebaseRecyclerAdapter<Users, ViewUsersAdapter.myviewholder> {
    private Context mContext;

    public ViewUsersAdapter(@NonNull FirebaseRecyclerOptions<Users> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewUsersAdapter.myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull Users users) {

        holder.text.setText(users.getFullName());

            Glide.with(mContext).load(users.getImageUrl()).into(holder.circleImageView);



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.text.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Do you want to delete this account ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(position).getKey()).removeValue();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public ViewUsersAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacherrow, parent, false);
        return new ViewUsersAdapter.myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView delete;
        TextView text;
        CircleImageView circleImageView;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.nametext);
            circleImageView = itemView.findViewById(R.id.img);
            delete = itemView.findViewById(R.id.deleteicon);
        }
    }
}
