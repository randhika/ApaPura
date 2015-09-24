package md.fusionworks.aquamea.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.model.Well;
import md.fusionworks.aquamea.provider.WellProvider;
import md.fusionworks.aquamea.ui.view.MapLegendView;
import md.fusionworks.aquamea.util.CommonConstants;
import md.fusionworks.aquamea.util.UIUtils;
import md.fusionworks.aquamea.util.Utils;

public class MapActivity extends BaseNavigationDrawerActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int ACTIVITY_RESULT_ADD_WELL = 0;
    public static final int CAMERA_POSITION_ZOOM = 15;

    private static final String KEY_MAP_LEGEND_VIEW_WAS_SHOWED = "KEY_MAP_LEGEND_VIEW_VISIBILITY";

    @Bind(R.id.addWellFab)
    FloatingActionButton addWellFab;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.mapLegendView)
    MapLegendView mapLegendView;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location myLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);
        setUpMapIfNeeded();

        addWellFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity.this, AddWellActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD_WELL);
            }
        });

        buildGoogleApiClient();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setTitle("Map");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // drawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
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

        RealmResults<Well> wells = WellProvider.newInstance(this).getAll();
        for (Well well : wells) {

            int rating = Utils.calculateWaterRating(well.getAppearanceRating(), well.getTasteRating(), well.getSmellRating());
            createMarker(well.getLatitude(), well.getLongitude(), UIUtils.getMarkerColorByWaterRating(rating));
        }
    }

    @Override
    protected int getSelfDrawerItem() {

        return DRAWER_ITEM_MAP;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case ACTIVITY_RESULT_ADD_WELL:

                    Well well = (Well) data.getSerializableExtra(CommonConstants.EXTRA_PARAM_WELL);
                    int rating = Utils.calculateWaterRating(well.getAppearanceRating(), well.getTasteRating(), well.getSmellRating());
                    goToPosition(well.getLatitude(), well.getLongitude());
                    createMarker(well.getLatitude(), well.getLongitude(), UIUtils.getMarkerColorByWaterRating(rating));

                    coordinatorLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Snackbar.make(coordinatorLayout, "Well added", Snackbar.LENGTH_SHORT).show();
                        }
                    }, 500);
                    break;

            }
        }
    }

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (googleApiClient.isConnected()) {

            googleApiClient.disconnect();
        }
    }

    private void goToPosition(Double latitude, Double longitude) {

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(CAMERA_POSITION_ZOOM).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    private void createMarker(Double latitude, Double longitude, float hue) {

        LatLng latLng = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(hue)));
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (myLastLocation != null) {

            goToPosition(myLastLocation.getLatitude(), myLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }


    @Override
    public void onConnectionSuspended(int cause) {

        googleApiClient.connect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(KEY_MAP_LEGEND_VIEW_WAS_SHOWED, mapLegendView.isLegendShowed());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean mapLegendViewWasShowed = savedInstanceState.getBoolean(KEY_MAP_LEGEND_VIEW_WAS_SHOWED);
        if (mapLegendViewWasShowed)
            mapLegendView.showLegend();
        else
            mapLegendView.hideLegend();
    }
}
