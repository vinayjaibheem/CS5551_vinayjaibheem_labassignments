package com.example.rohithkumar.androidwearapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private TextView mTextView;
    private EditText key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        mTextView = (TextView) findViewById(R.id.textView_display);
        Button button = (Button) findViewById(R.id.button2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void weather(View v) {
        key = (EditText) findViewById(R.id.editext_cityname);
        String s = key.getText().toString();
        String queryStringTwo = s.replace(" ", "_");
        String getURL = "http://api.wunderground.com/api/4bbbc25f4f5946dd/conditions/q/" + queryStringTwo + ".json";
        //Toast.makeText(this, "get url="+getURL, Toast.LENGTH_SHORT).show();
        String response = null;
        BufferedReader bfr = null;
        try {
            URL url = new URL(getURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = bfr.readLine()) != null) {
                // Append server response in string
                sb.append(line + " ");
            }
            response = sb.toString();

        } catch (Exception ex) {
            //Toast.makeText(this, "weather button clicked exception", Toast.LENGTH_SHORT).show();
            String Error = ex.getMessage();
        } finally {
            try {
                bfr.close();
            } catch (Exception ex) {

            }
        }
        String queryStringOne = null;
        JSONObject jsonResult;
        try {
            jsonResult = new JSONObject(response);
            JSONObject a = jsonResult.getJSONObject("response");
            JSONArray array = a.optJSONArray("results");
            for(int t=0;t<array.length();t++){
                if(array.getJSONObject(t).getString("country_name").equalsIgnoreCase("USA")){
                    queryStringOne = array.getJSONObject(t).getString("state");
                    break;
                };
            }
           // Toast.makeText(this, "queryStringOne="+queryStringOne, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String UR = "http://api.wunderground.com/api/4bbbc25f4f5946dd/conditions/q/" + queryStringOne + "/" + queryStringTwo + ".json";
        //Toast.makeText(this, "my url="+UR, Toast.LENGTH_SHORT).show();
        String res = null;
        BufferedReader bf = null;
        try {
            URL url = new URL(UR);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder str = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = bf.readLine()) != null) {
                // Append server response in string
                str.append(line + " ");
            }
            res = str.toString();
           // Toast.makeText(this, " "+res, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            String Error = ex.getMessage();
        } finally {
            try {
                bf.close();
            } catch (Exception ex) {
                String Error = ex.getMessage();
            }
        }
        try {
            JSONObject f = new JSONObject(res);
            JSONObject t = f.getJSONObject("current_observation");
            String dis = t.getString("relative_humidity");
           String city1=  ((EditText) findViewById(R.id.editext_cityname)).getText().toString();
            if(dis==null){
                Toast.makeText(this, "Sorry couldn't get data....!", Toast.LENGTH_SHORT).show();
            }
            mTextView.setText("Humidity in "+city1+":"+"\n" + dis.toString());
        } catch (Exception e) {
           // Toast.makeText(this, "weather button parse clicked", Toast.LENGTH_SHORT).show();
            String Error = e.getMessage();
        }
    }
}
