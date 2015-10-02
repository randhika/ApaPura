package md.fusionworks.aquamea.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import md.fusionworks.aquamea.R;
import md.fusionworks.aquamea.helper.MapHelper;
import md.fusionworks.aquamea.model.realm.Well;
import md.fusionworks.aquamea.provider.WellProvider;
import md.fusionworks.aquamea.ui.view.EmptyImageView;
import md.fusionworks.aquamea.ui.view.MapLegendView;
import md.fusionworks.aquamea.ui.view.MapTypeSatelliteSwitcher;
import md.fusionworks.aquamea.util.BitmapUtils;
import md.fusionworks.aquamea.util.Constants;
import md.fusionworks.aquamea.util.Convertor;
import md.fusionworks.aquamea.util.UIUtils;
import md.fusionworks.aquamea.util.Utils;

public class MapActivity extends BaseNavigationDrawerActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    @Bind(R.id.addWellFab)
    FloatingActionButton addWellFab;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.mapLegendView)
    MapLegendView mapLegendView;
    @Bind(R.id.mapTypeSatelliteSwitcher)
    MapTypeSatelliteSwitcher mapTypeSatelliteSwitcher;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private MapHelper mapHelper;
    private Location myLastLocation;
    private Map<Marker, md.fusionworks.aquamea.model.Well> wellDetailsMap;
    private boolean isActivityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);
        setUpMapIfNeeded();
        mapHelper = MapHelper.newInstance(this, map);
        isActivityResult = false;

        addWellFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity.this, AddWellActivity.class);
                startActivityForResult(intent, Constants.ACTIVITY_RESULT_ADD_WELL);
            }
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

        buildGoogleApiClient();
        pinWellsOnMap();
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

    private void  setUpMapIfNeeded() {

        if (map == null) {

            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (map != null) {

                setUpMap();
            }
        }
    }

    private void setUpMap() {

        map.setMyLocationEnabled(true);
        map.setInfoWindowAdapter(new WellDetailInfoWindowAdapter(this));
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
    }

    private void pinWellsOnMap() {

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

        isActivityResult = true;

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case Constants.ACTIVITY_RESULT_ADD_WELL:

                    md.fusionworks.aquamea.model.Well well = (md.fusionworks.aquamea.model.Well) data.getSerializableExtra(Constants.EXTRA_PARAM_WELL);
                    int rating = Utils.calculateWaterRating(well.getAppearanceRating(), well.getTasteRating(), well.getSmellRating());
                    mapHelper.goToPosition(well.getLatitude(), well.getLongitude(), true, Constants.MY_LOCATION_CAMERA_POSITION_ZOOM);
                    Marker marker = mapHelper.createMarker(well.getLatitude(), well.getLongitude(), UIUtils.getMarkerColorByWaterRating(rating));
                    wellDetailsMap.put(marker, well);
                    marker.showInfoWindow();

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

        if (!isActivityResult) {
            myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (myLastLocation != null) {

                mapHelper.goToPosition(myLastLocation.getLatitude(), myLastLocation.getLongitude(), false, Constants.MY_LOCATION_CAMERA_POSITION_ZOOM);
            }
        } else {

            isActivityResult = false;
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
        outState.putBoolean(Constants.KEY_MAP_TYPE_SATELLITE_SWITCHER_WAS_CHECKED, mapTypeSatelliteSwitcher.isChecked());

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

        boolean mapTypeSatelliteSwithcerWasChecked = savedInstanceState.getBoolean(Constants.KEY_MAP_TYPE_SATELLITE_SWITCHER_WAS_CHECKED);
        mapTypeSatelliteSwitcher.setChecked(mapTypeSatelliteSwithcerWasChecked);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        mapHelper.goToPosition(marker.getPosition().latitude, marker.getPosition().longitude, true);
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        md.fusionworks.aquamea.model.Well well = wellDetailsMap.get(marker);

        Intent wellDetailIntent = new Intent(this, WellDetailActivity.class);
        wellDetailIntent.putExtra(Constants.EXTRA_PARAM_WELL, well);
        startActivityForResult(wellDetailIntent, Constants.ACTIVITY_RESULT_WELL_DETAIL);
    }

    public class WellDetailInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private Context context;
        private View view;

        public WellDetailInfoWindowAdapter(Context context) {

            this.context = context;
            view = getLayoutInflater().inflate(R.layout.well_detail_info_window_layout, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView totalRatingField = (TextView) view.findViewById(R.id.totalRatingField);
            TextView appearanceRatingField = (TextView) view.findViewById(R.id.appearanceRatingField);
            TextView tasteRatingField = (TextView) view.findViewById(R.id.tasteRatingField);
            TextView smellRatingField = (TextView) view.findViewById(R.id.smellRatingField);
            EmptyImageView emptyImageView = (EmptyImageView) view.findViewById(R.id.emptyImageView);

            md.fusionworks.aquamea.model.Well well = wellDetailsMap.get(marker);
            int totalRating = Utils.calculateWaterRating(well.getAppearanceRating(), well.getTasteRating(), well.getSmellRating());

            totalRatingField.setText(String.format("%s %d", getString(R.string.field_total_rating_), totalRating));
            appearanceRatingField.setText(String.format("%s %d", getString(R.string.field_appearance_), well.getAppearanceRating()));
            tasteRatingField.setText(String.format("%s %d", getString(R.string.field_taste_), well.getTasteRating()));
            smellRatingField.setText(String.format("%s %d", getString(R.string.field_smell_), well.getSmellRating()));

            String photoPath = well.getPhotoPath();
            if (!TextUtils.isEmpty(photoPath)) {

                emptyImageView.setImageBitmap(BitmapUtils.scaleToActualAspectRatio(emptyImageView, photoPath));
                emptyImageView.setTag(photoPath);
            } else
                emptyImageView.removeImage();

            return view;
        }
    }
}
