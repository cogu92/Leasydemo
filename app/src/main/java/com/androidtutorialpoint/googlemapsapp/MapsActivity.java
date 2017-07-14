package com.androidtutorialpoint.googlemapsapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,GoogleMap.OnMapClickListener,View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public   TextToSpeech mtext_Speech;
    public   String Selection="Vibration";
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        TextView txt_emergency = (TextView) findViewById(R.id.txt_emergency);
        txt_emergency.setTextSize(MainSettings.ivar1);

        TextView txt_Interestpoints = (TextView) findViewById(R.id.txt_Interestpoints);
        txt_Interestpoints.setTextSize(MainSettings.ivar1);

        ImageButton btn_call=(ImageButton)findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);

        ImageButton btn_points=(ImageButton)findViewById(R.id.btn_points);
        btn_points.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mtext_Speech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mtext_Speech.setLanguage(Locale.US);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.cancel();

        mtext_Speech.shutdown();

    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Toast.makeText(this, "Map clicked", Toast.LENGTH_SHORT).show();
        this.mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Title")
                .snippet("Some info to be displayed"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        try {

            if ( MenuActivity.ivar2=="activity")
            {


                LatLng sydney = new LatLng(41.3866248, 2.1693842);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Pool").icon(BitmapDescriptorFactory.fromResource(R.drawable.pool)));
                sydney = new LatLng(41.3871638, 2.1711614);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Gym").icon(BitmapDescriptorFactory.fromResource(R.drawable.gim)));
                sydney = new LatLng(41.3877394, 2.1694632);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Chees").icon(BitmapDescriptorFactory.fromResource(R.drawable.chees)));


            }
            else {
                InputStream is = getAssets().open("barcelona.xml");
                   DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(is);
                Element element = doc.getDocumentElement();
                element.normalize();
                NodeList nList = doc.getElementsByTagName("node");
                for (int i = 0; i < nList.getLength(); i++) {
                    Node node = nList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element2 = (Element) node;
                        LatLng sydney = new LatLng(Double.parseDouble(element2.getAttribute("lat")), Double.parseDouble(element2.getAttribute("lon")));
                        mMap.addMarker(new MarkerOptions().position(sydney).title(element2.getAttribute("id")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }
                }
            }

        } catch (Exception e) {e.printStackTrace();}


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent saveIntent = new Intent().setClass(this,MenuActivity.class);
        startActivity(saveIntent);
        finish();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {



        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        //stop location updates
         if (mGoogleApiClient != null) {
           LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
         }
        Context context = getApplicationContext();


        float[] results = new float[1];
        float max_value=0;
        String ide ="";
        LatLng sydney ;

        Location locationA = new Location("point A");
        Location locationB = new Location("point B");


        try {
            InputStream is = getAssets().open("barcelona.xml");
            // InputStream is = getAssets().open("cunit.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element=doc.getDocumentElement();
            element.normalize();
            NodeList nList = doc.getElementsByTagName("node");
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), Double.parseDouble(element2.getAttribute("lat")), Double.parseDouble(element2.getAttribute("lon")), results);

                    if(max_value >results[0] || max_value==0) {
                        max_value = results[0];
                        ide=element2.getAttribute("id");
                        locationA.setLatitude(location.getLatitude());
                        locationA.setLongitude(location.getLongitude());
                        locationB.setLatitude(Double.parseDouble(element2.getAttribute("lat")));
                        locationB.setLongitude(Double.parseDouble(element2.getAttribute("lon")));
                    }

                }
            }

        } catch (Exception e) {e.printStackTrace();}


        float distance = locationA.distanceTo(locationB);
        if ( Selection=="Vibration") {
            mtext_Speech.shutdown();
            if (distance > 15) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.cancel();
            }
            if (distance <= 15) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {3, 100, 1000};
                v.vibrate(pattern, 1);
                //            CharSequence text = ide+" " +distance;
                CharSequence text = "CROSSWALK NEAR" + distance;

                // SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                // biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                // Toast.makeText(context, biggerText, Toast.LENGTH_LONG).show();

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);

                toast.show();
            }
            if (distance <= 10) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {3, 100, 500};
                v.vibrate(pattern, 1);
                //            CharSequence text = ide+" " +distance;
                CharSequence text = "CROSSWALK NEAR";

                // SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                // biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                // Toast.makeText(context, biggerText, Toast.LENGTH_LONG).show();

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);

                toast.show();
            }
            if (distance <= 3) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {3, 100, 100};
                v.vibrate(pattern, 1);
                //            CharSequence text = ide+" " +distance;
                CharSequence text = "CROSSWALK NEAR";

                // SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                // biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                // Toast.makeText(context, biggerText, Toast.LENGTH_LONG).show();

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);

                toast.show();
            }


        }

        if ( Selection=="Voice") {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.cancel();
            if (distance <15)
            {

                        String toSpeak = "Corner in 15 meters";
                        mtext_Speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


            }
            if (distance <10)
            {

                        String toSpeak = "Corner in 10 meters";
                        mtext_Speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }
            if (distance <5)
            {

                        String toSpeak = "Corner in 5 meters";
                        mtext_Speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        finish();

            }

        }



    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {

                //Selection="Vibration";
               // Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
             //   v.vibrate(500);
            case R.id.btn_call:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:674870818"));

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    return;

                }
                startActivity(callIntent);
                break;
            case R.id.btn_points:


                try {
                    InputStream is = getAssets().open("points.xml");
                    //   InputStream is = getAssets().open("cunit.xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);
                    Element element=doc.getDocumentElement();
                    element.normalize();
                    NodeList nList = doc.getElementsByTagName("node");
                    for (int i=0; i<nList.getLength(); i++) {
                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element2 = (Element) node;
                            LatLng sydney = new LatLng(Double.parseDouble(element2.getAttribute("lat")),Double.parseDouble( element2.getAttribute("lon")));
                          // ad image .icon(BitmapDescriptorFactory.fromResource(R.drawable.agenda))
                            mMap.addMarker(new MarkerOptions().position(sydney).title(element2.getAttribute("v")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    }

                } catch (Exception e) {e.printStackTrace();}

//                mtext_Speech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
             //       @Override
            //        public void onInit(int status) {
           //             if(status != TextToSpeech.ERROR) {
           //                 mtext_Speech.setLanguage(Locale.US);
          //              }
          //          }
          //      });
          //      Selection="Voice";
          //      String toSpeak = Selection;
           //     Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
          //      mtext_Speech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                break;


        }
    }
}
