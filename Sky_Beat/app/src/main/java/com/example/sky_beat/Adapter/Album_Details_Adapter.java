package com.example.sky_beat.Adapter;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sky_beat.Activity.PlayerActivity;
import com.example.sky_beat.R;
import com.example.sky_beat.model.MusicFile;

import java.io.File;
import java.util.ArrayList;

public class Album_Details_Adapter extends RecyclerView.Adapter<Album_Details_Adapter.viewHolder> {
    static Context context;
    public static ArrayList<MusicFile> album_Music;

    public Album_Details_Adapter(Context context, ArrayList<MusicFile> album_Music) {
        this.context = context;
        this.album_Music = album_Music;
    }

    @NonNull
    @Override
    public Album_Details_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_list_item, parent, false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Album_Details_Adapter.viewHolder holder, int position) {
        MusicFile musicFileTemp = album_Music.get(position);
        holder.textView.setText(musicFileTemp.getTitle());
        byte[] image = getAlbumArt(musicFileTemp.getPath());
        if (image != null) {
            Glide.with(context).asBitmap().load(image).into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.listimage)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("Sender", "albumDetails");
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);


                builder.setMessage("Do you really want to delete ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                        Long.parseLong(album_Music.get(position).getId()));

                                File file = new File(album_Music.get(position).getPath());
                                boolean deleted = file.delete();

                                if (deleted) {
                                    context.getContentResolver().delete(uri, null, null);
                                    album_Music.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, album_Music.size());
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null);

                final AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);

                    }
                });


                alertDialog.setTitle("Deleting the song");
                alertDialog.show();


                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return album_Music.size();
    }
    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.music_img);
            textView = itemView.findViewById(R.id.music_filename);
                 }
    }
}
