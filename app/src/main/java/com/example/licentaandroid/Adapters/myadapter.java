package com.example.licentaandroid.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licentaandroid.JavaClasses.FetchData;
import com.example.licentaandroid.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class myadapter extends FirebaseRecyclerAdapter<FetchData, myadapter.myviewholder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<FetchData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull FetchData model) {
        holder.text.setText(model.getTitle());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.text.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true, 1100)
                        .create();
                View myview = dialogPlus.getHolderView();
                final EditText title = myview.findViewById(R.id.t);
                final EditText content = myview.findViewById(R.id.c);
                final EditText homework = myview.findViewById(R.id.h);
                Button button = myview.findViewById(R.id.submit);

                title.setText(model.getTitle());
                content.setText(model.getText());
                homework.setText(model.getHomework());

                dialogPlus.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString());
                        map.put("text", content.getText().toString());
                        map.put("homework", homework.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Courses")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.text.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Do you want to delete this course ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Courses")
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
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView edit, delete;
        TextView text;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.nametext);
            edit = itemView.findViewById(R.id.editicon);
            delete = itemView.findViewById(R.id.deleteicon);
        }
    }
}
