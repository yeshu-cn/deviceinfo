package `fun`.yeshu.nosugar.deviceinfo.ui.message

import `fun`.yeshu.nosugar.deviceinfo.R
import `fun`.yeshu.nosugar.deviceinfo.model.MessageInfo
import `fun`.yeshu.nosugar.deviceinfo.utils.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private var data: List<MessageInfo>
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var listener: OnClickListener? = null
    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun updateData(data: List<MessageInfo>) {
        this.data = data
        selectMode = false
        notifyDataSetChanged()
    }

    var selectMode: Boolean = false

    interface OnClickListener {
        fun onClick(position: Int, item: MessageInfo)

        fun onLongClick(position: Int, item: MessageInfo)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
        val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val ivAvatar: ImageView = itemView.findViewById(R.id.iv_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        if (null == item.contactName) {
            holder.tvPhone.text = item.address
        } else {
            holder.tvPhone.text = String.format("%s(%s)", item.contactName, item.address)
        }
        when (item.type) {
            1 -> {
                holder.tvTime.text = String.format("%s(%s)", TimeUtils.formatTime(item.date), "发送")
            }
            2 -> {
                holder.tvTime.text = String.format("%s(%s)", TimeUtils.formatTime(item.date), "接受")
            }
            else -> {
                holder.tvTime.text = String.format("%s(%s)", TimeUtils.formatTime(item.date), "unknown")
            }
        }
        holder.tvContent.text = item.body

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