package com.example.sky_beat.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sky_beat.Adapter.SongAdapter;
import com.example.sky_beat.R;

import static com.example.sky_beat.Activity.MainActivity.musicFile;

public class SongFragment extends Fragment {

    RecyclerView recyclerView;
    public  static SongAdapter songAdapter;

    public SongFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        songAdapter = new SongAdapter(musicFile, getContext());
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(layoutManager);
        return view;

    }
}