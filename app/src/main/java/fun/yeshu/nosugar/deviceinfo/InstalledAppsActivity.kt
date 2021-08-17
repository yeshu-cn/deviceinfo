package `fun`.yeshu.nosugar.deviceinfo

import `fun`.yeshu.nosugar.deviceinfo.bean.AppInfo
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityInstalledAppsBinding
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
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
            title = "已安装的程序"
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
        println("---${data.size}")
        val adapter = AppInfoAdapter(data)
        adapter.setListener(object: AppInfoAdapter.OnClickListener{
            override fun onClick(appInfo: AppInfo) {
                Toast.makeText(this@InstalledAppsActivity, "upload apk file", Toast.LENGTH_SHORT).show();
            }

        })
        binding.recyclerView.adapter = adapter

        startActionMode(initActionMode())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initActionMode(): ActionMode.Callback {
        val callback = object: ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.let {
                    val inflater : MenuInflater = it.menuInflater
                    inflater.inflate(R.menu.menu_upload, menu)
                }
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {

            }

        }
        return callback
    }
}