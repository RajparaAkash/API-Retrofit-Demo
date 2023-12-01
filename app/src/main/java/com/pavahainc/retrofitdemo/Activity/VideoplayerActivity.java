package com.pavahainc.retrofitdemo.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.pavahainc.retrofitdemo.R;

public class VideoplayerActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);

        videoView = findViewById(R.id.videoView);

        Bundle bundle = getIntent().getExtras();
        String id = (String) bundle.getString("id");

        String path = "http://159.65.146.129/EKYPBDF/78805655/" + id + ".mp4";

        Uri uri = Uri.parse(path);

        videoView.setVideoPath(path);
        videoView.setVideoURI(uri);
        videoView.start();
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }
}
