package com.lody.virtual.client.ipc;

import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.lody.virtual.client.hook.proxies.location.LocationManagerStub;
import com.lody.virtual.server.ILocationManager;


public class VLocationManager {
    private static final VLocationManager sInstance = new VLocationManager();
    private ILocationManager mRemote;

    private VLocationManager() {
    }

    public static VLocationManager get() {
        return sInstance;
    }

    public ILocationManager getService() {
        if (mRemote == null) {
            synchronized (VLocationManager.class) {
                if (mRemote == null) {
                    final IBinder pmBinder = ServiceManagerNative.getService(ServiceManagerNative.LOCATION);
                    mRemote = ILocationManager.Stub.asInterface(pmBinder);
                }
            }
        }
        return mRemote;
    }

    public void setVirtualLocation(int userId, String pkg, Location location){
        //TODO 设置虚拟位置,pkg是5.0以上才有
    }

    /**
     * getCellLocation
     * getAllCellInfo
     * getNeighboringCellInfo
     * getScanResults
     */
    public boolean hasVirtualLocation(String pkg, int userId) {
        //TODO 是否有虚拟定位
        return false;
    }

    public ProxyResult proxyRequest(String method, String pkg, int userId, Object[] args) {
        //TODO 代理listener
        // 从listenerTransport获取里面的listener
        // 然后包一个壳，再调用系统的方法,
        // 可以基于虚拟的位置，根据实际位移模拟
        // isProviderEnabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //addGpsMeasurementsListener
            //addGpsNavigationMessageListener
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //addGpsStatusListener
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //requestLocationUpdates
            //removeUpdates
            //requestGeofence
            //removeGeofence
            //getLastLocation
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN
                && TextUtils.equals(Build.VERSION.RELEASE, "4.1.2")) {
            //requestLocationUpdates
            //requestLocationUpdatesPI
            //removeUpdates
            //removeUpdatesPI
            //addProximityAlert
            //getLastKnownLocation
        }
        return new ProxyResult();
    }

    public class ProxyResult {
        boolean hasResult;
        Object result;

        public void setResult(Object result) {
            this.result = result;
            this.hasResult = true;
        }

        public boolean isProxy() {
            return hasResult;
        }

        public Object getResult() {
            return result;
        }
    }

}
