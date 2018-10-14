package com.example.akash.wikipedia.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


/**
 * Created by Akash Verma on 14/10/18.
 */
public class ObscuredSharedPreference implements SharedPreferences {

    protected String UTF8 = "UTF-8";
    //this key is defined at runtime based on ANDROID_ID which is supposed to last the life of the device
    private char[] SEKRIT;


    protected SharedPreferences delegate;
    protected Context context;

    //Set to true if a decryption error was detected
    //in the case of float, int, and long we can tell if there was a parse error
    //this does not detect an error in strings or boolean - that requires more sophisticated checks
    boolean decryptionErrorFlag = false;


    /**
     * Constructor
     *
     * @param context
     * @param delegate - SharedPreferences object from the system
     */

    public ObscuredSharedPreference(Context context, SharedPreferences delegate) {
        this.delegate = delegate;
        this.context = context;
        SEKRIT = Settings.Secure.ANDROID_ID.toCharArray();
    }

    public SharedPreferences getDelegate() {
        return delegate;
    }

    public void setDelegate(SharedPreferences delegate) {
        this.delegate = delegate;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Only used to change to a new key during runtime.
     * If you don't want to use the default per-device key for example
     *
     * @param key
     */
    void setNewKey(String key) {
        SEKRIT = key.toCharArray();
    }

    /**
     * Accessor to grab the preferences in a singleton.  This stores the reference in a singleton so it can be accessed repeatedly with
     * no performance penalty
     *
     * @param c - the context used to access the preferences.
     * @param appName - domain the shared preferences should be stored under
     * @param contextMode - Typically Context.MODE_PRIVATE
     * @return
     */
    private static ObscuredSharedPreference prefs;

    public static synchronized ObscuredSharedPreference getPrefs(Context c, String appName, Integer contextMode) {
        if (prefs == null) {
            //make sure to use application context since preferences live outside an Activity
            //use for objects that have global scope like: prefs or starting services
            prefs = new ObscuredSharedPreference(
                    c.getApplicationContext(), c.getApplicationContext().getSharedPreferences(appName, contextMode));
        }
        return prefs;
    }


    public class Editor implements SharedPreferences.Editor {
        protected SharedPreferences.Editor delegate;

        public Editor() {
            this.delegate = ObscuredSharedPreference.this.delegate.edit();
        }

        @Override
        public SharedPreferences.Editor putString(String key, @Nullable String value) {
            delegate.putString(key, encrypt(value));
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, @Nullable Set<String> values) {
            throw new RuntimeException("This class does not work with String Sets.");
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            delegate.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            delegate.putString(key, encrypt(java.lang.Long.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            delegate.putString(key, encrypt(java.lang.Float.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            delegate.putString(key, encrypt(java.lang.Boolean.toString(value)));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            delegate.remove(key);
            return this;
        }

        @Override
        public SharedPreferences.Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public void apply() {
            delegate.commit();
        }
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException(); // left as an exercise to the reader
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        String v = delegate.getString(key, null);
        if (v != null)
            return decrypt(v);
        else
            return defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        throw new RuntimeException("This class does not work with String Sets.");
    }

    @Override
    public int getInt(String key, int defValue) {
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getInt(key, defValue);
        }

        try {
            return Integer.parseInt(decrypt(v));
        } catch (NumberFormatException e) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true;
            Log.e(ObscuredSharedPreference.this.getClass().getSimpleName(), "Warning, could not decrypt the value.  Possible incorrect key.  " + e.getMessage());
        }

        return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getLong(key, defValue);
        }

        try {
            return java.lang.Long.parseLong(decrypt(v));
        } catch (NumberFormatException e) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true;
            Log.e(ObscuredSharedPreference.this.getClass().getSimpleName(), "Warning, could not decrypt the value.  Possible incorrect key.  " + e.getMessage());
        }

        return defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getFloat(key, defValue);
        }

        try {
            return java.lang.Float.parseFloat(decrypt(v));
        } catch (NumberFormatException e) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true;
            Log.e(ObscuredSharedPreference.this.getClass().getSimpleName(), "Warning, could not decrypt the value.  Possible incorrect key.  " + e.getMessage());
        }

        return defValue;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
//if these weren't encrypted, then it won't be a string
        String v;
        try {
            v = delegate.getString(key, null);
        } catch (ClassCastException e) {
            return delegate.getBoolean(key, defValue);
        }

        if (v != null)
            return java.lang.Boolean.parseBoolean(decrypt(v));
        else
            return defValue;
    }

    @Override
    public boolean contains(String key) {
        return delegate.contains(key);
    }

    @Override
    public SharedPreferences.Editor edit() {
        return new Editor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        delegate.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        delegate.unregisterOnSharedPreferenceChangeListener(listener);
    }

    protected String encrypt(String value) {

        try {
            byte[] bytes;
            if (value.getBytes(Charset.forName(UTF8)) != null)
                bytes = value.getBytes(Charset.forName(UTF8));
            else
                bytes = new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(Charset.forName(UTF8)), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String decrypt(String value) {
        try {
            byte[] bytes;
            if (value != null)
                bytes = Base64.decode(value, Base64.DEFAULT);
            else
                bytes = new byte[0];

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key,
                    new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID).getBytes(Charset.forName(UTF8)), 20));
            return new String(pbeCipher.doFinal(bytes), Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
