package `fun`.yeshu.nosugar.deviceinfo

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object ApkUtils {
    //查询已安装的APP
    fun queryApps(context: Context): List<ResolveInfo> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        return  pm.queryIntentActivities(intent, 0).filter {
            !isSysApp(pm , it)
        }
    }

    private fun isSysApp(pm : PackageManager, info: ResolveInfo): Boolean{
        val packageInfo = pm.getPackageInfo(info.activityInfo.packageName, 0)
        return (ApplicationInfo.FLAG_SYSTEM and packageInfo.applicationInfo.flags) != 0
    }

    fun copyApk(context: Context, name: String, path: String) {
        val dest = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path + name + ".apk"
        //path:app程序源文件路径  dest:新的存储路径  name:app名称
        Thread(CopyRunnable(path, dest, name)).start()
    }

    fun getApk(context: Context, packageName: String): String? {
        var appDir: String? = null
        try {
            //通过包名获取程序源文件路径
            appDir = context.packageManager.getApplicationInfo(packageName, 0).sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appDir
    }


    class CopyRunnable(
        private val source: String,
        private val dest: String,
        private val key: String
    ) : Runnable {
        override fun run() {
            try {
                println("dest is $dest")
                var length = 1024 * 1024
                val fDest = File(dest)
                if (fDest.exists()) {
                    fDest.delete()
                }
                fDest.createNewFile()
                val `in` = FileInputStream(File(source))
                val out = FileOutputStream(fDest)
                val inC = `in`.channel
                val outC = out.channel
                var i = 0
                while (true) {
                    if (inC.position() == inC.size()) {
                        inC.close()
                        outC.close()
                        //成功
                        break
                    }
                    length = if (inC.size() - inC.position() < 1024 * 1024) {
                        (inC.size() - inC.position()).toInt()
                    } else {
                        1024 * 1024
                    }
                    inC.transferTo(inC.position(), length.toLong(), outC)
                    inC.position(inC.position() + length)
                    i++
                }
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }
    }
}