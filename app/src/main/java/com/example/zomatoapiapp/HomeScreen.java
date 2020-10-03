package com.example.zomatoapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageView;

public class HomeScreen extends AppCompatActivity {
EditText Cities;
Button btn_next;
TextView city_name;
TextView Country;
    String myResponse;
    GifImageView dancer;
    String city_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
Cities=findViewById(R.id.Cities_input);
btn_next=findViewById(R.id.btn_next);
        Country=findViewById(R.id.Country);
        Country.setVisibility(View.INVISIBLE);
        city_name=findViewById(R.id.city_name);
        city_name.setVisibility(View.INVISIBLE);
        dancer=findViewById(R.id.dancer);
        dancer.setVisibility(View.INVISIBLE);
        ConnectivityManager connectivityManager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
        {
            //internet is inactive
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.alert_dialog);
            //outside touch
            dialog.setCanceledOnTouchOutside(false);
            //width and height
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT
                    ,WindowManager.LayoutParams.WRAP_CONTENT);
            //Set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//animation
            dialog.getWindow().getAttributes().windowAnimations=android.R.style.Animation_Dialog;
            Button btTryAgain=dialog.findViewById(R.id.bt_try_again);
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();
        }
        else {


            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if ()
                    OkHttpClient client = new OkHttpClient();
                    final ProgressDialog progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setMessage("Please Wait!\nLoading Data");
                    progressDialog.show();
                    String getCities = Cities.getText().toString();

                    final okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("https://developers.zomato.com/api/v2.1/locations?query=" + getCities)
                            .header("user-key", "1b3c8b37ea96785391fa55c288ac385c")
                            .get()
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                myResponse = response.body().string();
                                progressDialog.dismiss();
                                try {
                                    JSONObject o = new JSONObject(myResponse);
                                    JSONArray location_suggestions = o.getJSONArray("location_suggestions");
                                    JSONObject entity_type = location_suggestions.getJSONObject(0);
                                    // entity_type.getString("entity_id");

                                    Log.i("ENTITY ID", entity_type.getString("entity_id"));
                                    city_id=entity_type.getString("entity_id");
                                    HomeScreen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            city_name.setVisibility(View.VISIBLE);
                                            Country.setVisibility(View.VISIBLE);
                                            dancer.setVisibility(View.VISIBLE);
                                            btn_next.setEnabled(false);
                                            btn_next.setBackgroundColor(getResources().getColor(R.color.backBtn));
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    Intent mainIntent = new Intent(HomeScreen.this, MainActivity.class);
                                                    mainIntent.putExtra("city_id",city_id);
                                                    startActivity(mainIntent);
                                                }
                                            }, 5000);

                                        }
                                    });

                                    city_name.setText(entity_type.getString("city_name"));

                                    Country.setText(entity_type.getString("country_name"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.i("Skipped", "Error");

                            }
                        }
                    });


                }
            });
        }

    }
}