package com.ebfstudio.comet.extension

import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.checkPermissionDenied(permission: String): Boolean {

    val context = context

    return if (context != null) {
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED
    } else {
        Log.w("FragmentExtension", "Attention, on tente d'utiliser le contexte qui est null")
        false
    }

}

fun Fragment.requestPermission(permission: String, requestCode: Int, action: () -> Unit = {}) {
    if (checkPermissionDenied(permission)) {
        requestPermissions(arrayOf(permission), requestCode)
    } else {
        action()
    }
}