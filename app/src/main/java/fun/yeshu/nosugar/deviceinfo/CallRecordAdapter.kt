package `fun`.yeshu.nosugar.deviceinfo

import `fun`.yeshu.nosugar.deviceinfo.bean.CallRecord
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CallRecordAdapter(
    private val data: List<CallRecord>
) : RecyclerView.Adapter<CallRecordAdapter.ViewHolder>() {
    private var listener: OnClickListener? = null
    fun setListener(listener: OnClickListener) {
        this.listener = listener
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
        holder.tvNumber.text = item.number
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val d = Date(item.time)
        val strDate = dateFormat.format(d)
        holder.tvDate.text = strDate
        holder.tvDuration.text = TimeUtils.formatDuration(item.duration)
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