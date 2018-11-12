package the.easy.cab.app;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

import the.easy.cab.R;

public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Mapbox Access token
        String mapbox_access_token = getString(R.string.mapbox_access_token);
        Mapbox.getInstance(getApplicationContext(), mapbox_access_token);
    }
}
