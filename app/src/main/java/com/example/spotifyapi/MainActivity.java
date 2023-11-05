package com.example.spotifyapi;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //The Client Id is taken from the registration of the app in spotify.developers and the redirect id is the url of the page inside the developers dashboard.
    private static final String CLIENT_ID = "239ce54eb5294430a6cd489a95f7aeec";   // 클라이언트 id
    private static final String REDIRECT_URI = "https://developer.spotify.com/dashboard/applications/239ce54eb5294430a6cd489a95f7aeec";  // redirect url 입력하기
    private SpotifyAppRemote mSpotifyAppRemote;
    private boolean pause;

    // 기본으로 설정되어 있는 감정(neutrality)
    private String mood = "neutrality";
    
    // 재생할 노래
    public List<String> playedSongs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     // activity_main과 연동


    }

    // 스포티파이 api를 사용하기 위한 사용자 인증
    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)     // 클라이언트 id
                        .setRedirectUri(REDIRECT_URI)       // redirect_url
                        .showAuthView(true)
                        .build();


        // 스포티파이 api 연결(this 에러)
        SpotifyAppRemote.connect(this ,connectionParams,
                new Connector.ConnectionListener() {
            @Override
            
            // 성공한 경우
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected successfully!");

            }

            // 실패한 경우
            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }

        });
    }
    
    // 실패한 경우
    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }



    // play 버튼을 누르면 노래를 재생하기 위해 값이 spinner로 이동
    public void playSong(View MainActivity) {
        Spinner spinner;
        spinner = findViewById(R.id.spinner);

        mood = spinner.getSelectedItem().toString().toLowerCase();
        setContentView(R.layout.activity_main);

        play(mood);
    }

    // 현재의 감정에 대한 노래 플레이리스트 재생하기 
    @SuppressLint("SetTextI18n")
    private void play(String key) {
        // string key가 현재의 감정
        playerSetup();

        // 현재의 감정에 대한 음악리스트(출력)
        switch (key) {

            // angry playlist: https://open.spotify.com/playlist/609gQW5ztNwAkKnoZplkao?si=84e754a6817f489f
            case "angry":        // mood: angry
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:609gQW5ztNwAkKnoZplkao");

                break;

                // anxious playlist: https://open.spotify.com/playlist/0uCjaUdAss2kM3gHIOBrY8?si=6a32f5ff3ba64e65

            case "anxious":    // mood: anxious
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:0uCjaUdAss2kM3gHIOBrY8");

                break;

            //https://open.spotify.com/playlist/4STj00No69Jjdoiop0fzDC?si=c28e7cd8e9a24d3a

            case "embarrassed":   // mood: embarrassed
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:4STj00No69Jjdoiop0fzDC");

                break;

            //https://open.spotify.com/playlist/5apyfDc5hudFwYTfeMbF6t?si=c7836a75e74e4993
            //https://open.spotify.com/playlist/3796IJquuU9QEAdIP2Gpo7?si=470473eaec97418c
            case "happy":        // mood: happy
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:3796IJquuU9QEAdIP2Gpo7");

                break;

            // https://open.spotify.com/playlist/4ekUsAoB1iIj8Z2FAFg4kY?si=7d4fa2a25f08460a
            case "hurt":     // mood: hurt
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:4ekUsAoB1iIj8Z2FAFg4kY");

                break;

            //https://open.spotify.com/playlist/2ialHGHKmEjAK522BxXEfH?si=2884a58257db48fb
            case "sad":        // mood: sad
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:2ialHGHKmEjAK522BxXEfH");

                break;
            //https://open.spotify.com/playlist/6Gbg57fuSoGf40qMLOG3u3?si=6f325f9343444682
            case "neutrality":
            default:      // 기본으로 설정된 음악 플레이리스트
                mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:6Gbg57fuSoGf40qMLOG3u3");

        }


    }

   // 현재 노래에 대한 이미지 올리기(노래제목, 아티스트, 노래 그림)
    @SuppressLint("SetTextI18n")
    private void updateSongAndImage(TextView song, TextView artist, ImageView songImage){


        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
            final Track track = playerState.track;
            if (track != null) {
                Log.d("MainActivity", track.name + " by " + track.artist.name);
                song.setText(track.name);
                artist.setText("by " + track.artist.name);

                if (!playedSongs.contains(track.name + "\n by \n" + track.artist.name + "\n\n\n")) {
                    playedSongs.add(track.name + "\n by \n" + track.artist.name + "\n\n\n");
                    Log.d("MainActivity", playedSongs.get(playedSongs.size() - 1));


                }

            }
        });

        // 이미지 가져오기
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> {
            final Track track = playerState.track;
            if (track != null) {
                mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(bitmap -> {
                    
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);

                    final float roundPx = (float) bitmap.getWidth() * 0.06f;
                    roundedBitmapDrawable.setCornerRadius(roundPx);

                    songImage.setImageDrawable(roundedBitmapDrawable);
                });
            }


        });
    }
    // player layout -> main으로 이동
    public void moodShift(View activity_player) {

        setContentView(R.layout.activity_main);


    }
    
    public void player(View MainActivity) {

        setContentView(R.layout.activity_player);
        playerSetup();


    }
    
    // theme에 backgroundColor 및 textColor 설정
    // 선택된 mood에 따른 play
    
    @SuppressLint("SetTextI18n")
    private void playerSetup()
    {

        TextView song = (TextView) findViewById(R.id.songName);
        TextView artist = (TextView) findViewById(R.id.songAuthor);
        ImageView songImage = (ImageView) findViewById(R.id.songImage);
        ConstraintLayout player = (ConstraintLayout) findViewById(R.id.activityPlayer);
        TextView currentMood = (TextView) findViewById(R.id.mood);
        String capital = mood.substring(0, 1).toUpperCase() + mood.substring(1);
        currentMood.setText(capital + "!");
        switch (mood) {

            case "angry":
                player.setBackgroundColor(Color.rgb(201, 41, 41));
                currentMood.setTextColor(Color.WHITE);
                song.setTextColor(Color.WHITE);
                artist.setTextColor(Color.WHITE);
                break;

            case "anxious":
                player.setBackgroundColor(Color.rgb(253, 248, 226));
                currentMood.setTextColor(Color.BLACK);
                song.setTextColor(Color.BLACK);
                artist.setTextColor(Color.BLACK);
                break;

            case "embarrassed":
                player.setBackgroundColor(Color.rgb(201, 147, 111));
                currentMood.setTextColor(Color.BLACK);
                song.setTextColor(Color.BLACK);
                artist.setTextColor(Color.BLACK);
                break;


            case "happy":
                player.setBackgroundColor(Color.rgb(255, 154, 85));
                currentMood.setTextColor(Color.BLACK);
                song.setTextColor(Color.BLACK);
                artist.setTextColor(Color.BLACK);

                break;

            case "hurt":
                player.setBackgroundColor(Color.rgb(51, 36, 36));
                currentMood.setTextColor(Color.WHITE);
                song.setTextColor(Color.WHITE);
                artist.setTextColor(Color.WHITE);
                break;


            case "sad":
                player.setBackgroundColor(Color.rgb(42, 59, 144));
                currentMood.setTextColor(Color.WHITE);
                song.setTextColor(Color.WHITE);
                artist.setTextColor(Color.WHITE);
                break;

            case "neutrality":
            default:    // 기본으로 설정된 음악플레이리스트
                player.setBackgroundColor(Color.rgb(15, 174, 122));
                currentMood.setTextColor(Color.BLACK);
                song.setTextColor(Color.BLACK);
                artist.setTextColor(Color.BLACK);
        }
        updateSongAndImage(song,artist,songImage);
    }

    public void record(View MainActivity) {
        setContentView(R.layout.record);
    }

    public void updateList(View MainActivity) {
        TextView list = (TextView) findViewById(R.id.recordList);
        if (playedSongs.isEmpty()) return;
        list.setMovementMethod(new ScrollingMovementMethod());
        if (!list.getText().toString().isEmpty()) {
            list.setText("");
        }
        updateListItem(list);
    }


    // 재생할 노래 리스트 가져오기
    private void updateListItem(TextView list) {
        for (String item : playedSongs) {
            list.append(item);
            Log.d("MainActivity", item);
        }
    }

    // 다음 곡 스킵
    public void next(View MainActivity) {
        mSpotifyAppRemote.getPlayerApi().skipNext().setResultCallback(empty -> {


        });
    }
   
    // 이전 곡 요청
    public void previous(View MainActivity) {
        mSpotifyAppRemote.getPlayerApi().skipPrevious().setResultCallback(empty -> {


        });
    }
    
    // 버튼 클릭 시 현재 노래 재생 및 멈춤
    public void PlayOrPause(View MainActivity) {
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState -> pause = playerState.isPaused);

        if (pause) {
            resume();
        } else {
            stop();
        }
    }
    
    // player 재생 멈춤
    private void stop() {
        mSpotifyAppRemote.getPlayerApi().pause();
    }
    //Resumes the player
    private void resume() {
        mSpotifyAppRemote.getPlayerApi().resume();
    }

}