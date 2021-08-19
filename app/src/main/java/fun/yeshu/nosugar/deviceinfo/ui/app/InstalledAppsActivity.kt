package `fun`.yeshu.nosugar.deviceinfo.ui.app

import `fun`.yeshu.nosugar.deviceinfo.utils.ApkUtils
import `fun`.yeshu.nosugar.deviceinfo.R
import `fun`.yeshu.nosugar.deviceinfo.model.AppInfo
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityInstalledAppsBinding
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

class InstalledAppsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInstalledAppsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstalledAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.title_installed_app)
        }

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        val data = ApkUtils.queryApps(this).map {
            AppInfo(
                it.loadLabel(packageManager).toString(),
                it.activityInfo.packageName,
                ApkUtils.getApk(applicationContext, it.activityInfo.packageName),
                it.loadIcon(packageManager)
            )
        }
        val adapter = AppInfoAdapter(data)
        adapter.setListener(object: AppInfoAdapter.OnClickListener {
            override fun onClick(appInfo: AppInfo) {
                showDialog(appInfo)
            }

        })
        binding.recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(appInfo: AppInfo) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.msg_upload_apk))
        builder.setNegativeButton(getString(R.string.cancel)
        ) { dialog, _ -> dialog?.dismiss() }
        builder.setPositiveButton(getString(R.string.confirm)
        ) { dialog, _ ->
            dialog?.dismiss()
            uploadApk(appInfo)
        }
        builder.create().show()
    }

    private fun uploadApk(appInfo: AppInfo){
        // todo
    }
}