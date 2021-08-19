package `fun`.yeshu.nosugar.deviceinfo.ui

import `fun`.yeshu.nosugar.deviceinfo.R
import `fun`.yeshu.nosugar.deviceinfo.utils.SpUtils
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivitySetInfoBinding
import `fun`.yeshu.nosugar.deviceinfo.utils.DeviceInfoUtils
import `fun`.yeshu.nosugar.deviceinfo.utils.PermissionUtils
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class SetInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_set_info)
        }

        var caseId = SpUtils.getCaseId(this)
        if (caseId.isEmpty()) {
            caseId = obtainCaseId()
        }
        val deviceId = SpUtils.getDeviceId(this)
        if (deviceId.isEmpty()) {
            PermissionUtils.checkAndRequestPermission(
                this, Manifest.permission.READ_PHONE_STATE, 0
            ) {
                binding.inputDeviceId.setText(obtainDeviceId())
            }

        }
        binding.inputCaseId.setText(caseId)
        binding.inputDeviceId.setText(deviceId)

        binding.btnSave.setOnClickListener { save() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.inputDeviceId.setText(obtainDeviceId())
        }
    }


    private fun obtainCaseId(): String {
        return System.currentTimeMillis().toString()
    }

    private fun obtainDeviceId(): String {
        val id = DeviceInfoUtils.getImei(this)
        return if ("unknown" == id) {
            UUID.randomUUID().toString()
        } else {
            id
        }
    }

    private fun save() {
        val caseId = binding.inputCaseId.text.toString()
        val deviceId = binding.inputDeviceId.text.toString()
        if (caseId.isEmpty()) {
            binding.inputCaseId.error = getString(R.string.errot_input_not_null)
            return
        }
        if (deviceId.isEmpty()) {
            binding.inputDeviceId.error = getString(R.string.errot_input_not_null)
            return
        }
        SpUtils.saveInfo(
            this,
            caseId,
            deviceId,
        )
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}