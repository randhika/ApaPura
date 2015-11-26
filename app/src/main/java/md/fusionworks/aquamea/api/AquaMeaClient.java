package md.fusionworks.aquamea.api;

import com.squareup.okhttp.RequestBody;

import java.util.List;

import md.fusionworks.aquamea.model.Well;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by ungvas on 11/26/15.
 */
public interface AquaMeaClient {

    String BASE_URL = "http://192.168.88.21/en/api/";

    String MARKERS = "getMarkers";
    String ADD_MARKER = "addMarker";

    @GET(MARKERS)
    Call<List<Well>> getRates(@Query("token") String token);

    @FormUrlEncoded
    @POST(ADD_MARKER)
    Call<String> uploadWell(@Query("token") String token, @Field("appearance") int appearance, @Field("taste") int taste, @Field("smell") int smell, @Field("note") String note, @Field("latitude") double latitude, @Field("longitude") double longitude);
}
