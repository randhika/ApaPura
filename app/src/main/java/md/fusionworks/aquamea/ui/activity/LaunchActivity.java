package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import md.fusionworks.aquamea.util.Constants;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intentToLaunch = new Intent(this, MapActivity.class);
        intentToLaunch.putExtra(Constants.EXTRA_PARAM_FROM_LAUNCH_SCREEN, true);
        startActivity(intentToLaunch);
    }
}
