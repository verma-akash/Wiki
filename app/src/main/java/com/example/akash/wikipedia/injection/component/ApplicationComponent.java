package com.example.akash.wikipedia.injection.component;

import com.example.akash.wikipedia.WikiApplication;
import com.example.akash.wikipedia.data.DataManager;
import com.example.akash.wikipedia.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Akash Verma on 13/10/18.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WikiApplication wikiApplication);

    DataManager getDataManager();
}