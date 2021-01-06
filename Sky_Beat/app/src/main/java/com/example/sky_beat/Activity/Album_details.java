package com.example.sky_beat.Activity;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sky_beat.Adapter.Album_Details_Adapter;
import com.example.sky_beat.R;
import com.example.sky_beat.model.MusicFile;

import java.util.ArrayList;

import static com.example.sky_beat.Activity.MainActivity.musicFile;


public class Album_details extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    ArrayList<MusicFile> albumSong = new ArrayList<>();
    String AlbumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.recyclerView_details);
        imageView = findViewById(R.id.image);
        AlbumName = getIntent().getStringExtra("AlbumPosition");
        int j = 0;
        for (int i = 0; i < musicFile.size(); i++) {
            if (AlbumName.equals(musicFile.get(i).getAlbum())) {
                albumSong.add(j, musicFile.get(i));
                j++;
            }
        }
        byte[] image = getAlbumArt(albumSong.get(0).getPath());
        if (image == null) {
            Glide.with(this)
                    .load(R.drawable.heart)
                    .into(imageView);
        } else {
            Glide.with(this)
                    .load(image)
                    .into(imageView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSong.size() < 1)) {
            Album_Details_Adapter album = new Album_Details_Adapter (this, albumSong);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setAdapter(album);
            recyclerView.setLayoutManager(layoutManager);
        }
    }


    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }


}