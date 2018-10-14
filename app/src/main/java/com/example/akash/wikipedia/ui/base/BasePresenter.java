package com.example.akash.wikipedia.ui.base;

import com.example.akash.wikipedia.data.DataManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private DataManager dataManager;
    private CompositeDisposable compositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
        mMvpView = null;
    }

    @Override
    public void handleApiError() {

    }

    public Boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public CompositeDisposable getCompositeDisposible() {
        return compositeDisposable;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

}
