package com.example.googlemapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button showmap, currentloc, loclistener;
    EditText loc;

    LocationManager locationManager;
    String lat, lng;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lat = lng = "";
        showmap = findViewById(R.id.shwmap);
        currentloc = findViewById(R.id.crntloc);
        loclistener = findViewById(R.id.loclist);
        loc = findViewById(R.id.location);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(new Criteria(), false);
        String provider = "";

        for (String pro : providers) {
            provider += pro + "\n";
        }
        Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_LONG).show();

        currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!providerEnabled) {
                    EnableGPS();
                } else
                    GetLocation();
            }
        });


    }

    private void GetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perm = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(this,perm,1);
        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if (!location.equals(null)){
                        Toast.makeText(MainActivity.this,"Latitude :  " +location.getLatitude()+"\n Longitude :  "+location.getLongitude(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perm = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(this,perm,1);
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (!location.equals(null)){
                lat = location.getLatitude()+"";
                lng = location.getLongitude()+"";
                loc.setText("Latitude :" + lat +"\n Longitude :" + lng);
            }
        }

    }

    private void EnableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
