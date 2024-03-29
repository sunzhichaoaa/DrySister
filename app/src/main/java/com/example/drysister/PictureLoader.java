package com.example.drysister;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.LogRecord;

public class PictureLoader {
    private ImageView loadImg;
    private String imgUrl;
    private byte[] picByte;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x123){
                if(picByte != null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte,0,picByte.length);
                    loadImg.setImageBitmap(bitmap);
                }
            }
        }
    };
    public void load(ImageView loadImg, String imgUrl) {
        this.loadImg = loadImg;
        this.imgUrl = imgUrl;
        Drawable drawable = loadImg.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(PictureLoader.this.imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    Log.i("好好学习1", "run: " +PictureLoader.this.imgUrl);
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length = in.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, length);
                        }
                        picByte = outputStream.toByteArray();
                        in.close();
                        outputStream.close();
                        handler.sendEmptyMessage(0x123);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }


}


