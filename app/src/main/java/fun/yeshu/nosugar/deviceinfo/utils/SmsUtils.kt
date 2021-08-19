package `fun`.yeshu.nosugar.deviceinfo.utils

import `fun`.yeshu.nosugar.deviceinfo.model.MessageInfo
import android.Manifest
import android.content.Context
import android.database.sqlite.SQLiteException
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*
import android.provider.ContactsContract.PhoneLookup

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract

import android.content.ContentUris
import java.lang.Exception


object SmsUtils {
    fun getSmsInPhone(context: Context): List<MessageInfo> {
        val smsList = mutableListOf<MessageInfo>()
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS)
        PermissionUtils.checkAndRequestMorePermissions(
            context,
            permissions,
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
                    val indexAddress = cur.getColumnIndex("address")
                    val indexPerson = cur.getColumnIndex("person")
                    val indexBody = cur.getColumnIndex("body")
                    val indexDate = cur.getColumnIndex("date")
                    val indexType = cur.getColumnIndex("type")
                    while (true) {
                        val strAddress = cur!!.getString(indexAddress)
                        val contactName = getContactName(context, strAddress)
                        val strbody = cur.getString(indexBody)
                        val longDate = cur.getLong(indexDate)
                        val intType = cur.getInt(indexType)
                        smsList.add(
                            MessageInfo(
                                strAddress,
                                contactName,
                                strbody,
                                longDate,
                                intType
                            )
                        )
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

    private fun getContactName(context: Context, phoneNumber: String?): String? {
        val cr = context.contentResolver
        val uri = Uri.withAppendedPath(
            PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor =
            cr.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null)
                ?: return null
        var contactName: String? = null
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(
                cursor
                    .getColumnIndex(PhoneLookup.DISPLAY_NAME)
            )
        }
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        return contactName
    }

    fun fetchContactIdFromPhoneNumber(context: Context, phoneNumber: String?): String? {
        val cr = context.contentResolver
        val uri = Uri.withAppendedPath(
            PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor =
            cr.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null)
                ?: return null
        var contactId: String? = ""
        if (cursor.moveToFirst()) {
            do {
                contactId = cursor.getString(
                    cursor
                        .getColumnIndex(PhoneLookup._ID)
                )
            } while (cursor.moveToNext())
        }
        return contactId
    }

    fun getPhotoUri(context: Context, contactId: Long): Uri? {
        val contentResolver: ContentResolver = context.contentResolver
        try {
            val cursor = contentResolver
                .query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID
                            + "="
                            + contactId
                            + " AND "
                            + ContactsContract.Data.MIMETYPE
                            + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                            + "'", null, null
                )
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null // no photo
                }
            } else {
                return null // error in cursor process
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val person = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI, contactId
        )
        return Uri.withAppendedPath(
            person,
            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
        )
    }
}