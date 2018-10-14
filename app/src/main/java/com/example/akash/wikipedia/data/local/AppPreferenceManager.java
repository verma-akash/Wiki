package com.example.akash.wikipedia.data.local;

import com.example.akash.wikipedia.data.remote.model.Page;
import com.example.akash.wikipedia.data.remote.model.SearchResult;
import com.example.akash.wikipedia.utils.AppConstants;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class AppPreferenceManager implements PreferenceManager {

    private PreferenceHelper preferenceHelper;

    @Inject
    public AppPreferenceManager(PreferenceHelper preferenceHelper) {
        this.preferenceHelper = preferenceHelper;
    }

    @Override
    public void setWelcomeMessage(String msg) {
        preferenceHelper.putString("welcome_msg", msg);
    }

    @Override
    public String getWelcomeMessage() {
        return preferenceHelper.getString("welcome_msg", "");
    }

    @Override
    public Observable<List<Page>> getSearchResults() {
        return Observable.fromCallable(new Callable<List<Page>>() {
            @Override
            public List<Page> call() throws Exception {
                Gson gson = new Gson();
                String schemeMap = preferenceHelper.getString(AppConstants.SEARCH_RESULT, "");
                SearchResult searchResult = gson.fromJson(schemeMap, SearchResult.class);
                return searchResult.getQuery().getPages();
            }
        });
    }

    @Override
    public Observable<Boolean> saveSearchResult(final SearchResult searchResult) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Gson gson = new Gson();
                String json = gson.toJson(searchResult);
                preferenceHelper.putString(AppConstants.SEARCH_RESULT, json);
                return true;
            }
        });
    }
}
