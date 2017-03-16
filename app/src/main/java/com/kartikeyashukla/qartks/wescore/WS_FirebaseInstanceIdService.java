package com.kartikeyashukla.qartks.wescore;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by qartks on 3/15/17.
 */

public class WS_FirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TOKEN_BROARDCAST = "FCNBroadCast";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROARDCAST));
        storeToken(refreshedToken);
    }

    private void storeToken(String refreshedToken) {
        SharedPrefManager.getInstance(this).storeToken(refreshedToken);
        // TODO Store token to app server , external Db
    }


}
