package com.lody.virtual.client.listeners;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.IAppTask;
import android.app.IServiceConnection;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.internal.content.ReferrerIntent;

/**
 * Created by 247321453 on 2016/8/22.
 */
public interface ActivityManagerListener {
    void onActivityCreate(Activity activity);

    void onActivityResumed(Activity activity);

    void onActivityDestroy(Activity activity);
}
