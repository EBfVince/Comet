package com.ebfstudio.comet.extension

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View

/**
 * Permet de rendre la status bar transparente facilement
 * Cela fonctionne que pour SDK >= 21 (LOLLIPOP)
 */
fun Activity.setTransparentStatusBar() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.TRANSPARENT
    }
}