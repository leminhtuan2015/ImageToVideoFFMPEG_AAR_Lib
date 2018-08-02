package com.leminhtuan.imagestovideo;

/**
 * Created by fuji on 8/2/18.
 */

public interface FFMPEGCallBack {
    public void onSuccess(String videoPath);
    public void onError(String errorMessage);
}
