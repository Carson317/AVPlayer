package com.ch.vaplayer.media;

import android.graphics.Bitmap;

/**
 * Created by heng.cao on 9/7/16.
 */
public class LoadedImage {
    Bitmap mBitmap;

    public LoadedImage(Bitmap bitmap){
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
