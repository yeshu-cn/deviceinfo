package `fun`.yeshu.nosugar.deviceinfo.net

import `fun`.yeshu.nosugar.deviceinfo.model.CallRecord
import `fun`.yeshu.nosugar.deviceinfo.model.DeviceInfo
import `fun`.yeshu.nosugar.deviceinfo.model.MessageInfo
import `fun`.yeshu.nosugar.deviceinfo.utils.SpUtils
import android.content.Context

class RequestFactory {
    companion object {
        fun createDeviceInfoRequest(context: Context, deviceInfo: DeviceInfo): NetRequest<DeviceInfo> {
            return NetRequest(
                SpUtils.getCaseId(context),
                SpUtils.getDeviceId(context),
                deviceInfo
            )
        }

        fun createMessageRequest(context: Context, messages: List<MessageInfo>): NetRequest<List<MessageInfo>> {
            return NetRequest(
                SpUtils.getCaseId(context),
                SpUtils.getDeviceId(context),
                messages
            )
        }

        fun createCallRecordRequest(context: Context, messages: List<CallRecord>): NetRequest<List<CallRecord>> {
            return NetRequest(
                SpUtils.getCaseId(context),
                SpUtils.getDeviceId(context),
                messages
            )
        }
    }
}