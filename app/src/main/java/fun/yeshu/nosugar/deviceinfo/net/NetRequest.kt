package `fun`.yeshu.nosugar.deviceinfo.net

import com.google.gson.annotations.Expose

data class NetRequest<T>(
    @Expose
    val caseId: String,
    @Expose
    val deviceId: String,
    @Expose
    val body: T
)