package com.example.akash.wikipedia.data.remote;

import android.content.Context;

import com.example.akash.wikipedia.data.remote.model.SearchResult;
import com.example.akash.wikipedia.injection.qualifier.ApplicationContext;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class AppApiManager implements ApiManager {

    private Context context;

    @Inject
    public AppApiManager(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Observable<SearchResult> getSearchResultsFromAPI(String searchWord) {
        return ApiClient.getmInstance().getSearchResultsFromAPI(searchWord);
    }
}