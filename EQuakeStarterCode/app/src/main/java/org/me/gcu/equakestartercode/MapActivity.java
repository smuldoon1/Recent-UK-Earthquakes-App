package org.me.gcu.equakestartercode;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends EarthquakeActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private GoogleMap map;

    private String[] markerIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);

        markerIDs = new String[MainActivity.earthquakes.length];

        for (int i = 0; i < MainActivity.earthquakes.length; i++) {
            LatLng latLng = new LatLng(MainActivity.earthquakes[i].getLatitude(), MainActivity.earthquakes[i].getLongitude());
            if (i == 0)
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            Marker marker = map.addMarker(new MarkerOptions().position(latLng));
            markerIDs[i] = marker.getId();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        for (int i = 0; i < MainActivity.earthquakes.length; i++) {
            if (marker.getId().equals(markerIDs[i])) {
                Intent intent = new Intent(this, DetailedEarthquakeActivity.class);
                intent.putExtra("earthquakeIndex", i);
                if (intent != null)
                    startActivity(intent);
            }
        }
        return false;
    }
}