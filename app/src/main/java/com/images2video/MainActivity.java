package com.images2video;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.leminhtuan.imagestovideo.FFMPEGMainActivity;
import com.leminhtuan.imagestovideo.FFMPEGManager;
import com.leminhtuan.imagestovideo.Image2Video;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Clicked", "xxx");

//                Image2Video.instance.initialize(getApplication());
//                Image2Video.instance.goToFFMPEGMainActivity(null);

                FFMPEGManager.getInstance(getApplication()).loadFFMPEG();
            }
        });

    }
}
