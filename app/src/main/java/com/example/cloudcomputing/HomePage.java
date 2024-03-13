package com.example.cloudcomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class HomePage extends AppCompatActivity {
    EditText enterCity, enterCountry;
    TextView textViewResults;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "9121661dcf739d9f2cf503dcd947be35";
    private final String lang = "pl";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        enterCity = findViewById(R.id.enterCity);
        enterCountry = findViewById(R.id.enterCountry);
        textViewResults = findViewById(R.id.textViewResults);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = enterCity.getText().toString().trim();
        String country = enterCountry.getText().toString().trim();

        if(city.equals("")){
            tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid + "&lang=" + lang;
        }
        else {
            tempUrl = url + "?q=" + city + "&appid=" + appid + "&lang=" + lang;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    int temp = (int) (jsonObjectMain.getDouble("temp") - 273);
                    int feelslike = (int) (jsonObjectMain.getDouble("feels_like") - 273);
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryname = jsonObjectSys.getString("country");
                    String cityname = jsonResponse.getString("name");
                    textViewResults.setTextColor(Color.BLACK);
                    output += "Aktualna pogoda dla miasta " + cityname + " (" + countryname + ")"
                            + "\nTemperatura: " + df.format(temp) +  " ℃"
                            + "\nTemperatura odczuwalna: " + df.format(feelslike) + " ℃"
                            + "\nOpis: " + description;
                    textViewResults.setText(output);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}