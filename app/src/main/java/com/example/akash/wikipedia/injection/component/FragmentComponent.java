package com.example.akash.wikipedia.injection.component;

import com.example.akash.wikipedia.injection.module.FragmentModule;
import com.example.akash.wikipedia.injection.scope.PerActivity;

import dagger.Component;

/**
 * Created by Akash Verma on 13/10/18.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
}
