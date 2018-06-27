package ru.alpha.technoservtest.messages.network;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 26/06/2018.
 */
public class ApiFactory {

    private static final String API_ENDPOINT = "http://samples.openweathermap.org";

    private static OkHttpClient client;
    private static MessagesService messagesService;

    private ApiFactory() {
    }

    @NonNull
    public static MessagesService getMessagesService() {
        MessagesService service = messagesService;

        if (service == null) {
            synchronized (ApiFactory.class) {
                service = messagesService;
                if (service == null) {
                    service = messagesService = buildRetrofit().create(MessagesService.class);
                }
            }
        }
        return service;
    }

    @NonNull
    private static OkHttpClient getClient() {
        OkHttpClient client = ApiFactory.client;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = ApiFactory.client;
                if (client == null) {
                    client = ApiFactory.client = buildClient();
                }
            }
        }
        return client;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
