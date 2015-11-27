package md.fusionworks.aquamea.api;

import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.List;

import md.fusionworks.aquamea.model.Well;
import md.fusionworks.aquamea.util.Constants;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by ungvas on 11/26/15.
 */
public interface AquaMeaClient {

    String BASE_URL = Constants.SERVER_URL + "/en/api/";

    String MARKERS = "getMarkers";
    String ADD_MARKER = "addMarker";

    @GET(MARKERS)
    Call<List<Well>> getRates(@Query("token") String token);

    @Multipart
    @POST(ADD_MARKER)
    Call<JSONObject> uploadWell(@Query("token") String token, @Part("appearance") RequestBody appearance, @Part("taste") RequestBody taste, @Part("smell") RequestBody smell, @Part("note") RequestBody note, @Part("latitude") RequestBody latitude, @Part("longitude") RequestBody longitude, @Part("photo\"; filename=\"image.png\" ") RequestBody photo);
}
