package com.lody.virtual.client.ipc;

import android.app.PendingIntent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.lody.virtual.client.hook.proxies.location.LocationManagerStub;
import com.lody.virtual.helper.utils.Reflect;
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

    public void setVirtualLocation(int userId, String pkg, Location location) {
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
        ProxyResult result = new ProxyResult();
        if ("isProviderEnabled".equals(method)) {
            String provider = (String) args[0];
            result.setResult((boolean) LocationManager.GPS_PROVIDER.equals(provider));
        } else if ("registerGnssStatusCallback".equals(method)) {
            //TODO 各种api适配
            Object gnssStatusListenerTransport = args[0];
            GpsStatus.Listener listener = Reflect.on(gnssStatusListenerTransport).get("mGpsListener");
            Object mOldGnssCallback = Reflect.on(gnssStatusListenerTransport).get("mOldGnssCallback");
            if(mOldGnssCallback != null) {
                //TODO java's Proxy
                //gnssStatusListenerTransport.onSvStatusChanged(int svCount, int[] prnWithFlags, float[] cn0s, float[] elevations, float[] azimuths)
            }
            //更新gps状态
            Reflect.on(gnssStatusListenerTransport).set("mGpsListener", new GpsStatusListenerProxy(listener));
        } else if ("getLastLocation".equals(method)) {
            String provider = Reflect.on(args[0]).call("getProvider").get();
            //return Location
            Location location = new Location(provider);
            //自己伪造一下
            result.setResult(location);
        } else if ("requestGeofence".equals(method)) {
            Object request = args[0];
            Object geofence = args[1];
            //TODO geofence修正：根据当前虚拟位置修正通知范围
            PendingIntent intent = (PendingIntent) args[2];
            //TODO 当进入某个范围出发intent.send();
        } else if ("removeGeofence".equals(method)) {
            Object geofence = args[0];
            PendingIntent intent = (PendingIntent) args[1];
            //TODO
        } else if ("requestLocationUpdates".equals(method)) {
            Object request = args[0];
            Object listenerTransport = args[1];
            LocationListener locationListener = Reflect.on(listenerTransport).get("mListener");
            Reflect.on(listenerTransport).set("mListener", new LocationListenerProxy(locationListener));
            //TODO 系统返回位置信息，通过LocationListenerProxy封装
            //requestLocationUpdates(request, transport, intent, packageName)
        } else if ("removeUpdates".equals(method)) {
            //removeUpdates(transport, null, packageName)
        }
        return result;
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

    private static class LocationListenerProxy implements LocationListener {
        private LocationListener mProxy;

        LocationListenerProxy(LocationListener proxy) {
            mProxy = proxy;
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private static class GpsStatusListenerProxy implements GpsStatus.Listener {
        private GpsStatus.Listener mProxy;

        GpsStatusListenerProxy(GpsStatus.Listener listener) {

        }

        @Override
        public void onGpsStatusChanged(int event) {

        }
    }

}
