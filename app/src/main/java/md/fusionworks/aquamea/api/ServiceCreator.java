package md.fusionworks.aquamea.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ungvas on 10/28/15.
 */
public class ServiceCreator {

    private static OkHttpClient httpClient = new OkHttpClient();

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        httpClient.interceptors().add(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .build();

            return chain.proceed(request);
        });

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }
}
