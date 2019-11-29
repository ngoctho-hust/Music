package com.example.music;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageButton btnPrev, btnPlay, btnNext, btnShuffle;
    public static ArrayList<Song> arraySong;
    public static int position = 0;
    public static MediaPlayer mediaPlayer;
    ListView listViewPlaylist;
    SongAdapter songAdapter;
    boolean isClicked = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.BLACK);


        anhXa();
        addSong();

        khoiTaoMediaPlayer();

        khoiTaoListViewPlaylist();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    //neu dang phat -> pause va doi hinh
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position > arraySong.size() - 1) {
                    position = 0;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                khoiTaoMediaPlayer();
                btnPlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position < 0) {
                    position = arraySong.size()-1;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                khoiTaoMediaPlayer();
                btnPlay.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                setTimeTotal();
                updateTimeSong();
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(simpleDateFormat.format(skSong.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });

        listViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pstn, long id) {
                parent.getChildAt(position).setAlpha(1.0f);
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                parent.getChildAt(pstn).setAlpha(0.5f);
                position = pstn;
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                updateTimeSong();
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked){
                    btnShuffle.setBackgroundColor(Color.BLACK);
                    Collections.shuffle(arraySong);
                    isClicked = true;
                } else {
                    btnShuffle.setBackgroundColor(Color.TRANSPARENT);
                    Collections.sort(arraySong, new SortbyName());
                    isClicked = false;
                }
                khoiTaoListViewPlaylist();
            }
        });
    }

    public void khoiTaoListViewPlaylist() {
        songAdapter = new SongAdapter(this, arraySong);
        listViewPlaylist.setAdapter(songAdapter);
    }


    final Handler handler = new Handler();
    public void updateTimeSong(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position > arraySong.size() - 1) {
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        khoiTaoMediaPlayer();
                        btnPlay.setImageResource(R.drawable.pause);
                        mediaPlayer.start();
                        setTimeTotal();
                        updateTimeSong();
                    }
                });

                handler.postDelayed(this, 500);
            }
        }, 0);
    }

    public void setTimeTotal(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());
    }

    public void khoiTaoMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

    private void addSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Hai triệu năm",  R.raw.haitrieunam));
        arraySong.add(new Song("Lối Nhỏ",  R.raw.loinho));
        arraySong.add(new Song("Thanh xuân",  R.raw.thanhxuan));
        arraySong.add(new Song("Nếu em đi",  R.raw.neuemdi));
        arraySong.add(new Song("Nước mắt em lau bằng tình yêu mới",  R.raw.nuocmatemlaubangtinhyeumoi));
        Collections.sort(arraySong, new SortbyName());
    }

    private void anhXa(){
        txtTimeSong     = (TextView) findViewById(R.id.textViewTime);
        txtTimeTotal    = (TextView) findViewById(R.id.textViewTimeTotal);
        txtTitle        = (TextView) findViewById(R.id.textViewTitle);
        skSong          = (SeekBar) findViewById(R.id.seekBarSong);
        btnNext         = (ImageButton) findViewById(R.id.imageButtonNext);
        btnPlay         = (ImageButton) findViewById(R.id.imageButtonPlay);
        btnPrev         = (ImageButton) findViewById(R.id.imageButtonPrevious);
        listViewPlaylist    = (ListView) findViewById(R.id.listViewPlaylist);
        btnShuffle      = (ImageButton) findViewById(R.id.imageButtonShuffle);
    }
}
