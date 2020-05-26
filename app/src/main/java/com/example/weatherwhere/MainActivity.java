package com.example.weatherwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String description="",temperature="",humidity="",city="",s="";
    EditText inputText;
    TextView output;

    public void weather(View view){

        city=inputText.getText().toString();

        getWeather task=new getWeather();
        try {
            s=task.execute("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=1836bdb051b97568759784f8897005b2").get();
            //s=task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02").get();
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray weather=jsonObject.getJSONArray("weather");
                JSONObject temp=jsonObject.getJSONObject("main");

                description=weather.getJSONObject(0).getString("main");
                temperature=String.format("%.2f",(Float.parseFloat(temp.getString("temp"))-273.15));
                humidity=temp.getString("humidity");



            } catch (JSONException e) {
                e.printStackTrace();
            }
            output.setText("WEATHER: "+description+"\nTEMPERATURE: "+temperature+" \u2103\nHUMIDITY: "+humidity+"%");
            //output.setText("DESCRIPTION:"+description);
        } catch (Exception e) {
            e.printStackTrace();
            output.setText("ERROR!!");
        }
        output.setVisibility(View.VISIBLE);
    }

    public class getWeather extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection;
            String result="";

            try {
                url =new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream in=url.openStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    result+=(char)data;
                    data=reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText=(EditText)findViewById(R.id.editText);
        output=(TextView)findViewById(R.id.textView);

        output.setVisibility(View.INVISIBLE);
    }
}
