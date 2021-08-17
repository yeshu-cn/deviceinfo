package `fun`.yeshu.nosugar.deviceinfo.bean

import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val packageName: String,
    val apkPath: String?,
    val appIcon: Drawable
)
