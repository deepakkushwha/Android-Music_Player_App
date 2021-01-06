package com.example.sky_beat.Activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sky_beat.R;
import com.example.sky_beat.model.MusicFile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.example.sky_beat.Activity.MainActivity.musicFile;
import static com.example.sky_beat.Adapter.Album_Details_Adapter.album_Music;
import static com.example.sky_beat.Adapter.SongAdapter.musicFilesSongAdapter;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    static MediaPlayer mediaPlayer;
    static boolean shuffleBoolean = false, repeatBoolean;
    ArrayList<MusicFile> listSong = new ArrayList<>();
    Uri uri;
    Handler handler = new Handler();
    Thread playThread, prevThread, nextThread;
    private int position = -1;
    private TextView songName, durationPlayed, totalDuration;
    private ImageView shuffleOnOff, playPrev, playNext, repeatOnOff, menuBtn, backBtn, songImg;
    private FloatingActionButton playPause;
    private SeekBar songSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initView();
        getIntentMethod();
        mediaPlayer.setOnCompletionListener(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    songSeekBar.setProgress(mCurrentPosition);
                    durationPlayed.setText(formattedTime(mCurrentPosition));

                }
                handler.postDelayed(this, 1000);

            }
        });
        shuffleOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffleBoolean) {
                    shuffleBoolean = false;
                    shuffleOnOff.setImageResource(R.drawable.ic_shuffle_off);
                } else {
                    shuffleBoolean = true;
                    shuffleOnOff.setImageResource(R.drawable.ic_shuffle_on);

                }
            }
        });
        repeatOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    repeatOnOff.setImageResource(R.drawable.ic_repeat_off);
                } else {

                    repeatBoolean = true;
                    repeatOnOff.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });
    }

    //_____________________Set Time Formate ________________________________
    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String second = String.valueOf(mCurrentPosition % 60);
        String minute = String.valueOf(mCurrentPosition / 60);
        totalOut = minute + ":" + second;
        totalNew = minute + ":" + "0" + second;
        if (second.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }

    }

    private void getIntentMethod() {

        position = getIntent().getIntExtra("position", -1);
        String sender =  getIntent().getStringExtra("Sender");
        if(sender!=null && sender.equals("albumDetails")) {
        listSong = album_Music;}
        else {
            listSong = musicFilesSongAdapter;
        }if (listSong != null) {
            playPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(listSong.get(position).getPath());
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();

        }
        songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
        metaDAta(uri);
    }

    //_____________________________Set Image & Title in this Method_____________________________
    private void metaDAta(Uri uri) {

        int TotalDuration = Integer.parseInt(listSong.get(position).getDuration()) / 1000;
        totalDuration.setText(formattedTime(TotalDuration));
        songName.setText(listSong.get(position).getTitle());

        byte[] image = getAlbumArt(uri.getPath());
        if (image != null) {
            Glide.with(this).asBitmap().load(image).into(songImg);
        } else {
            Glide.with(this)
                    .load(R.drawable.listimage)
                    .into(songImg);
        }

    }

    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @Override
    protected void onPostResume() {
        playThreadBtn();
        prevThreadBtn();
        nextThreadBtn();
        super.onPostResume();
    }

    // ____________________________playPrev coding___________________________________
    private void playThreadBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }

                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            playPause.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
            mediaPlayer.setOnCompletionListener(this);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        songSeekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));

                    }
                    handler.postDelayed(this, 1000);

                }
            });

        } else {
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
            mediaPlayer.setOnCompletionListener(this);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        songSeekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));

                    }
                    handler.postDelayed(this, 1000);

                }
            });

        }
    }

    //____________________________playPrev coding___________________________________
    private void prevThreadBtn() {
        prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSong.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSong.size() - 1) : (position - 1));
            }
            uri = Uri.parse(listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaDAta(uri);
            songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        songSeekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));

                    }
                    handler.postDelayed(this, 1000);

                }
            });
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSong.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position - 1) < 0 ? (listSong.size() - 1) : (position - 1));
            }
            uri = Uri.parse(listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaDAta(uri);
            songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        songSeekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));

                    }
                    handler.postDelayed(this, 1000);

                }
            });
            playPause.setImageResource(R.drawable.ic_play);
        }
        mediaPlayer.setOnCompletionListener(this);

    }

    // ____________________________playPrev coding___________________________________
    private void nextThreadBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextBtnClicked();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSong.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSong.size());
            }
            uri = Uri.parse(listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaDAta(uri);
            songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
            mediaPlayer.setOnCompletionListener(this);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        songSeekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));

                    }
                    handler.postDelayed(this, 1000);

                }
            });
            playPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if (shuffleBoolean && !repeatBoolean) {
                position = getRandom(listSong.size() - 1);
            } else if (!shuffleBoolean && !repeatBoolean) {
                position = ((position + 1) % listSong.size());
            }
            uri = Uri.parse(listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaDAta(uri);
            songSeekBar.setMax(mediaPlayer.getDuration() / 1000);
            mediaPlayer.setOnCompletionListener(this);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        songSeekBar.setProgress(mCurrentPosition);
                        durationPlayed.setText(formattedTime(mCurrentPosition));

                    }
                    handler.postDelayed(this, 1000);

                }
            });
            playPause.setImageResource(R.drawable.ic_play);
        }
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    private void initView() {

        songName = findViewById(R.id.song_name);
        durationPlayed = findViewById(R.id.duration_played);
        totalDuration = findViewById(R.id.duration_total);
        shuffleOnOff = findViewById(R.id.shuffle_on);
        playPrev = findViewById(R.id.play_prev);
        playNext = findViewById(R.id.play_next);
        repeatOnOff = findViewById(R.id.repeat);
        menuBtn = findViewById(R.id.menu_btn);
        backBtn = findViewById(R.id.back_btn);
        playPause = findViewById(R.id.play_pause);
        songSeekBar = findViewById(R.id.seekbar);
        songImg = findViewById(R.id.music_art);

    }

    //____________________This () is for Automatic Called next  process_________________________
    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnClicked();
        if (mediaPlayer != null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
playPause.setImageResource(R.drawable.ic_pause);
    }
}