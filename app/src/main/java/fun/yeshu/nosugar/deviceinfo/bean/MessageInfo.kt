package `fun`.yeshu.nosugar.deviceinfo.bean

data class MessageInfo(
    val address: String,
    val person: Int,
    val body: String,
    val date: String,
    val type: Int, // 1，接受 2，发送
    var selected: Boolean = false
)