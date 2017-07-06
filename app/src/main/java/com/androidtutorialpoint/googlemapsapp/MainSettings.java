package com.androidtutorialpoint.googlemapsapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by DELL on 6/8/2017.
 */

public class MainSettings extends Activity   implements View.OnClickListener  {

    public static int ivar1=25;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings);

    ImageButton btn_big_font = (ImageButton) findViewById(R.id.btn_big_font);
        btn_big_font.setOnClickListener(this);
        ImageButton btn_font = (ImageButton) findViewById(R.id.btn_font);
        btn_font.setOnClickListener(this);

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
    public void onBackPressed() {
        super.onBackPressed();
        Intent saveIntent = new Intent().setClass(this,MenuActivity.class);
        startActivity(saveIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        TextView txt_font = (TextView) findViewById(R.id.txt_font);
        TextView txt_settings = (TextView) findViewById(R.id.txt_settings);
        TextView txv_emergency = (TextView) findViewById(R.id.txv_emergency);
        EditText  editText2 = (EditText) findViewById(R.id.editText2);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        switch (view.getId()) {
            case R.id.btn_big_font:
                editor.putString("50", "username");
                editor.commit();
                txt_font.setTextSize(35);
                txt_settings.setTextSize(35);
                txv_emergency.setTextSize(35);
                ivar1 = 35;

                 break;

            case R.id.btn_font:


                editor.putString("25","username");
                editor.commit();
                txt_font.setTextSize(25);
                txt_settings.setTextSize(25);
                txv_emergency.setTextSize(25);
                ivar1=25;
                break;


        }
    }

}
