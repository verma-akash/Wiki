package com.example.akash.wikipedia.injection.module;

import android.app.Application;
import android.content.Context;

import com.example.akash.wikipedia.data.AppDataManager;
import com.example.akash.wikipedia.data.DataManager;
import com.example.akash.wikipedia.data.local.AppPreferenceManager;
import com.example.akash.wikipedia.data.local.PreferenceManager;
import com.example.akash.wikipedia.data.remote.ApiManager;
import com.example.akash.wikipedia.data.remote.AppApiManager;
import com.example.akash.wikipedia.injection.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Akash Verma on 13/10/18.
 */
@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    ApiManager provideApiManager(AppApiManager appApiManager) {
        return appApiManager;
    }

    @Provides
    @Singleton
    PreferenceManager providePreferenceManager(AppPreferenceManager appPreferenceManager) {
        return appPreferenceManager;
    }
}
