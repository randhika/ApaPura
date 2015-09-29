package md.fusionworks.apapura.ui.activity;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import md.fusionworks.apapura.R;
import md.fusionworks.apapura.helper.MapHelper;
import md.fusionworks.apapura.model.realm.Well;
import md.fusionworks.apapura.provider.WellProvider;
import md.fusionworks.apapura.ui.view.MapLegendView;
import md.fusionworks.apapura.util.Constants;
import md.fusionworks.apapura.util.Convertor;
import md.fusionworks.apapura.util.UIUtils;
import md.fusionworks.apapura.util.Utils;

public class MapActivity extends BaseNavigationDrawerActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener {

    @Bind(R.id.addWellFab)
    FloatingActionButton addWellFab;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.mapLegendView)
    MapLegendView mapLegendView;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private MapHelper mapHelper;
    private Location myLastLocation;
    private Map<Marker, md.fusionworks.apapura.model.Well> wellDetailsMap;

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
                startActivityForResult(intent, Constants.ACTIVITY_RESULT_ADD_WELL);
            }
        });

        buildGoogleApiClient();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setTitle(getString(R.string.module_title_map));
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

                mapHelper = MapHelper.newInstance(this, map);
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);

        wellDetailsMap = new HashMap<>();
        RealmResults<Well> wellRealms = WellProvider.newInstance(this).getAll();
        for (Well wellRealm : wellRealms) {

            int rating = Utils.calculateWaterRating(wellRealm.getAppearanceRating(), wellRealm.getTasteRating(), wellRealm.getSmellRating());
            Marker marker = mapHelper.createMarker(wellRealm.getLatitude(), wellRealm.getLongitude(), UIUtils.getMarkerColorByWaterRating(rating));
            wellDetailsMap.put(marker, Convertor.wellRealmObjectToSimple(wellRealm));
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

                case Constants.ACTIVITY_RESULT_ADD_WELL:

                    md.fusionworks.apapura.model.Well well = (md.fusionworks.apapura.model.Well) data.getSerializableExtra(Constants.EXTRA_PARAM_WELL);
                    int rating = Utils.calculateWaterRating(well.getAppearanceRating(), well.getTasteRating(), well.getSmellRating());
                    mapHelper.goToPosition(well.getLatitude(), well.getLongitude(), true, Constants.MY_LOCATION_CAMERA_POSITION_ZOOM);
                    Marker marker = mapHelper.createMarker(well.getLatitude(), well.getLongitude(), UIUtils.getMarkerColorByWaterRating(rating));
                    wellDetailsMap.put(marker, well);

                    coordinatorLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Snackbar.make(coordinatorLayout, getString(R.string.info_well_added), Snackbar.LENGTH_SHORT).show();
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

    @Override
    public void onConnected(Bundle connectionHint) {

        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (myLastLocation != null) {

            mapHelper.goToPosition(myLastLocation.getLatitude(), myLastLocation.getLongitude(), false, Constants.MY_LOCATION_CAMERA_POSITION_ZOOM);
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

        outState.putBoolean(Constants.KEY_MAP_LEGEND_VIEW_WAS_SHOWED, mapLegendView.isLegendShowed());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean mapLegendViewWasShowed = savedInstanceState.getBoolean(Constants.KEY_MAP_LEGEND_VIEW_WAS_SHOWED);
        if (mapLegendViewWasShowed)
            mapLegendView.showLegend();
        else
            mapLegendView.hideLegend();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        md.fusionworks.apapura.model.Well well = wellDetailsMap.get(marker);

        Intent wellDetailIntent = new Intent(this, WellDetailActivity.class);
        wellDetailIntent.putExtra(Constants.EXTRA_PARAM_WELL, well);
        startActivityForResult(wellDetailIntent, Constants.ACTIVITY_RESULT_WELL_DETAIL);
        return true;
    }
}
