package com.example.akash.wikipedia;

import android.app.Application;
import android.content.Context;

import com.example.akash.wikipedia.injection.component.ApplicationComponent;
import com.example.akash.wikipedia.injection.component.DaggerApplicationComponent;
import com.example.akash.wikipedia.injection.module.ApplicationModule;

/**
 * Created by Akash Verma on 13/10/18.
 */
public class WikiApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mApplicationComponent.inject(this);

    }

    public static WikiApplication get(Context context) {
        return (WikiApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplictionComponent() {
        return mApplicationComponent;
    }
}
