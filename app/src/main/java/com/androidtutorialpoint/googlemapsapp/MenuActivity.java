package com.androidtutorialpoint.googlemapsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.R.attr.id;

/**
 * Created by DELL on 6/4/2017.
 */

public class MenuActivity extends   Activity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int PICK_CONTACT =1 ;
    private static final int CONTACT_PICKER_RESULT =1 ;
    public static String ivar2="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_options);

        ImageButton btn_ok = (ImageButton) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        ImageButton btn_map_activity = (ImageButton) findViewById(R.id.btn_map_activity);
        btn_map_activity.setOnClickListener(this);

        ImageButton btn_contact = (ImageButton) findViewById(R.id.btn_contact);
        btn_contact.setOnClickListener(this);
        ImageButton btn_settings = (ImageButton) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);

        TextView txt_contact = (TextView) findViewById(R.id.txt_contact);
        txt_contact.setTextSize(MainSettings.ivar1);
        TextView txt_config = (TextView) findViewById(R.id.txt_config);
        txt_config.setTextSize(MainSettings.ivar1);
        TextView txt_map = (TextView) findViewById(R.id.txt_map);
        txt_map.setTextSize(MainSettings.ivar1);
        TextView txt_routes = (TextView) findViewById(R.id.txt_routes);
        txt_routes.setTextSize(MainSettings.ivar1);

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {

                        String name = "911" + c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+name));

                        if (ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                            return;

                        }
                        startActivity(callIntent);

                    }
                }
                break;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_map_activity:
                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }
                else{

                    ivar2="navigate";
                    Intent mainIntent = new Intent().setClass(this, MapsActivity.class);
                    startActivity(mainIntent);
                    finish();

                }


                break;
            case R.id.btn_settings:
                                 Intent mainIntent = new Intent().setClass(this, MainSettings.class);
                    startActivity(mainIntent);
                    finish();
                                 break;

            case R.id.btn_contact:

                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(intent, PICK_CONTACT);

                break;

            case R.id.btn_ok:



                final LocationManager managers = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if ( !managers.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }
                else{

                    ivar2="activity";
                     mainIntent = new Intent().setClass(this,MainSaveRoutes.class);
//                    mainIntent = new Intent().setClass(this, MapsActivity.class);

                    startActivity(mainIntent);
                    finish();

                }


               break;


        }
    }

}
