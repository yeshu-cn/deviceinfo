package `fun`.yeshu.nosugar.deviceinfo.utils

import android.content.Context

object SpUtils {
    const val FILE_NAME = "config"
    const val KEY_CASE_ID = "caseId"
    const val KEY_DEVICE_ID = "deviceId"

    fun saveInfo(context: Context, caseId: String, deviceId: String) {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(KEY_CASE_ID, caseId)
        editor.putString(KEY_DEVICE_ID, deviceId)
        editor.apply()
    }

    fun getCaseId(context: Context): String {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val value = sp.getString(KEY_CASE_ID, "")
        return  value?:""
    }


    fun getDeviceId(context: Context): String {
        val sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val value = sp.getString(KEY_DEVICE_ID, "")
        return  value?:""
    }

}