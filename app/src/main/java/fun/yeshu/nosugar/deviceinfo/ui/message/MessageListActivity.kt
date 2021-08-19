package `fun`.yeshu.nosugar.deviceinfo.ui.message

import `fun`.yeshu.nosugar.deviceinfo.R
import `fun`.yeshu.nosugar.deviceinfo.utils.SmsUtils
import `fun`.yeshu.nosugar.deviceinfo.model.MessageInfo
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityMessageListBinding
import `fun`.yeshu.nosugar.deviceinfo.model.User
import `fun`.yeshu.nosugar.deviceinfo.net.RequestFactory
import `fun`.yeshu.nosugar.deviceinfo.ui.call.CallRecordAdapter
import `fun`.yeshu.nosugar.deviceinfo.utils.CallInfoUtils
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import android.R.attr.data
import android.content.pm.PackageManager


class MessageListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageListBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var callback: ActionMode.Callback
    private var actionMode: ActionMode? = null
    private var selectData : ArrayList<MessageInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "短信列表"
        }

        callback = initActionMode()

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        val data = SmsUtils.getSmsInPhone(this)
        adapter = MessageAdapter(data)

        adapter.setListener(object: MessageAdapter.OnClickListener {
            override fun onClick(position: Int, item: MessageInfo) {
                if (adapter.selectMode) {
                    if (item.selected) {
                        selectData.remove(item)
                    } else {
                        selectData.add(item)
                    }
                    item.selected = !item.selected
                    adapter.notifyItemChanged(position)
                }
                updateActionModeTitle()
            }

            override fun onLongClick(position: Int, item: MessageInfo) {
                if (item.selected) {
                    return
                }
                item.selected = true
                selectData.add(item)
                adapter.notifyItemChanged(position)
                if (!adapter.selectMode) {
                    actionMode = startActionMode(callback)
                    adapter.selectMode = true
                }
                updateActionModeTitle()
            }

        })
        binding.recyclerView.adapter = adapter
    }

    private fun updateActionModeTitle() {
        actionMode?.let {
            it.title = selectData.size.toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initActionMode(): ActionMode.Callback {
        return object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.let {
                    val inflater: MenuInflater = it.menuInflater
                    inflater.inflate(R.menu.menu_upload, menu)
                }
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                upload()
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                selectData.forEach {
                    it.selected = false
                }
                selectData.clear()
                adapter.selectMode = false
                adapter.notifyDataSetChanged()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val data = SmsUtils.getSmsInPhone(this)
            selectData.clear()
            adapter.updateData(data)
        }
    }

    private fun upload() {
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val request = RequestFactory.createMessageRequest(this, selectData)
        val data = gson.toJson(request)
        println(data)
        val user = User("1", "2", "3", "1")
        println("-------->${gson.toJson(selectData)}")
        Toast.makeText(this@MessageListActivity, data, Toast.LENGTH_SHORT).show()
    }
}