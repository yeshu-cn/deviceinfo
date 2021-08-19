package `fun`.yeshu.nosugar.deviceinfo.model

import com.google.gson.annotations.Expose

//https://juejin.cn/post/6844903688193081352
data class DeviceInfo(
    @Expose
    val sn: String? = null,
    @Expose
    val androidId: String? = null,
    @Expose
    val mac: String? = null,
    @Expose
    val imei: String? = null,
    @Expose
    val meid: String? = null,
    @Expose
    val brand: String,
    @Expose
    val sdkVersion: Int,
    @Expose
    val releaseVersion: String,
    @Expose
    val screen: String,
    @Expose
    val internalMemorySize: String,
    @Expose
    val externalMemorySize: String


) {
    override fun toString(): String {
        return "DeviceInfo(sn=$sn, androidId=$androidId, mac=$mac, imei=$imei, meid=$meid, brand='$brand', sdkVersion=$sdkVersion, releaseVersion='$releaseVersion', screen='$screen')"
    }
}
