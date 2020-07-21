package com.example.tabuaimusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Bundle musicBundle;
    ArrayList<File> songFileList;
    private RelativeLayout parentLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private String SpeechKeper = "";
    private ImageButton repeatBtn, shuffleBtn, previosBtn, playBtn, nextBtn;
    private TextView songDuration_elapsed, songTime, songNameDisplay;
    private SeekBar seekBar;
    private static MediaPlayer mP;
    private MusicUtilies Utilies;
    private CircleImageView circleImageView;
    private ArrayList<File> mySongs;
    private int position;
    private String songName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repeatBtn = findViewById(R.id.btn_repeat);
        shuffleBtn = findViewById(R.id.btn_shuffle);
        previosBtn = findViewById(R.id.btn_prev);
        playBtn = findViewById(R.id.btn_play);
        nextBtn = findViewById(R.id.btn_next);
        songDuration_elapsed = findViewById(R.id.music_duration_elapsed);
        songTime = findViewById(R.id.song_time);
        songNameDisplay = findViewById(R.id.song_name_play);
        seekBar = findViewById(R.id.seekBar);
        circleImageView = findViewById(R.id.image_center);

        Utilies = new MusicUtilies();

        Intent songData = getIntent();
        musicBundle = songData.getExtras();

        if(mP != null){
            mP.stop();
        }

        songFileList = (ArrayList) musicBundle.getParcelableArrayList("song");
        position = musicBundle.getInt("position", 0);
        intMusicPlayer(position);


        previosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mP.getCurrentPosition() > 0) {
                    PlayPreviousSong();
                    if (mP.isPlaying()) {
                        playBtn.setImageResource(R.drawable.ic_pause_circle);
                    } else {
                        playBtn.setImageResource(R.drawable.ic_play_circle);
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mP.getCurrentPosition() > 0) {
                    PlayNextSong();
                    if (mP.isPlaying()) {
                        playBtn.setImageResource(R.drawable.ic_pause_circle);
                    } else {
                        playBtn.setImageResource(R.drawable.ic_play_circle);
                    }
                }
            }
        });

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        VoicePermission();
        parentLayout = findViewById(R.id.parentLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> matchesFound = bundle.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);

                if (matchesFound != null) {

                    SpeechKeper = matchesFound.get(0);

                    if (SpeechKeper.equals("Pause the song") || SpeechKeper.equals("pause")) {

                        mP.pause();
                        playBtn.setImageResource(R.drawable.ic_play_circle);

                        Toast.makeText(MainActivity.this, "Command: " + SpeechKeper, Toast.LENGTH_SHORT).show();

                        rotateTheDisk();

                    } else if (SpeechKeper.equals("Play the song") || SpeechKeper.equals("play")) {

                        mP.start();
                        playBtn.setImageResource(R.drawable.ic_pause_circle);

                        Toast.makeText(MainActivity.this, "Command: " + SpeechKeper, Toast.LENGTH_SHORT).show();

                        rotateTheDisk();
                    } else if (SpeechKeper.equals("Play next song") || SpeechKeper.equals("next")) {

                        PlayNextSong();
                        Toast.makeText(MainActivity.this, "Command: " + SpeechKeper, Toast.LENGTH_SHORT).show();
                        rotateTheDisk();
                    } else {
                        if (SpeechKeper.equals("Play previous song") || SpeechKeper.equals("previous")) {

                            PlayPreviousSong();
                            Toast.makeText(MainActivity.this, "Command: " + SpeechKeper, Toast.LENGTH_SHORT).show();
                            rotateTheDisk();

                        }
                    }

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        speechRecognizer.startListening(speechIntent);

                        SpeechKeper = "";
                        break;

                    case MotionEvent.ACTION_UP:

                        speechRecognizer.stopListening();
                        break;
                }
                return false;
            }
        });
    }

    public void intMusicPlayer(int position) {

        if (mP != null && mP.isPlaying()) {
            mP.reset();
        }

        String name = songFileList.get(position).getName();
        songNameDisplay.setText(name);

        Uri songResource = Uri.parse(songFileList.get(position).toString());
        mP = MediaPlayer.create(getApplicationContext(), songResource);
        Utilies = new MusicUtilies();


        buttonPlayerAction();

        mP.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //set seek bar maximum...

                seekBar.setMax(mP.getDuration());

                //set maximum song duration or minutes

                String totTime = Utilies.TimeUtilies(mP.getDuration());
                songTime.setText(totTime);

                mP.start();

                if(mP.isPlaying()){
                    rotateTheDisk();
                    playBtn.setImageResource(R.drawable.ic_pause_circle);
                }else{
                    playBtn.setImageResource(R.drawable.ic_play_circle);
                }

            }
        });

        mP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //do some thing when the song is  finished playing
                playBtn.setImageResource(R.drawable.ic_play_circle);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //do something when the seek bar is changed

                if(fromUser){
                    mP.seekTo(progress);//seek the song
                    seekBar.setProgress(progress);// seek the progress
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //set seekBar to change with songs duration

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mP !=null){
                    try{
                        Message message = new Message();

                        message.what = mP.getCurrentPosition();
                        mHandler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }


    //create a handler to set progress...

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage( Message msg) {
            songDuration_elapsed.setText(Utilies.TimeUtilies(msg.what));//set timer for the time elapsed
            seekBar.setProgress(msg.what);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mP.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mP.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mP.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mP.pause();
    }

    private void PlayNextSong() {
        if (position < songFileList.size()-1) {

            //check if the current song posithin is less than the present song in the list
            //increase the position by 0ne to play next song in the list
            position++;

            playBtn.setImageResource(R.drawable.ic_play_circle);
        } else {
            //if the position is greater or equal to the number in the list , set the position to zero
            position = 0;
            playBtn.setImageResource(R.drawable.ic_pause_circle);
        }

        // play the song in the list with position

        intMusicPlayer(position);

        rotateTheDisk();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mP.start();
    }

    private void PlayPreviousSong() {
        if (position <=0) {

            // if the position of the song in the list is less or equal to zero
            position = songFileList.size()-1;

            playBtn.setImageResource(R.drawable.ic_play_circle);
        } else {

            position --;
            playBtn.setImageResource(R.drawable.ic_pause_circle);

        }

        intMusicPlayer(position);

        rotateTheDisk();

    }

    private void buttonPlayerAction() {

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mP.isPlaying()) {

                    mP.pause();
                    playBtn.setImageResource(R.drawable.ic_play_circle);
                } else {
                    mP.start();
                    playBtn.setImageResource(R.drawable.ic_pause_circle);
                }

                rotateTheDisk();

            }
        });

    }

    private void rotateTheDisk() {
         if(!mP.isPlaying()) return;

        circleImageView.animate().setDuration(100).rotation(circleImageView.getRotation() + 2f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateTheDisk();
                super.onAnimationEnd(animation);
            }
        });

    }


    private void VoicePermission() {

    }

}
