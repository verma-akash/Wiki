package com.example.akash.wikipedia.ui.base;

import android.support.annotation.StringRes;

/**
 * Created by Akash Verma on 13/10/18.
 */
public interface MvpView {

    void showLoading(String message);

    void hideLoading();

    void showToast(@StringRes Integer resId);

    void showToast(String message);

    Boolean isNetworkConnected();

    void hideKeyboard();

    void invalidAuthCode();

}
