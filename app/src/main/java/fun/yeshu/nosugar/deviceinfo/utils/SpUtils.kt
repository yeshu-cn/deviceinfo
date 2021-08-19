package `fun`.yeshu.nosugar.deviceinfo.utils

import android.content.Context

object SpUtils {
    fun saveInfo(context: Context, caseId: String, deviceId: String) {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("caseId", caseId)
        editor.putString("deviceId", deviceId)
        editor.apply()
    }

    fun getCaseId(context: Context): String {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        val value = sp.getString("caseId", "")
        return  value?:""
    }


    fun getDeviceId(context: Context): String {
        val sp = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        val value = sp.getString("deviceId", "")
        return  value?:""
    }

}