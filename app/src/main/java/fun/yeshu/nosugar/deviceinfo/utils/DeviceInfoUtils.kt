package `fun`.yeshu.nosugar.deviceinfo.utils

import `fun`.yeshu.nosugar.deviceinfo.model.DeviceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

import android.telephony.TelephonyManager
import java.lang.Exception
import java.lang.reflect.Method

// https://juejin.cn/post/6844903688193081352
object DeviceInfoUtils {
    @SuppressLint("HardwareIds")
    fun getDeviceInfo(context: Context): DeviceInfo {
        val sn = getSN()
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val mac = MacUtils.getMacAddress()
        val imei = getImei(context)
        val meid = getMeid(context)
        val brand = Build.BRAND
        val sdkVersion = Build.VERSION.SDK_INT
        val releaseVersion = Build.VERSION.RELEASE
        val screen =
            "${SystemUtils.getScreenWidth(context)}*${SystemUtils.getScreenHeight(context)}"
        return DeviceInfo(
            sn = sn,
            androidId = androidId,
            mac = mac,
            imei = imei,
            meid = meid,
            brand = brand,
            sdkVersion = sdkVersion,
            releaseVersion = releaseVersion,
            screen = screen,
            internalMemorySize = SystemUtils.getTotalInternalMemorySize(),
            externalMemorySize = SystemUtils.getTotalExternalMemorySize()
        )
    }

    fun getImei(context: Context): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "unknown"
        }
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            ?: return "unknown"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei?:"unknown"
        } else {
            telephonyManager.deviceId?:"unknown"
        }
    }

    private fun getMeid(context: Context): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "unknown"
        }
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
            ?: return "unknown"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.meid?:"unknown"
        } else {
            telephonyManager.deviceId?:"unknown"
        }
    }

    fun getSN(): String? {
        var serial = ""
        //通过android.os获取sn号
        try {
            serial = Build.SERIAL
            if (serial != "" && serial != "unknown") return serial
        } catch (e: Exception) {
            serial = ""
        }

        //通过反射获取sn号
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get: Method = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
            if (serial != "" && serial != "unknown") return serial

            //9.0及以上无法获取到sn，此方法为补充，能够获取到多数高版本手机 sn
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) serial = Build.getSerial()
        } catch (e: Exception) {
            serial = ""
        }
        return serial
    }
}