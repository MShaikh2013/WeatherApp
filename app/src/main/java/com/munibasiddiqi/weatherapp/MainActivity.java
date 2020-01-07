package com.munibasiddiqi.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;

    TextView textViewWeatherDetail;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url = null;
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();

            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data!=-1){

                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");


                Log.i("Weather Contents", weatherInfo);

               JSONArray jsonArray = new JSONArray(weatherInfo);

               String message = "";

                for (int i=0; i<jsonArray.length(); i++) {

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    Log.i("main", jsonPart.getString("main"));

                    Log.i("description", jsonPart.getString("description"));

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if ((!main.equals("")) && (!description.equals(""))) {

                        message += main + ": " + description + "\r\n";
                    }

                }

                if (!message.equals("")) {
                    textViewWeatherDetail.setText(message);
                }else{

                    textViewWeatherDetail.setText(message);

                    Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();

            }


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       cityEditText = (EditText) findViewById(R.id.editTextCity);

       textViewWeatherDetail = (TextView) findViewById(R.id.textViewWeatherDetail);



    }

    public void findWeather(View view){

        DownloadTask task = new DownloadTask();

        textViewWeatherDetail.setText(null);

        Log.i("city", cityEditText.getText().toString());

        //"+cityEditText.getText().toString()+"

        try {

            task.execute("https://openweathermap.org/data/2.5/weather?q="+cityEditText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityEditText.getWindowToken(),0);

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_LONG).show();
        }



    }

}
