package com.leminhtuan.imagestovideo;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FFMPEGMainActivity extends AppCompatActivity {

    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ff_activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onBackPressed", "onBackPressed");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //copyFileOrDir("images");
        ffmpeg = FFmpeg.getInstance(this);
        loadFfmpeg();
    }

    private void loadFfmpeg(){
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    executeFFMPEG();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

    private void executeFFMPEG(){
        String s = "-f image2 " +  "-i /sdcard/color_by_number/images/img_%d.png" + " -vcodec libx264 sdcard/color_by_number/_video.mp4";

        String[] command = s.split(" ");

        Log.d("executeFFMPEG xxx", s);

        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d("executeFFMPEG", "onStart: ");
                }

                @Override
                public void onProgress(String message) {
                    Log.d("executeFFMPEG", "onProgress: " + message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d("executeFFMPEG", "onFailure: " + message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("executeFFMPEG", "onSuccess: " + message);
                }

                @Override
                public void onFinish() {
                    Log.d("executeFFMPEG", "onFinish: ");


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }

    private void copyFileOrDir(String path) {
//        String to = "/data/data/";
        String to = "/sdcard/color_by_number/";
        AssetManager assetManager = this.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath =  to + /*this.getPackageName() + */ "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    dir.mkdir();
                for (int i = 0; i < assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
//        String to = "/data/data/";
        String to = "/sdcard/color_by_number/images/";
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = to + /*this.getPackageName() + */ "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
