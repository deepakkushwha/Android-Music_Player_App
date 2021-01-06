package com.example.sky_beat.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sky_beat.Adapter.AlbumAdapter;
import com.example.sky_beat.Adapter.SongAdapter;
import com.example.sky_beat.R;

import static com.example.sky_beat.Activity.MainActivity.musicFile;

public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    AlbumAdapter albumAdapter ;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.Album_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
       albumAdapter = new AlbumAdapter(musicFile, getContext());
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;

    }
}