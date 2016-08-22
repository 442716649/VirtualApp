package io.virtualapp;

import android.app.Activity;
import android.content.Intent;

import com.lody.virtual.client.listeners.ActivityManagerListener;
import com.lody.virtual.helper.utils.VLog;

/**
 * Created by 247321453 on 2016/8/22.
 */
public class MyAcitivityListener implements ActivityManagerListener {
    @Override
    public void onActivityCreate(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityDestroy(Activity activity) {
        if ("com.whatsapp".equals(activity.getPackageName()) && "com.whatsapp.VoipActivity".equals(activity.getClass().getName())) {
            activity.stopService(new Intent().setClassName("com.whatsapp", "com.whatsapp.VoiceService"));
            VLog.w("ActivityManager", "stop service");
        }
    }
}
