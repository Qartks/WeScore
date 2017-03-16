package com.kartikeyashukla.qartks.wescore;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qartks on 3/15/17.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String KEY_ACCESS_TOKEN = "FCMToken";

    private static Context mCtx;
    private static SharedPrefManager mSharedPref;

    private SharedPrefManager(Context context) {
        this.mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance (Context context) {
        if (mSharedPref == null) {
            return mSharedPref = new SharedPrefManager(context);
        }
        return mSharedPref;
    }

    public boolean storeToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        return true;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_APPEND);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }
}
