package com.travels.searchtravels.api;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.google.api.services.vision.v1.model.Image;

public class VisionApi {
    public static Thread findLocation(Image bitmap, String token, OnVisionApiListener onVisionApiListener) {
        Thread thread = new Thread(() -> {
            new VisionApi2(onVisionApiListener).findLocation(bitmap, token);
        });
        thread.start();
        return thread;
    }
}
