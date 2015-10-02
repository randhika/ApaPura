package md.fusionworks.aquamea.ui.activity;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by ungvas on 9/23/15.
 */
public abstract class BaseLocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private Location myLastLocation;

    protected synchronized void buildGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public GoogleApiClient getGoogleApiClient() {

        return googleApiClient;
    }

    public void connectGoogleApiClient() {

        if (!googleApiClient.isConnected())
            googleApiClient.connect();
    }

    public void disconnectGoogleApiClient() {

        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();

        buildGoogleApiClient();
        connectGoogleApiClient();
    }

    @Override
    protected void onStop() {
        super.onStop();

        disconnectGoogleApiClient();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        onGetLastLocation(myLastLocation);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }


    @Override
    public void onConnectionSuspended(int cause) {

        googleApiClient.connect();
    }

    public abstract void onGetLastLocation(Location location);
}
