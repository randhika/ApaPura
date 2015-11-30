package md.fusionworks.aquamea.repository;

import android.content.Context;
import android.text.TextUtils;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import md.fusionworks.aquamea.api.AquaMeaClient;
import md.fusionworks.aquamea.api.Callback;
import md.fusionworks.aquamea.api.ServiceCreator;
import md.fusionworks.aquamea.model.Well;
import md.fusionworks.aquamea.provider.WellProvider;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ungvas on 11/26/15.
 */
public class AquameaRepository {

    private AquaMeaClient aquaMeaClient;

    public AquameaRepository() {

        aquaMeaClient = ServiceCreator.createService(AquaMeaClient.class, AquaMeaClient.BASE_URL);
    }

    public void getWells(Callback<List<Well>> callback) {

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

    public void syncWell(Context context, Well well) {

        RequestBody requestBody = null;
        if (!TextUtils.isEmpty(well.getLocalPhoto())) {

            File file = new File(well.getLocalPhoto());

            requestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        RequestBody appearance = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getAppearanceRating()));
        RequestBody taste = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getTasteRating()));
        RequestBody smell = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getSmellRating()));
        RequestBody note = RequestBody.create(MediaType.parse("text/plain"), well.getNote());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getLatitude()));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getLongitude()));

        Call<JSONObject> call = aquaMeaClient.uploadWell(
                "g64SD6Gr8g4s.e54HHJChDS864dfgd"
                , appearance
                , taste
                , smell
                , note
                , latitude
                , longitude
                , requestBody
        );

        call.enqueue(new retrofit.Callback<JSONObject>() {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

                md.fusionworks.aquamea.model.realm.Well wellRealm = new md.fusionworks.aquamea.model.realm.Well();
                wellRealm.setLocalPhoto(well.getLocalPhoto());
                wellRealm.setAppearanceRating(well.getAppearanceRating());
                wellRealm.setSmellRating(well.getSmellRating());
                wellRealm.setTasteRating(well.getTasteRating());
                wellRealm.setNote(well.getNote());
                wellRealm.setLatitude(well.getLatitude());
                wellRealm.setLongitude(well.getLongitude());
                wellRealm.setSync(false);

                Realm realm = Realm.getInstance(context);
                realm.executeTransaction(realm1 -> realm1.copyToRealm(wellRealm));
            }
        });
    }


    public void syncWell(Context context, md.fusionworks.aquamea.model.realm.Well well) {

        RequestBody requestBody = null;
        if (!TextUtils.isEmpty(well.getLocalPhoto())) {

            File file = new File(well.getLocalPhoto());

            requestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        RequestBody appearance = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getAppearanceRating()));
        RequestBody taste = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getTasteRating()));
        RequestBody smell = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getSmellRating()));
        RequestBody note = RequestBody.create(MediaType.parse("text/plain"), well.getNote());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getLatitude()));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(well.getLongitude()));

        Call<JSONObject> call = aquaMeaClient.uploadWell(
                "g64SD6Gr8g4s.e54HHJChDS864dfgd"
                , appearance
                , taste
                , smell
                , note
                , latitude
                , longitude
                , requestBody
        );

        call.enqueue(new retrofit.Callback<JSONObject>() {
            @Override
            public void onResponse(Response<JSONObject> response, Retrofit retrofit) {

                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();
                well.removeFromRealm();
                realm.commitTransaction();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void syncWells(Context context) {

        RealmResults<md.fusionworks.aquamea.model.realm.Well> wellRealms = WellProvider.newInstance(context).getNotSyncWells();
        for (md.fusionworks.aquamea.model.realm.Well wellRealm : wellRealms) {

            syncWell(context, wellRealm);
        }
    }
}
