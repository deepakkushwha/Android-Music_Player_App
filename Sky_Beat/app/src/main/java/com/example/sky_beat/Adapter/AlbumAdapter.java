package com.example.sky_beat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sky_beat.Activity.Album_details;
import com.example.sky_beat.R;
import com.example.sky_beat.model.MusicFile;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.viewHolder> {

    public ArrayList<MusicFile> AlbumFilesAdapter;
    private Context context;

    public AlbumAdapter(ArrayList<MusicFile> albumFilesSongAdapter, Context context) {
        AlbumFilesAdapter = albumFilesSongAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_list_item, parent, false);
        return new AlbumAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MusicFile musicFileTemp = AlbumFilesAdapter.get(position);
        holder.textView.setText(musicFileTemp.getTitle());
        byte[] image = getAlbumArt(musicFileTemp.getPath());
        if (image != null) {
            Glide.with(context).asBitmap().load(image).into(holder.imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.listimage)
                    .into(holder.imageView);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Album_details.class);
                intent.putExtra("AlbumPosition",AlbumFilesAdapter.get(position).getAlbum());
                context.startActivity(intent);
            }
        });

    }

    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @Override
    public int getItemCount() {
        return AlbumFilesAdapter.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album_image);
            textView = itemView.findViewById(R.id.album_text);
            cardView = itemView.findViewById(R.id.album_items);
        }
    }

}
