package `fun`.yeshu.nosugar.deviceinfo

import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityMainBinding
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

        initDeviceInfo()
        binding.btnCalllog.setOnClickListener { goToCallLog() }
        binding.btnSms.setOnClickListener { goToSmsLog() }
        binding.btnBackupApk.setOnClickListener {backupApk()}
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
        run()
        //startActivity(Intent(this, MessageListActivity::class.java))
        //println("sms->${SmsUtils.getSmsInPhone(this).size}")
    }

    private fun initDeviceInfo() {
        val basic = SystemUtils.getSysVersionInfo(this)
        val imei = SystemUtils.getIMEI(this)
        val storage = SystemUtils.getTotalInternalMemorySize() + SystemUtils.getAvailableInternalMemorySize() +
                SystemUtils.getTotalExternalMemorySize() + SystemUtils.getAvailableExternalMemorySize()
        val mac = SystemUtils.getMacAddress(this)
        binding.basicInfo.text = basic
        binding.imeiInfo.text = imei
        binding.storageInfo.text = storage
        binding.macInfo.text = mac + "\n" + MacUtils.getMacAddress() + "\n" + HardWareAddressUtils.getMachineHardwareAddress()
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