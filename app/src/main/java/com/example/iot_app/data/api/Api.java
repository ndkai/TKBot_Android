package com.example.iot_app.data.api;

import com.google.gson.Gson;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api implements IApi{
    public static final String QUERY_WIDTH = "width";
    public static final String QUERY_HEIGHT = "height";
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private static final String BASE_URL = "http://116.102.225.29/book_place/api/";
//    private static final String BASE_URL = "http://171.235.33.206/book_place/api/";
    private IApi mIApi;
        private Retrofit mRetrofit;

    @Inject
    public Api() {
        mRetrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        mIApi = mRetrofit.create(IApi.class);
    }

}
