package jp.ne.poropi.launchtest.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

val REQUEST_SYSTEM_OVERLAY = 0

fun Activity.requestOverlayPermission() {
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${getPackageName()}"));
    this.startActivityForResult(intent, REQUEST_SYSTEM_OVERLAY)
}