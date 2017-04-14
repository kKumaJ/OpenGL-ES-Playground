package com.kumaj.opengl_es_playground.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;

public final class OpenGLUtils {

    public static final int sGLEsVersion2 = 0x20000;

    public static final int sGLEsVersion3 = 0x30000;

    private OpenGLUtils() {}

    public static boolean isSupportsEs2(Context context) {
        return isSupportsEsx(context, sGLEsVersion2);
    }

    public static boolean isSupportsEs3(Context context) {
        return isSupportsEsx(context, sGLEsVersion3);
    }

    private static boolean isSupportsEsx(Context context, int version) {
        final ActivityManager activityManager =
            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
            activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= version ||
            (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
            && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
            ));
    }

}
