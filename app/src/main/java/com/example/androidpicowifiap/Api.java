package com.example.androidpicowifiap;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Api {
    private ExecutorService mExecutor;
    Handler mHandler;

    public interface IOnProcessedListener {
        public void onProcessed(String result);
    }

    public Api() {
        // Roughly based on https://stackoverflow.com/a/64736616
        mExecutor = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void TurnOn(IOnProcessedListener listener) {
        Runnable backgroundRunnable = new Runnable(){
            @Override
            public void run(){
                String result = turnOnOffHandler(true);

                listener.onProcessed(result);
            }
        };

        mExecutor.execute(backgroundRunnable);
    }

    public void TurnOff(IOnProcessedListener listener) {
        Runnable backgroundRunnable = new Runnable(){
            @Override
            public void run(){
                String result = turnOnOffHandler(false);
                listener.onProcessed(result);
            }
        };

        mExecutor.execute(backgroundRunnable);
    }

    private String turnOnOffHandler(Boolean isOn) {
        URL url = null;
        try {
            // Plain text communication
            // https://stackoverflow.com/a/50834600
            url = new URL("http://192.168.4.1/led.cgi");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            // https://stackoverflow.com/a/69037699
            urlConnection.setRequestMethod( "POST" );
            urlConnection.setDoOutput(true);
            String post_data=isOn ? "led_state=ON" : "led_state=OFF";
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(post_data.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            InputStream in = null;
            try {
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while (true) {
                try {
                    if (!((line = reader.readLine()) != null))
                        break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                result.append(line);

            }
            Log.d("myApp", result.toString());
            return result.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    public Boolean isOn(IOnProcessedListener listener) {
        return false;
    }
}
