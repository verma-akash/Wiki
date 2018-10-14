package com.example.akash.wikipedia.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.akash.wikipedia.WikiApplication;
import com.example.akash.wikipedia.injection.component.ActivityComponent;
import com.example.akash.wikipedia.injection.component.DaggerActivityComponent;
import com.example.akash.wikipedia.injection.module.ActivityModule;
import com.example.akash.wikipedia.utils.DialogUtils;
import com.example.akash.wikipedia.utils.NetworkUtils;

/**
 * Created by Akash Verma on 13/10/18.
 */
public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(WikiApplication.get(this).getApplictionComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    public void showLoading(String message) {
        DialogUtils.displayProgressDialog(this, message);
    }

    @Override
    public void hideLoading() {
        DialogUtils.cancelProgressDialog();
    }

    @Override
    public void showToast(Integer resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Some Error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void invalidAuthCode() {
        Toast.makeText(this, "Invalid Authcode", Toast.LENGTH_SHORT).show();
    }
}
