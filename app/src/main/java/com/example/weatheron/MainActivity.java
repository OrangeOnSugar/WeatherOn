package com.example.weatheron;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button okey;
    RelativeLayout ok;
    String city="",getCitys;
    TextView cityN, graduS, pogoD, davleN, vlazH, vateR;
    int trys=0;
    private RequestQueue mQueue;
    private RequestQueue mQueue1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okey = (Button) findViewById(R.id.checkes);
        ok = (RelativeLayout) findViewById(R.id.pek);
        cityN = (TextView) findViewById(R.id.citynameA);
        graduS = (TextView) findViewById(R.id.gradus);
        pogoD = (TextView) findViewById(R.id.pogod);
        davleN = (TextView) findViewById(R.id.davlen);
        vlazH = (TextView) findViewById(R.id.vlazh);
        vateR = (TextView) findViewById(R.id.veter);
        mQueue= Volley.newRequestQueue(this);
        mQueue1= Volley.newRequestQueue(this);
        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opDil();
            }
        });
    }
    public void jsonParse()
    {
        String url="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20200127T021115Z.4b733ce5cb4923b2.1537223d16c21c0089e1d656b97ac03313c02630&text=" + city + "&lang=ru-en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            String s = response.getString("text"),ss="";
                            char[] prekol = s.toCharArray();
                            for(int i=2;i<prekol.length-2;i++)
                            {
                                ss=ss+prekol[i];
                            }
                            getCitys=ss;
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        mQueue1.add(request);
    }
    /*public  void  jsonWeather()
    {
        String url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=b1b35bba8b434a28a0be2a3e1071ae5b&units=metric&lang=ru";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            String s = response.getString("name");
                            trys=0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            trys=1;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }*/


    public void opDil() {
        try {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.message);

            final Button ok = dialog.findViewById(R.id.okeys), exit = dialog.findViewById(R.id.back);
            final EditText cityname1 = dialog.findViewById(R.id.citynameS);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        city = cityname1.getText().toString();
                        jsonParse();
                        city=getCitys;
                        jsonWeatherS();
                        if(trys==0)
                        {
                            dialog.hide();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Такого города либо не существует, либо вы написали его не в английской раскладке!",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });
            dialog.show();
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public  void  jsonWeatherS()
    {
        String url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=b1b35bba8b434a28a0be2a3e1071ae5b&units=metric&lang=ru";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            JSONObject main_object = response.getJSONObject("main");
                            JSONObject main_object1 = response.getJSONObject("wind");
                            JSONArray array=response.getJSONArray("weather");
                            JSONObject object=array.getJSONObject(0);
                            ok.setVisibility(View.VISIBLE);
                            graduS.setText(main_object.getInt("temp")+"°C");
                            cityN.setText(response.getString("name"));
                            davleN.setText("Атмосферное давление: "+main_object.getString("pressure")+" мм рт. ст.");
                            vlazH.setText("Влажность воздуха: "+main_object.getString("humidity")+"%");
                            vateR.setText("Ветер: " + main_object1.getString("speed") + " м/с");
                            pogoD.setText(object.getString("description"));
                            trys=0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            trys=1;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        mQueue.add(request);
    }

}

