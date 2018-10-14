package com.example.akash.wikipedia.data;

import com.example.akash.wikipedia.data.local.PreferenceManager;
import com.example.akash.wikipedia.data.remote.ApiManager;
import com.example.akash.wikipedia.data.remote.model.Page;
import com.example.akash.wikipedia.data.remote.model.SearchResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class AppDataManager implements DataManager {

    private PreferenceManager preferenceManager;
    private ApiManager apiManager;

    @Inject
    public AppDataManager(PreferenceManager preferenceManager, ApiManager apiManager) {
        this.preferenceManager = preferenceManager;
        this.apiManager = apiManager;
    }

    @Override
    public Observable<List<Page>> getSearchResults() {
        return preferenceManager.getSearchResults();
    }

    @Override
    public Observable<Boolean> saveSearchResult(SearchResult searchResult) {
        return preferenceManager.saveSearchResult(searchResult);
    }

    @Override
    public void setWelcomeMessage(String msg) {
        preferenceManager.setWelcomeMessage(msg);
    }

    @Override
    public String getWelcomeMessage() {
        return preferenceManager.getWelcomeMessage();
    }

    @Override
    public Observable<SearchResult> getSearchResultsFromAPI(String searchWord) {
        return apiManager.getSearchResultsFromAPI(searchWord);
    }
}
