package com.example.akash.wikipedia.ui.search;

import com.example.akash.wikipedia.data.DataManager;
import com.example.akash.wikipedia.data.remote.model.Page;
import com.example.akash.wikipedia.data.remote.model.SearchResult;
import com.example.akash.wikipedia.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class SearchResultMvpPresenter extends BasePresenter<SearchResultMvpView> {

    @Inject
    public SearchResultMvpPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    void getSearchResult(final String searchWord) {
        getMvpView().hideKeyboard();

        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading("Please wait...");
            getCompositeDisposible().add(
                    getDataManager().getSearchResultsFromAPI(searchWord)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<SearchResult>() {
                                @Override
                                public void accept(SearchResult searchResult) throws Exception {
                                    updateDb(searchResult, searchWord);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    getMvpView().hideLoading();
                                    getMvpView().showToast("something went wrong..");
                                    updateRecyclerView(searchWord);
                                }
                            }));
        } else {
            updateRecyclerView(searchWord);
        }
    }

    private void updateDb(SearchResult model, final String searchWord) {
        getCompositeDisposible().add(
                getDataManager().saveSearchResult(model)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                updateRecyclerView(searchWord);
                                getMvpView().hideLoading();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                getMvpView().hideLoading();

                            }
                        }));
    }

    private void updateRecyclerView(final String searchWord) {
        getCompositeDisposible().add(getDataManager().getSearchResults()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<List<Page>, List<Page>>() {
                    @Override
                    public List<Page> apply(List<Page> pages) throws Exception {
                        List<Page> pageList = new ArrayList<>();
                        if (pages != null && pages.size() > 0
                                && pages.get(0).getTitle().toLowerCase().contains(searchWord.toLowerCase())) {
                            pageList = pages;
                        }

                        return pageList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Page>>() {
                    @Override
                    public void accept(List<Page> pages) throws Exception {
                        if (pages != null && pages.size() > 0)
                            getMvpView().updateRecyclerView(pages);
                        else {
                            getMvpView().showToast("error fetching data from db..");
                            getMvpView().showNoDataErrorMsg();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().showToast("error fetching data from db..");
                        getMvpView().showNoDataErrorMsg();
                    }
                }));
    }
}
