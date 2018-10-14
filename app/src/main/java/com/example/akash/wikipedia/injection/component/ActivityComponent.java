package com.example.akash.wikipedia.injection.component;

import com.example.akash.wikipedia.injection.module.ActivityModule;
import com.example.akash.wikipedia.injection.scope.PerActivity;
import com.example.akash.wikipedia.ui.search.SearchResultActivity;

import dagger.Component;

/**
 * Created by Akash Verma on 13/10/18.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SearchResultActivity searchResultActivity);
}
