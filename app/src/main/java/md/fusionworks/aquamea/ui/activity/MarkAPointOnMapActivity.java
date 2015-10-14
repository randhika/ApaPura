package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.helper.MapHelper;
import md.fusionworks.aquamea.ui.widget.MapTypeSatelliteSwitcher;
import md.fusionworks.aquamea.util.Constants;
import md.fusionworks.aquamea.util.DialogUtils;

public class MarkAPointOnMapActivity extends BaseLocationActivity implements GoogleMap.OnMapClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.latitudeField)
    TextView latitudeField;
    @Bind(R.id.longitudeField)
    TextView longitudeField;
    @Bind(R.id.mapTypeSatelliteSwitcher)
    MapTypeSatelliteSwitcher mapTypeSatelliteSwitcher;

    private GoogleMap map;
    private MapHelper mapHelper;
    private String latitudeExtraString;
    private String longitudeExtraString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_apoint_on_map);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            if (bundle.containsKey(Constants.EXTRA_PARAM_LATITUDE)) {

                latitudeExtraString = bundle.getString(Constants.EXTRA_PARAM_LATITUDE);
                longitudeExtraString = bundle.getString(Constants.EXTRA_PARAM_LONGITUDE);
            }

        ButterKnife.bind(this);
        setUpMapIfNeeded();
        mapHelper = MapHelper.newInstance(this, map);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.module_title_mark_a_point_on_map);
        toolbar.setNavigationOnClickListener(v -> {

            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

        mapTypeSatelliteSwitcher.setOnCheckedChangeListener(new MapTypeSatelliteSwitcher.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {

                if (isChecked)
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });


        map.setOnMapClickListener(this);
    }

    private void setUpMapIfNeeded() {

        if (map == null) {

            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {

                setUpMap();
            }
        }
    }

    private void setUpMap() {

        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private boolean validateInput() {

        boolean isValid = true;

        if (TextUtils.isEmpty(latitudeField.getText().toString()) || TextUtils.isEmpty(longitudeField.getText().toString())) {

            isValid = false;
            DialogUtils.showAlertDialog(this, getString(R.string.dialog_title_validation_error), getString(R.string.validation_error_message_gps_coordinates_required));
        }

        return isValid;
    }

    private void saveGPSCoordinates() {

        if (validateInput()) {

            String latitude = latitudeField.getText().toString();
            String longitude = longitudeField.getText().toString();

            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_PARAM_LATITUDE, latitude);
            intent.putExtra(Constants.EXTRA_PARAM_LONGITUDE, longitude);

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean hasExtraLatLng() {

        return !TextUtils.isEmpty(latitudeExtraString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mark_apoint_on_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.action_save_gps_coordinates) {

            saveGPSCoordinates();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetLastLocation(Location location) {

        double latitude = 0;
        double longitude = 0;

        if (hasExtraLatLng()) {

            latitude = Double.valueOf(latitudeExtraString);
            longitude = Double.valueOf(longitudeExtraString);
        } else if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        map.clear();
        latitudeField.setText(String.valueOf(latitude));
        longitudeField.setText(String.valueOf(longitude));
        mapHelper.goToPosition(latitude, longitude, false, Constants.MY_LOCATION_CAMERA_POSITION_ZOOM);
        mapHelper.createMarker(latitude, longitude, BitmapDescriptorFactory.HUE_GREEN);

        disconnectGoogleApiClient();
    }

    @Override
    public void onMapClick(LatLng latLng) {

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        map.clear();
        latitudeField.setText(String.valueOf(latitude));
        longitudeField.setText(String.valueOf(longitude));
        mapHelper.goToPosition(latitude, longitude, true);
        mapHelper.createMarker(latitude, longitude, BitmapDescriptorFactory.HUE_GREEN);
    }
}
