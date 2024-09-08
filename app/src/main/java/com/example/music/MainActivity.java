package com.example.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;
    MediaPlayer mp;
    ImageButton pause_btn;
    ImageButton prev_btn;
    ImageButton next_btn;
    TextView secondTime_tv;
    TextView firstTime_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.SeekBar0);
        pause_btn = findViewById(R.id.pause_btn);
        prev_btn = findViewById(R.id.prev_btn);
        next_btn = findViewById(R.id.next_btn);
        pause_btn.setImageResource(R.drawable.baseline_play_arrow_24);
        secondTime_tv=findViewById(R.id.secondtiming);
        firstTime_tv=findViewById(R.id.firsttiming);
        mp = MediaPlayer.create(this, R.raw.baqara);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        int duration=mp.getDuration();
        long minutes=TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds=TimeUnit.MILLISECONDS.toSeconds(duration);
        long restseconds=seconds%(minutes*60);
        String time=String.format("%02d:%02d", minutes,restseconds);
        secondTime_tv.setText(time);
            pause_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mp.isPlaying()){
                        mp.pause();
                        pause_btn.setImageResource(R.drawable.baseline_play_arrow_24);
                    }else{
                        mp.start();

                        pause_btn.setImageResource(R.drawable.baseline_pause_24);
                    }

                }
            });
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int progress=mp.getCurrentPosition()+10000;
                    seekBar.setProgress(progress);
                    mp.seekTo(progress);
                }
            });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress=mp.getCurrentPosition()-10000;
                seekBar.setProgress(progress);
                mp.seekTo(progress);
            }
        });
            seekBar.setMax(duration);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mp.getCurrentPosition());
                int currentPosition=mp.getCurrentPosition();
                int min=(currentPosition/1000)/60;
                int seconds=(currentPosition/1000)%60;
                String firstTime=String.format("%02d:%02d", min,seconds);
                firstTime_tv.setText(firstTime);

            };
        },0,900);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mp.seekTo(i);
                }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mp.pause();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mp.start();
                    pause_btn.setImageResource(R.drawable.baseline_pause_24);
                }
            });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
