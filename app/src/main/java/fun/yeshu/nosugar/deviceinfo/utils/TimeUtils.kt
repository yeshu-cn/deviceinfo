package `fun`.yeshu.nosugar.deviceinfo.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun formatTime(time: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val d = Date(time)
        return dateFormat.format(d)
    }

    fun formatDuration(duration: Long): String {
        val mm: String
        val ss: String

        val hh: String = String.format("%02d", duration / 3600)

        val mm1 = (duration - hh.toInt() * 3600) / 60
        mm = String.format("%02d", mm1)

        val ss1 = duration - hh.toInt() * 3600 - mm.toInt() * 60
        ss = String.format("%02d", ss1)
        return "${hh}:${mm}:${ss}"
    }
}