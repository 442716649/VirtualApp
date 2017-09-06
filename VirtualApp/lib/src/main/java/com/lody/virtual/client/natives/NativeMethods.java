package com.lody.virtual.client.natives;

import android.hardware.Camera;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import java.lang.reflect.Method;

import dalvik.system.DexFile;

/**
 * @author Lody
 */
public class NativeMethods {

    public static int gCameraMethodType;
    public static int gAudioRecordNativeSetupType = -1;
    public static int gMediaRecorderNativeSetupType = -1;

    public static Method gCameraNativeSetup;

    public static Method gOpenDexFileNative;

    public static Method gAudioRecordNativeCheckPermission;

    public static Method gAudioRecordNativeSetup;

    public static Method gMediaRecorderNativeSetup;

    public static void init() {
        String methodName =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? "openDexFileNative" : "openDexFile";
        for (Method method : DexFile.class.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                gOpenDexFileNative = method;
                break;
            }
        }
        if (gOpenDexFileNative == null) {
            throw new RuntimeException("Unable to find method : " + methodName);
        }
        gOpenDexFileNative.setAccessible(true);

        gCameraMethodType = -1;
        try {
            gCameraNativeSetup = Camera.class.getDeclaredMethod("native_setup", Object.class, int.class, String.class);
            gCameraMethodType = 1;
        } catch (NoSuchMethodException e) {
            // ignore
        }
        if (gCameraNativeSetup == null) {
            try {
                gCameraNativeSetup = Camera.class.getDeclaredMethod("native_setup", Object.class, int.class, int.class, String.class);
                gCameraMethodType = 2;
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }
        // HuaWei common
        if (gCameraNativeSetup == null) {
            try {
                gCameraNativeSetup = Camera.class.getDeclaredMethod("native_setup", Object.class, int.class, int.class, String.class, boolean.class);
                gCameraMethodType = 3;
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }
        // HUAWEI MediaPad X1 7.0
        if (gCameraNativeSetup == null) {
            try {
                gCameraNativeSetup = Camera.class.getDeclaredMethod("native_setup", Object.class, int.class, String.class, boolean.class);
                gCameraMethodType = 4;
            } catch (NoSuchMethodException e) {
                // ignore
            }
        }
        if (gCameraNativeSetup != null) {
            gCameraNativeSetup.setAccessible(true);
        }

        //MediaRecorder api24
        try {
            gMediaRecorderNativeSetup = MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class, String.class);
            gMediaRecorderNativeSetupType = 1;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //api L
        if(gMediaRecorderNativeSetup == null){
            try {
                gMediaRecorderNativeSetup = MediaRecorder.class.getDeclaredMethod("native_setup", Object.class, String.class);
                gMediaRecorderNativeSetupType = 2;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if(gMediaRecorderNativeSetup != null){
            gMediaRecorderNativeSetup.setAccessible(true);
        }

        //AudioRecord L
        try {
            gAudioRecordNativeSetup = AudioRecord.class.getDeclaredMethod("native_setup",
                    Object.class, Object.class, int.class, int.class, int.class, int.class, int.class,
                    int[].class, String.class);
            gAudioRecordNativeSetupType = 1;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        //api 24
        if (gAudioRecordNativeSetup == null) {
            try {
                gAudioRecordNativeSetup = AudioRecord.class.getDeclaredMethod("native_setup",
                        Object.class, Object.class, int[].class, int.class, int.class, int.class, int.class,
                        int[].class, String.class, long.class);
                gAudioRecordNativeSetupType = 2;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if(gAudioRecordNativeSetup != null){
            gAudioRecordNativeSetup.setAccessible(true);
        }

        for (Method mth : AudioRecord.class.getDeclaredMethods()) {
            if (mth.getName().equals("native_check_permission")
                    && mth.getParameterTypes().length == 1
                    && mth.getParameterTypes()[0] == String.class) {
                gAudioRecordNativeCheckPermission = mth;
                mth.setAccessible(true);
                break;
            }
        }
    }

}
