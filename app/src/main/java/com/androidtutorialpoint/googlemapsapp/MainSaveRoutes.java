package com.androidtutorialpoint.googlemapsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by DELL on 6/9/2017.
 */

public class MainSaveRoutes extends Activity implements View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_routes);
        TextView txt_name = (TextView) findViewById(R.id.txt_name);
        txt_name.setTextSize(MainSettings.ivar1);
        TextView txt_addres = (TextView) findViewById(R.id.txt_addres);
        txt_addres.setTextSize(MainSettings.ivar1);
        TextView txt_placeone = (TextView) findViewById(R.id.txt_placeone);
        txt_placeone.setTextSize(MainSettings.ivar1);
        TextView txt_placetwo = (TextView) findViewById(R.id.txt_placetwo);
        txt_placetwo.setTextSize(MainSettings.ivar1);
        TextView txt_route = (TextView) findViewById(R.id.txt_route);
        txt_route.setTextSize(MainSettings.ivar1);
        Button btnsave = (Button) findViewById(R.id.btn_save);
        btnsave.setTextSize(MainSettings.ivar1);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent saveIntent = new Intent().setClass(this,MenuActivity.class);
        startActivity(saveIntent);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

    }

}