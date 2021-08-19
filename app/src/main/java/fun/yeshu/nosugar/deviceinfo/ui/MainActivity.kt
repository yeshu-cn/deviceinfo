package `fun`.yeshu.nosugar.deviceinfo.ui

import `fun`.yeshu.nosugar.deviceinfo.utils.SpUtils
import `fun`.yeshu.nosugar.deviceinfo.ui.app.InstalledAppsActivity
import `fun`.yeshu.nosugar.deviceinfo.ui.call.CallHistoryActivity
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityMainBinding
import `fun`.yeshu.nosugar.deviceinfo.ui.device.DeviceInfoActivity
import `fun`.yeshu.nosugar.deviceinfo.ui.message.MessageListActivity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "无糖信息采集工具"

        binding.btnDeviceInfo.setOnClickListener {
            goToDeviceInfo()
        }
        binding.btnCalllog.setOnClickListener { goToCallLog() }
        binding.btnSms.setOnClickListener { goToSmsLog() }
        binding.btnBackupApk.setOnClickListener { backupApk() }


        binding.tvCaseId.text = SpUtils.getCaseId(this)
        binding.tvDeviceId.text = SpUtils.getDeviceId(this)
        binding.layoutCaseInfo.setOnClickListener {
            startActivity(Intent(this, SetInfoActivity::class.java))
        }
    }

    private fun backupApk() {
        startActivity(Intent(this, InstalledAppsActivity::class.java))
//        PermissionUtils.checkAndRequestPermission(
//            this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0
//        ){
//            val apkList = ApkUtils.queryApps(this)
//            println("----apk list size is ${apkList.size}")
//            for (apk in apkList) {
//                println(apk.activityInfo.packageName)
//                if (apk.activityInfo.packageName.equals("com.example.material_colors")) {
//                    val apkPath = ApkUtils.getApk(this, apk.activityInfo.packageName)
//                    println("aaaa $apkPath")
//                    FileUtils.copyFileToDownloads(MainActivity@this, File(apkPath))
//                }
//            }
//        }
    }

    private fun goToCallLog() {
        startActivity(Intent(this, CallHistoryActivity::class.java))
        //println("callLog->${CallInfoUtils.getCallInfos(this).size}")
    }

    private fun goToSmsLog() {
        //run()
        startActivity(Intent(this, MessageListActivity::class.java))
        //println("sms->${SmsUtils.getSmsInPhone(this).size}")
    }

    private fun goToDeviceInfo() {
        startActivity(Intent(this, DeviceInfoActivity::class.java))
    }


    private val client = OkHttpClient()

    fun run() {
        val request = Request.Builder()
            .url("http://publicobject.com/helloworld.txt")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
    }


}