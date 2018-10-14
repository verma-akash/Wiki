package com.example.akash.wikipedia.injection.module;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.akash.wikipedia.injection.qualifier.ActivityContext;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Akash Verma on 13/10/18.
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment provideActivity() {
        return fragment;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return fragment.getActivity();
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}

