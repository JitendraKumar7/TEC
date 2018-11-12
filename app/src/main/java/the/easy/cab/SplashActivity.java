package the.easy.cab;

import android.os.Bundle;

import the.easy.cab.app.BaseActivity;
import the.easy.cab.ui.MapViewActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MapViewActivity.startActivity(activity);
    }
}
