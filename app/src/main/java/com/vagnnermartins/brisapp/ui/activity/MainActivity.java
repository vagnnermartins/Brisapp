package com.vagnnermartins.brisapp.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.vagnnermartins.brisapp.R;
import com.vagnnermartins.brisapp.app.App;
import com.vagnnermartins.brisapp.enums.StatusEnum;
import com.vagnnermartins.brisapp.ui.helper.MainUIHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class MainActivity extends ActionBarActivity {

    private App app;
    private MainUIHelper ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadValues();
    }

    private void loadValues() {
        if(app.message == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            ui.message.setText(app.message);
        }
    }

    private void init() {
        app = (App) getApplication();
        ui = new MainUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar(ui.toolbar);
    }

    private void checkStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            if(app.isInternetConnection(this)){
                statusInicio();
            }
        }else if(status == StatusEnum.EXECUTANDO){
            ui.progress.setVisibility(View.VISIBLE);
        }else if(status == StatusEnum.EXECUTADO){
            ui.progress.setVisibility(View.GONE);
        }
    }

    private void statusInicio() {
        Ion.with(this)
                .load("http://www.lerolero.com")
                .asString()
                .setCallback(onLoadMessage());
        checkStatus(StatusEnum.EXECUTANDO);
    }

    private FutureCallback<String> onLoadMessage() {
        return new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if(e == null){
                    app.message = Jsoup.parse(result).body().select("blockquote").text();
                    if(app.message.equals("")){
                        app.message = "Estamos sem criatividade, Volte mais tarde :(";
                    }
                    ui.message.setText(app.message);
                    checkStatus(StatusEnum.EXECUTADO);
                }else{
                    checkStatus(StatusEnum.INICIO);
                }
            }
        };
    }

    public void share(String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.abc_shareactionprovider_share_with)));
    }

    private void copy(String message) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Frase Brisapp", message);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_update:
                checkStatus(StatusEnum.INICIO);
                break;
            case R.id.menu_share:
                share(app.message);
                break;
            case R.id.menu_copy:
                copy(app.message);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
