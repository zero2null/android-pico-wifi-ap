package com.example.androidpicowifiap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidpicowifiap.databinding.FragmentFirstBinding;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       NavHostFragment.findNavController(FirstFragment.this)
                                                               .navigate(R.id.action_FirstFragment_to_SecondFragment);

                                                      new HttpTask().execute("http://192.168.4.1"); //192.168.4.1

                                                   }
                                               }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class HttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strURLs) {
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
                String post_data="led_state=ON";
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

        @Override
        protected void onPostExecute(String result) {
            Log.d("MyApp", result);
        }
    }

}