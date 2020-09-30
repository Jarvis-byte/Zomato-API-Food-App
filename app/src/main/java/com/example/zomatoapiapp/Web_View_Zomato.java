package com.example.zomatoapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Web_View_Zomato extends AppCompatActivity {
WebView web;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__view__zomato);
        Bundle extras = getIntent().getExtras();
        String url=extras.getString("url");
        Log.i("URL",url);
        web=findViewById(R.id.Web_View);
     web.setWebViewClient(new WebViewClient());
        web.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog = ProgressDialog.show(Web_View_Zomato.this, null,
                        "Please Wait...Page is Loading...");
               // dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }
        });
     web.loadUrl(url);
     WebSettings webSettings=web.getSettings();
     webSettings.setJavaScriptEnabled(true);
}

    @Override
    public void onBackPressed() {
        if (web.canGoBack())
        {
            web.goBack();
        }
        else
        {
           Intent back=new Intent(Web_View_Zomato.this,MainActivity.class);
           startActivity(back);
        }

    }
}