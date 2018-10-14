package com.example.akash.wikipedia.data.remote;

import com.example.akash.wikipedia.data.remote.model.SearchResult;

import io.reactivex.Observable;

/**
 * Created by Akash Verma on 13/10/18.
 */
public interface ApiManager {

    Observable<SearchResult> getSearchResultsFromAPI(String searchWord);

}
