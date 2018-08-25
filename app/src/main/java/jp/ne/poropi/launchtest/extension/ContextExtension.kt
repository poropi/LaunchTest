package jp.ne.poropi.launchtest.extension

import android.content.Context
import android.os.Build
import android.provider.Settings

fun Context.checkOverlayPermission(): Boolean {
    if (Build.VERSION.SDK_INT < 23) {
        return true
    }
    return Settings.canDrawOverlays(this)
}