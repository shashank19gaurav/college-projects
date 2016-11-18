package com.example.shashank.musicloud;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText usernameET, passwordEt;
    Button button;

    static String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usernameET = (EditText) findViewById(R.id.username);
        passwordEt = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.loginButton);

    }

    public void loginUser(View view) {
        //Toast.makeText(this, "Button Clicked", Toast.LENGTH_LONG).show();
        String username = usernameET.getText().toString();
        String password = passwordEt.getText().toString();
        new LoginTask().execute(username, password);
    }

    public void displayToast(String message, int success) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent goToPlayer = new Intent(this, MusicList.class);

        if(success==1) {
            startActivity(goToPlayer);
        }
    }

    public class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            HttpURLConnection connection;
            BufferedReader bufferedReader;

            String username = data[0];
            String password = data[1];
            String urlParameters = "username="+username+"&password="+password;

            try {
                URL url = new URL("http://api.bcnmusicloud.tv/v1/auth/token?"+urlParameters);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
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
                userToken = parentObject.getString("token");
                return  userToken;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return "nhp";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("nhp")) {
                displayToast("Sorry wrong username/password", 0);
            } else {
                displayToast("Logged In With Token = "+s, 1);
            }
        }
    }

}
