package md.fusionworks.aquamea.helper;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ungvas on 9/29/15.
 */
public class MapHelper {

    private Context context;
    private GoogleMap map;

    public MapHelper(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
    }

    public static MapHelper newInstance(Context context, GoogleMap map) {

        return new MapHelper(context, map);
    }

    public void goToPosition(Double latitude, Double longitude, boolean animateCamera) {

        float zoom = map.getCameraPosition().zoom;
        goToPosition(latitude, longitude, animateCamera, zoom);
    }

    public void goToPosition(Double latitude, Double longitude, boolean animateCamera, float zoom) {

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom).build();
        if (animateCamera)
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        else
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    public Marker createMarker(Double latitude, Double longitude, float hue) {

        LatLng latLng = new LatLng(latitude, longitude);
        return map.addMarker(new MarkerOptions()
                .position(latLng)
                        // .title("Title")
                        //  .snippet("Snippet")
                .icon(BitmapDescriptorFactory.defaultMarker(hue)));
    }
}
