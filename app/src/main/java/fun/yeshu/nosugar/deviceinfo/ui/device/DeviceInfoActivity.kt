package `fun`.yeshu.nosugar.deviceinfo.ui.device

import `fun`.yeshu.nosugar.deviceinfo.R
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityDeviceInfoBinding
import `fun`.yeshu.nosugar.deviceinfo.model.DeviceInfo
import `fun`.yeshu.nosugar.deviceinfo.net.RequestFactory
import `fun`.yeshu.nosugar.deviceinfo.utils.DeviceInfoUtils
import `fun`.yeshu.nosugar.deviceinfo.utils.PermissionUtils
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson

class DeviceInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceInfoBinding
    private lateinit var deviceInfo: DeviceInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_device_info)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }


        PermissionUtils.checkAndRequestPermission(
            this, Manifest.permission.READ_PHONE_STATE, 0
        ) {
            updateData()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateData()
        }
    }

    private fun updateData()  {
        deviceInfo = DeviceInfoUtils.getDeviceInfo(this)
        binding.tvSn.text = deviceInfo.sn
        binding.tvAndroidId.text = deviceInfo.androidId
        binding.tvMac.text = deviceInfo.mac
        binding.tvImei.text = deviceInfo.imei
        binding.tvMeid.text = deviceInfo.meid
        binding.tvBrand.text = deviceInfo.brand
        binding.tvSdkVersion.text = deviceInfo.sdkVersion.toString()
        binding.tvReleaseVersion.text = deviceInfo.releaseVersion
        binding.tvScreen.text = deviceInfo.screen
        binding.tvInternalMemorySize.text = deviceInfo.internalMemorySize
        binding.tvExternalMemorySize.text = deviceInfo.externalMemorySize
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.action_upload) {
            uploadData()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_upload, menu)
        return true
    }

    private fun uploadData() {
        val gson = Gson()
        val request = RequestFactory.createDeviceInfoRequest(this, deviceInfo)
        val data = gson.toJson(request)
        println(data)
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }

}