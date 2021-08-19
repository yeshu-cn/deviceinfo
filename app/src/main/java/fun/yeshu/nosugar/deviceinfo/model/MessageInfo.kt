package `fun`.yeshu.nosugar.deviceinfo.model

import android.net.Uri
import com.google.gson.annotations.Expose

data class MessageInfo(
    // 发件人地址，手机号.如+8613811810000
    @Expose
    val address: String,
    // 通讯录中联系人姓名
    @Expose
    val contactName: String?,
    // 类型 1是接收到的，2是已发出
    @Expose
    val body: String,
    // 日期  long型
    @Expose
    val date: Long,
    // 1，接受 2，发送
    @Expose
    val type: Int,
    @Expose(serialize = false, deserialize = false)
    val photo: Uri? = null,
    @Expose(serialize = false, deserialize = false)
    var selected: Boolean = false
)