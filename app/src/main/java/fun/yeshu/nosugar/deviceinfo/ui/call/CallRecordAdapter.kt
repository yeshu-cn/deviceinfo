package `fun`.yeshu.nosugar.deviceinfo.ui.call

import `fun`.yeshu.nosugar.deviceinfo.R
import `fun`.yeshu.nosugar.deviceinfo.model.CallRecord
import `fun`.yeshu.nosugar.deviceinfo.model.MessageInfo
import `fun`.yeshu.nosugar.deviceinfo.utils.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CallRecordAdapter(
    private var data: List<CallRecord>
) : RecyclerView.Adapter<CallRecordAdapter.ViewHolder>() {
    private var listener: OnClickListener? = null
    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun updateData(newData: List<CallRecord>) {
        this.data = newData
        selectMode = false
        notifyDataSetChanged()
    }

    var selectMode: Boolean = false

    interface OnClickListener {
        fun onClick(position: Int, item: CallRecord)

        fun onLongClick(position: Int, item: CallRecord)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.findViewById(R.id.tv_number)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
        val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_call_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        if (null == item.name) {
            holder.tvNumber.text = item.number
        } else {
            holder.tvNumber.text = String.format("%s(%s)", item.name, item.number)
        }

        holder.tvDate.text = TimeUtils.formatTime(item.time)
        when (item.type) {
            1 -> {
                holder.tvDuration.text = String.format("%s(%s)", TimeUtils.formatDuration(item.duration), "呼入")
            }
            2 -> {
                holder.tvDuration.text = String.format("%s(%s)", TimeUtils.formatDuration(item.duration), "呼出")
            }
            3 -> {
                holder.tvDuration.text = String.format("%s(%s)", TimeUtils.formatDuration(item.duration), "未接")
            }
            else -> {
                holder.tvDuration.text = String.format("%s(%s)", TimeUtils.formatDuration(item.duration), "unknown")
            }
        }
        if (item.selected) {
            holder.ivAvatar.setImageResource(R.drawable.icon_select)
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_default_person)
        }

        holder.itemView.setOnLongClickListener {
            listener?.onLongClick(position, item)
            true
        }
        holder.itemView.setOnClickListener {
            listener?.onClick(position, item)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}