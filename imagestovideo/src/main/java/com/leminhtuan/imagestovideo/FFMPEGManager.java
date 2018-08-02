package com.leminhtuan.imagestovideo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by fuji on 8/2/18.
 */

public class FFMPEGManager {
    private static FFmpeg ffmpeg;
    private static FFMPEGManager fFMPEGManager;
    private static Context context;
    private String imageData = "";
    private String outputPath = "/sdcard/color_by_number/images/";

    private FFMPEGManager(){

    }

    public static FFMPEGManager getInstance(Context context){

        if(fFMPEGManager == null){
            FFMPEGManager.context = context;
            ffmpeg = FFmpeg.getInstance(context);
            fFMPEGManager = new FFMPEGManager();
        }

        return fFMPEGManager;
    }

    public void convertImagesToVideo(String imageData){
        // [{"name" : "base64 value"}]

        this.imageData = imageData;
        new CreateImageFilesTask().execute("");
    }

    private void saveImageToSDCard(String base64, String outputPath, String imageName) {
        try {
            byte[] imageBytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(imageBytes);
            Bitmap image = BitmapFactory.decodeStream(is);

            String mFilePath = outputPath + imageName + ".png";

            File file = new File(mFilePath);

            FileOutputStream stream = new FileOutputStream(file);

            if (!file.exists()){
                file.createNewFile();
            }

            image.compress(Bitmap.CompressFormat.PNG, 100, stream);

            is.close();
            image.recycle();

            stream.flush();
            stream.close();
        } catch(Exception e) {
            Log.v("SaveFile",""+e);
        }
    }

    private void loadFFMPEG(){
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
        String s = "-f image2 " +  "-i " + outputPath + "img_%d.png" + " -vcodec libx264 " + outputPath + "video.mp4";

        String[] command = s.split(" ");

        Log.d("executeFFMPEG", s);

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

                    //CALLBACK
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }

    private class CreateImageFilesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                File file = new File(outputPath);

                if(file.isDirectory()){
                    String[] children = file.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(file, children[i]).delete();
                    }
                }

                file.mkdirs();

                JSONArray jsonArray = new JSONArray(imageData);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imageName = "img_" + i;
                    String base64 = jsonObject.getString(imageName);
                    //Log.d("base64", base64);
                    Log.d("imageName", imageName);

                    saveImageToSDCard(base64, outputPath, imageName);
                }

                loadFFMPEG();

            } catch (Exception e) {
                Log.d("convert ERROR",e.getMessage());
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
