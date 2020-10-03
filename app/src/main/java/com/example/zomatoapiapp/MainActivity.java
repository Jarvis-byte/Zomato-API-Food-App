package com.example.zomatoapiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    List<ListItem>listItems;
    String myResponse;
    String city_id="NOOB";
    String savedCity_id;
    SharedPreferences x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems=new ArrayList<>();
        Intent y=getIntent();
        city_id = y.getStringExtra("city_id");
        x=this.getSharedPreferences("com.example.zomatoapiapp",Context.MODE_PRIVATE);
        if(city_id == null||city_id.length()==0)
        {
            savedCity_id = x.getString("city","");
            Log.i("if",savedCity_id);
           // Toast.makeText(this, savedCity_id, Toast.LENGTH_SHORT).show();
        }
        else
        {
            x.edit().putString("city",city_id).apply();
            savedCity_id=x.getString("city","");
            Log.i("Saved",x.getString("city",""));
            //Toast.makeText(this, savedCity_id, Toast.LENGTH_SHORT).show();
        }


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
            OkHttpClient client = new OkHttpClient();
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Please Wait!\nLoading Data");
            progressDialog.show();

            okhttp3.Request request=new okhttp3.Request.Builder()
                    .url("https://developers.zomato.com/api/v2.1/search?entity_id=" + savedCity_id + "&entity_type=city")
                    .header("user-key","1b3c8b37ea96785391fa55c288ac385c")
                    .get()
                    .build();
            Log.i("HTTPS ","Request");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("Onfailed","Failed");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if (response.isSuccessful())
                    {
                        Log.i("OnResponse","SuccessFull");
                        myResponse=response.body().string();
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject =new JSONObject(myResponse);
                            JSONArray array=jsonObject.getJSONArray("restaurants");
                            if (array.length()==0)
                            {

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "No Restaurant Found! SORRY...", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(MainActivity.this,HomeScreen.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            else {
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject o = array.getJSONObject(i);
                                    JSONObject rest = o.getJSONObject("restaurant");
                                    JSONObject rating = rest.getJSONObject("user_rating");
                                    JSONObject loc = rest.getJSONObject("location");
                                    //  Log.i("Rating",rating.toString());
                                    ListItem item = new ListItem(
                                            rest.getString("name"),
                                            rest.getString("cuisines"),
                                            rest.getString("timings"),
                                            rating.getString("aggregate_rating"),
                                            rest.getString("phone_numbers"),
                                            rest.getString("photos_url"),
                                            rating.getString("votes"),
                                            loc.getString("address"),
                                            loc.getString("city")
                                    );
                                    listItems.add(item);
                                }
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new MyAdapter(listItems, getApplicationContext());
                                        recyclerView.setAdapter(adapter);
                                    }
                                });


                            } } //try end
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //if end
                    else {
                        Log.i("Skipped","Error");
                    }
                }
            });

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        MenuItem searchitem=menu.findItem(R.id.action_search);
        //  android.widget.SearchView searchView=(SearchView)searchitem.getActionView();
        SearchView searchView=(SearchView)searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this,HomeScreen.class));
        finish();
    }


}