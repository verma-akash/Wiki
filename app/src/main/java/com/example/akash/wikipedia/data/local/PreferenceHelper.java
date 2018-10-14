package com.example.akash.wikipedia.data.local;

import android.content.Context;

import com.example.akash.wikipedia.injection.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Akash Verma on 13/10/18.
 */
@Singleton
public class PreferenceHelper {

    private Context context;
    private ObscuredSharedPreference mPref;
    private static final String PREF_FILE_NAME = "_shared_prefs_file";


    @Inject
    public PreferenceHelper(@ApplicationContext Context context) {
        this.context = context;
        mPref = ObscuredSharedPreference.getPrefs(context, PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    void clear() {
        mPref.edit().clear().apply();
    }

    String getString(String KEY, String DEFAULT) {
        return mPref.getString(KEY, DEFAULT);
    }

    void putString(String KEY, String VALUE) {
        mPref.edit().putString(KEY, VALUE).apply();
    }

    void putDouble(String KEY, Double VALUE) {
        mPref.edit().putLong(KEY, java.lang.Double.doubleToRawLongBits(VALUE)).apply();
    }

    Double getDouble(String KEY, Double DEFAULT) {
        return java.lang.Double.longBitsToDouble(mPref.getLong(KEY, java.lang.Double.doubleToLongBits(DEFAULT)));
    }

    Boolean getBoolean(String KEY, Boolean DEFAULT) {
        return mPref.getBoolean(KEY, DEFAULT);
    }

    void putBoolean(String KEY, Boolean VALUE) {
        mPref.edit().putBoolean(KEY, VALUE).apply();
    }
}