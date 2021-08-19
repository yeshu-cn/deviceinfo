package `fun`.yeshu.nosugar.deviceinfo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CallRecord(
    // 姓名
    @Expose
    @SerializedName("name")
    val name: String?,
    // 号码
    @Expose
    @SerializedName("number")
    val number: String,
    // 通话日期
    @Expose
    @SerializedName("time")
    val time: Long,
    // 通话时长
    @Expose
    @SerializedName("duration")
    val duration: Long,
    // 通话类型 1.呼入 2.呼出 3.未接
    @Expose
    @SerializedName("type")
    val type: Int,

    @Expose(serialize = false, deserialize = false)
    var selected: Boolean = false
)
