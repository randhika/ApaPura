package md.fusionworks.aquamea.repository;

import android.text.TextUtils;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.List;

import md.fusionworks.aquamea.api.AquaMeaClient;
import md.fusionworks.aquamea.api.Callback;
import md.fusionworks.aquamea.api.ServiceCreator;
import md.fusionworks.aquamea.model.Well;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ungvas on 11/26/15.
 */
public class AquameaRepository {

    private AquaMeaClient aquaMeaClient;

    public AquameaRepository() {

        aquaMeaClient = ServiceCreator.createService(AquaMeaClient.class, AquaMeaClient.BASE_URL, "g64SD6Gr8g4s.e54HHJChDS864dfgd");
    }

    public void getMarkers(Callback<List<Well>> callback) {

        Call<List<Well>> call = aquaMeaClient.getRates("g64SD6Gr8g4s.e54HHJChDS864dfgd");
        call.enqueue(new retrofit.Callback<List<Well>>() {
            @Override
            public void onResponse(Response<List<Well>> response, Retrofit retrofit) {
                if (response.isSuccess()) {

                    callback.onSuccess(response.body());
                } else {

                    callback.onError(new Exception());
                }
            }

            @Override
            public void onFailure(Throwable t) {

                callback.onError(t);
            }
        });
    }

    public void uploadWell(Well well) {

        RequestBody requestBody = null;
        if (!TextUtils.isEmpty(well.getLocalPhoto())) {

            File file = new File(well.getLocalPhoto());

            requestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }

        Call<String> call = aquaMeaClient.uploadWell(
                "g64SD6Gr8g4s.e54HHJChDS864dfgd"
                , well.getAppearanceRating()
                , well.getTasteRating()
                , well.getSmellRating()
                , well.getNote()
                , well.getLatitude()
                , well.getLongitude()
        );

        call.enqueue(new retrofit.Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
