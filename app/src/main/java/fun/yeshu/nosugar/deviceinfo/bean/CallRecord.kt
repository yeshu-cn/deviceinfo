package `fun`.yeshu.nosugar.deviceinfo.bean

data class CallRecord(
    val number: String,
    val time: Long,
    val type: Int,
    val duration: Long,
    var selected: Boolean = false
)
