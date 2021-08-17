package `fun`.yeshu.nosugar.deviceinfo

import `fun`.yeshu.nosugar.deviceinfo.bean.MessageInfo
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityMessageListBinding
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

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

        adapter.setListener(object: MessageAdapter.OnClickListener{
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
                Toast.makeText(this@MessageListActivity, "select ${selectData.size}", Toast.LENGTH_SHORT).show()
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
}