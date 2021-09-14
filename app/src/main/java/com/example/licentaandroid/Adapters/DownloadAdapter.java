package com.example.licentaandroid.Adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licentaandroid.Activities.PdfDownloadActivity;
import com.example.licentaandroid.JavaClasses.Uploads;
import com.example.licentaandroid.R;

import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {
    PdfDownloadActivity pdfDownloadActivity;
    ArrayList<Uploads> uploads;

    public DownloadAdapter(PdfDownloadActivity pdfDownloadActivity, ArrayList<Uploads> uploads) {
        this.pdfDownloadActivity = pdfDownloadActivity;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(pdfDownloadActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.pdffile_layout, null, false);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.name.setText(uploads.get(position).getFname());
        holder.uri.setText(uploads.get(position).getUrl());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.name.getContext(), uploads.get(position).getFname(), ".pdf", DIRECTORY_DOWNLOADS, uploads.get(position).getUrl());
            }
        });
    }

    public void downloadFile(Context context, String filname, String fileExtensio, String destinationDirectoty, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectoty, filname + fileExtensio);

        downloadManager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public static class DownloadViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, uri;
        Button button;

        public DownloadViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            uri = itemView.findViewById(R.id.uri);
            button = itemView.findViewById(R.id.down);
        }
    }

}
