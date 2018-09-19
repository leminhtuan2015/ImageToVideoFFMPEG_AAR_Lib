package com.leminhtuan.imagestovideo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.Dictionary;

/**
 * Created by fuji on 7/30/18.
 */

public class Image2Video {

    public static Image2Video instance = new Image2Video();

    private Application application = null;

    public void initialize(Application application){
        Log.d("initialize", "Unity Called");
        this.application = application;
    }

    public void goToFFMPEGMainActivity(String imageData){
        //Log.d("goToFFMPEGMainActivity", "goToFFMPEGMainActivity: " + imageData);
        Intent intent = new Intent(application, FFMPEGMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    public void convertImagesToVideo(String imageData, FFMPEGCallBack ffmpegCallBack){
        //Log.d("convertImagesToVideo", imageData);
        FFMPEGManager.getInstance(application).convertImagesToVideo(imageData, ffmpegCallBack);
    }

    public void convertImagesInFolderToVideo(String imageFolder, FFMPEGCallBack ffmpegCallBack, int quality){
        //Log.d("convertImagesToVideo", imageData);
        FFMPEGManager.getInstance(application).convertImagesInFolderToVideo(imageFolder, ffmpegCallBack, quality);
    }
}
