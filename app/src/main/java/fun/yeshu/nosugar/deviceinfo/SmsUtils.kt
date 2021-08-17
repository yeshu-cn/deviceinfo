package `fun`.yeshu.nosugar.deviceinfo

import `fun`.yeshu.nosugar.deviceinfo.bean.MessageInfo
import android.content.Context
import android.database.sqlite.SQLiteException
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

object SmsUtils {
    fun getSmsInPhone(context: Context): List<MessageInfo> {
        val smsList = mutableListOf<MessageInfo>()
        PermissionUtils.checkAndRequestPermission(
            context,
            "android.permission.READ_SMS",
            0
        ) {
            try {
                val uri = Uri.parse("content://sms/")
                val projection = arrayOf("_id", "address", "person", "body", "date", "type")
                var cur = context.contentResolver.query(
                    uri,
                    projection,
                    null as String?,
                    null as Array<String?>?,
                    "date desc"
                )
                if (cur!!.moveToFirst()) {
                    val index_Address = cur.getColumnIndex("address")
                    val index_Person = cur.getColumnIndex("person")
                    val index_Body = cur.getColumnIndex("body")
                    val index_Date = cur.getColumnIndex("date")
                    val index_Type = cur.getColumnIndex("type")
                    while (true) {
                        val strAddress = cur!!.getString(index_Address)
                        val intPerson = cur.getInt(index_Person)
                        val strbody = cur.getString(index_Body)
                        val longDate = cur.getLong(index_Date)
                        val intType = cur.getInt(index_Type)
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                        val d = Date(longDate)
                        val strDate = dateFormat.format(d)

                        smsList.add(MessageInfo(strAddress, intPerson, strbody, strDate, index_Type))
                        if (!cur.moveToNext()) {
                            if (!cur.isClosed) {
                                cur.close()
                                cur = null
                            }
                            break
                        }
                    }
                } else {
                }
            } catch (var26: SQLiteException) {
                var26.printStackTrace()
            }
        }
        return smsList
    }
}