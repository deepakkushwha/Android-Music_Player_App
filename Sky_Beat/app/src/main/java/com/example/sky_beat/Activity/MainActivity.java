package com.example.sky_beat.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import  androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.sky_beat.Adapter.SongAdapter;
import com.example.sky_beat.Fragment.AlbumFragment;
import com.example.sky_beat.Fragment.SongFragment;
import com.example.sky_beat.R;
import com.example.sky_beat.model.MusicFile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static ArrayList<MusicFile> musicFile;
    int RequestCode = 1;
    public static final String SENDER_FLOATING_BTN="SENDER_FLOATING_BTN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        int_viewPager();

    }

    //_______________________________Data Access From Sd Card___________________________________
    public static ArrayList<MusicFile> getAllAudio(Context context) {
        ArrayList<MusicFile> tempFile = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] Projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM
        };
        Cursor cursor = context.getContentResolver().query(uri, Projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String ID = cursor.getString(0);
                String TITLE = cursor.getString(1);
                String DURATION = cursor.getString(2);
                String DATA = cursor.getString(3);
                String ARTIST = cursor.getString(4);
                String ALBUM = cursor.getString(5);
                MusicFile musicFile = new MusicFile(DATA, TITLE, ARTIST, ALBUM, DURATION, ID);
                tempFile.add(musicFile);
            }
            cursor.close();

        }

        return tempFile;
    }

    //____________________________________Storage permission Code________________________________________
    private void permission() {


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode);
        } else {
            int_viewPager();
            musicFile = getAllAudio(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (RequestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                int_viewPager();
                musicFile = getAllAudio(this);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode);


            }
        }
    }

    private void int_viewPager() {
        ViewPager viewPager;
        TabLayout tabLayout;
        FloatingActionButton openMusicPlayer;
        FloatingActionButton floatingActionButton;
        floatingActionButton = findViewById(R.id.open_Music);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
        androidx.appcompat.widget.Toolbar toolbar;
        viewPager = findViewById(R.id.view_Pager);
        tabLayout = findViewById(R.id.tab_layout);
        toolbar = findViewById(R.id.toolBar);
toolbar.setTitle("Sky Beat");
        toolbar.setTitleTextColor(Color.CYAN);

        setSupportActionBar(toolbar);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongFragment(), "Songs");
        viewPagerAdapter.addFragment(new AlbumFragment(), "Albums");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        openMusicPlayer= findViewById(R.id.open_Music);
        openMusicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
               startActivity(intent);}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;
        androidx.appcompat.widget.SearchView searchView;
        getMenuInflater().inflate(R.menu.menu, menu);
        menuItem = menu.findItem(R.id.search_option);
     searchView = (SearchView) menuItem.getActionView();
       searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<MusicFile> mySong = new ArrayList<>();
        for(MusicFile song :musicFile )
        {
            if(song.getTitle().toLowerCase().contains(userInput))
            {
                mySong.add(song);
            }
        }
        SongFragment.songAdapter.UpdateList(mySong);
        return true;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        private ArrayList<String> titles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);

            this.titles = new ArrayList<>();

            this.fragments = new ArrayList<>();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public CharSequence getPageTitle(int position) {

            return titles.get(position);

        }
    }
}