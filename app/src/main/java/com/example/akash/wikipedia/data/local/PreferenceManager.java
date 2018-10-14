package com.example.akash.wikipedia.data.local;

import com.example.akash.wikipedia.data.remote.model.Page;
import com.example.akash.wikipedia.data.remote.model.SearchResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Akash Verma on 13/10/18.
 */
public interface PreferenceManager {

    void setWelcomeMessage(String msg);

    String getWelcomeMessage();

    Observable<List<Page>> getSearchResults();

    Observable<Boolean> saveSearchResult(SearchResult searchResult);
}


