package `fun`.yeshu.nosugar.deviceinfo.utils

import `fun`.yeshu.nosugar.deviceinfo.model.CallRecord
import android.Manifest
import android.content.Context
import android.provider.CallLog
import java.util.*

object CallInfoUtils {
    fun getCallInfos(context: Context): List<CallRecord> {
        val infos: MutableList<CallRecord> = ArrayList()
        PermissionUtils.checkAndRequestPermission(
            context, Manifest.permission.READ_CALL_LOG, 0
        ) {
            val resolver = context.contentResolver
            // uri的写法需要查看源码JB\packages\providers\ContactsProvider\AndroidManifest.xml中内容提供者的授权
            // 从清单文件可知该提供者是CallLogProvider，且通话记录相关操作被封装到了Calls类中
            val uri = CallLog.Calls.CONTENT_URI
            val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,  // 号码
                CallLog.Calls.DATE,  // 日期
                CallLog.Calls.TYPE, // 类型：来电、去电、未接
                CallLog.Calls.DURATION
            )
            val cursor = resolver.query(uri, projection, null, null, null)
            while (cursor!!.moveToNext()) {
                val name = cursor.getString(0)
                val number = cursor.getString(1)
                val date = cursor.getLong(2)
                val type = cursor.getInt(3)
                val duration = cursor.getLong(4)
                infos.add(CallRecord(name, number, date, duration, type))
            }
            cursor.close()
        }
        return infos
    }
}