package com.example.licentaandroid.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licentaandroid.JavaClasses.FetchData;
import com.example.licentaandroid.JavaClasses.Uploads;
import com.example.licentaandroid.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter {
    private final List<Uploads> listuploads;
    private final UsersAdapter.RecyclerViewClickListener listener;

    public UsersAdapter(List<Uploads> listuploads, UsersAdapter.RecyclerViewClickListener listener) {
        this.listuploads = listuploads;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        UsersAdapter.ViewHolderClass viewHolderClass = new UsersAdapter.ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UsersAdapter.ViewHolderClass viewHolderClass = (UsersAdapter.ViewHolderClass) holder;

        Uploads uploads = listuploads.get(position);
        viewHolderClass.title.setText(uploads.getEmail());

    }

    @Override
    public int getItemCount() {
        return listuploads.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;

        public ViewHolderClass(@NonNull View view) {
            super(view);

            title = view.findViewById(R.id.title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
