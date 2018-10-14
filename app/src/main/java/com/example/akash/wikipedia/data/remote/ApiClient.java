package com.example.akash.wikipedia.data.remote;

import com.example.akash.wikipedia.data.remote.model.SearchResult;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class ApiClient {

    private Retrofit mRetrofit;
    private ApiEndpoints mApiServiceEndPoints;


    public ApiClient() {
        setUpClient();
    }

    private static ApiClient mInstance;

    public static ApiClient getmInstance() {
        if (mInstance == null) {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    private void setUpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
        httpLogger.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLogger);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org//w/api.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();

        mApiServiceEndPoints = getApiService();
    }

    private ApiEndpoints getApiService() {
        if (mApiServiceEndPoints == null) {
            mApiServiceEndPoints = mRetrofit.create(ApiEndpoints.class);
        }
        return mApiServiceEndPoints;
    }


    private interface ApiEndpoints {
        @GET("?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10")
        Observable<SearchResult> getSearchResultsFromAPI(@Query("gpssearch") String searchWord);
    }

    Observable<SearchResult> getSearchResultsFromAPI(String searchWord) {
        return getApiService().getSearchResultsFromAPI(searchWord);
    }
}


