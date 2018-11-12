package the.easy.cab.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;

import the.easy.cab.R;
import the.easy.cab.app.BaseActivity;

public class MapViewActivity extends BaseActivity implements
        OnMapReadyCallback, LocationEngineListener, PermissionsListener {


    private MapView mapView;
    private MapboxMap mapboxMap;
    private Location originLayout;
    private LocationEngine locationEngine;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationLayerPlugin;

    int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MapViewActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;

        locationEnable();
        mapboxMap.getUiSettings().setAllGesturesEnabled(true);
        mapboxMap.getUiSettings().setZoomControlsEnabled(true);
        mapboxMap.getUiSettings().setZoomGesturesEnabled(true);
        mapboxMap.getUiSettings().setScrollGesturesEnabled(true);

    }



    void locationEnable() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            intialLocationEngine();
            intializLocationLayer();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    void intialLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLayout = lastLocation;
            setCamerpostion(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }

    }

    void intializLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    void setCamerpostion(Location camerpostion) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(camerpostion.getLatitude(), camerpostion.getLongitude()), 13.0));
    }


    @Override
    public void onConnected() {

        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            return;
        }
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            locationEnable();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLayout = location;
            setCamerpostion(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
