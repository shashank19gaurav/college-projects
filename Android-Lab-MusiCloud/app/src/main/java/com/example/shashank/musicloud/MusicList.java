package com.example.shashank.musicloud;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MusicList extends AppCompatActivity {

    public String totalCount;
    Track[] tracks;
    static int tracksCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        new FetchTrackTask().execute();
    }

    public int populateList() {
        ListAdapter songListAdapter = new CustomSongAdapter(this, tracks);
        ListView songListView = (ListView) findViewById(R.id.songList);
        songListView.setAdapter(songListAdapter);
        return 1;
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public class FetchTrackTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            HttpURLConnection connection;
            BufferedReader bufferedReader;

            try {
                URL url = new URL("http://api.bcnmusicloud.tv/v1/tracks/latest?limit=100");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                InputStream stream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(stream));

                String line;

                StringBuffer response = new StringBuffer();
                while((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();

                JSONObject parentObject = new JSONObject(response.toString());
                totalCount = parentObject.getString("total");

                JSONArray trackArray = parentObject.getJSONArray("data");

                tracksCount = 0;

                tracks = new Track[100];
                for (int i=0; i < trackArray.length(); i++)
                {
                    try {
                        JSONObject oneObject = trackArray.getJSONObject(i);
                        // Pulling items from the array
                        Track track = new Track(oneObject.getString("title"), oneObject.getString("trackArtLink"), oneObject.getString("id"), oneObject.getString("trackDownloadLink"));

                        tracks[tracksCount++] = track;
                    } catch (JSONException e) {
                        // Oops
                    }
                }


                return  "hopaaya";

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return "nhp";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("nhp")) {
                displayToast("Sorry Could Not fetch the tracks");
            }
            displayToast(totalCount);
            populateList();
        }
    }


}
