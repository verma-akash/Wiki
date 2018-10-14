package com.example.akash.wikipedia.ui.base;

/**
 * Created by Akash Verma on 13/10/18.
 */
public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void handleApiError();
}
