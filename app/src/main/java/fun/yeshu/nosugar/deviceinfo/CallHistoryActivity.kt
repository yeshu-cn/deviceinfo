package `fun`.yeshu.nosugar.deviceinfo

import `fun`.yeshu.nosugar.deviceinfo.bean.CallRecord
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityCallHistoryBinding
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager




class CallHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallHistoryBinding
    private lateinit var callback: ActionMode.Callback
    private var actionMode: ActionMode? = null
    private var selectData : ArrayList<CallRecord> = ArrayList()
    private lateinit var adapter: CallRecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "通话记录"
        }

        callback = initActionMode()

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        val data = CallInfoUtils.getCallInfos(this)
        adapter = CallRecordAdapter(data)
        adapter.setListener(object: CallRecordAdapter.OnClickListener{
            override fun onClick(position: Int, item: CallRecord) {
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

            override fun onLongClick(position: Int, item: CallRecord) {
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
                return false
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