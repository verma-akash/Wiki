package com.example.akash.wikipedia.injection.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Akash Verma on 13/10/18.
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface PerActivity {
}
