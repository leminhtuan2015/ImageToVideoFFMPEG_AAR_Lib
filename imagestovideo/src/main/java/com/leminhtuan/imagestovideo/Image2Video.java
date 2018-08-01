package com.leminhtuan.imagestovideo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

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

    public void goToFFMPEGMainActivity(){
        Log.d("goToFFMPEGMainActivity", "goToFFMPEGMainActivity");
        Intent intent = new Intent(application, FFMPEGMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }
}
