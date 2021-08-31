package com.fanplus.fanplustalk.ui.channel.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fanplus.fanplustalk.R
import com.fanplus.fanplustalk.util.TimeUtil
import io.talkplus.entity.channel.TPChannel
import java.lang.Exception

class ChannelListAdapter(val channelItemListener: ChannelItemListener): RecyclerView.Adapter<ChannelListAdapter.ChannelListItemViewHolder>() {

    interface ChannelItemListener{
        fun channelItemClick(channel: TPChannel, title: String)
    }

    private val _list:  MutableList<TPChannel?> = mutableListOf()
    private var userIdx: String? = null

    fun setUserIdx(idx: String){
        userIdx = idx
    }

    fun addItem(item: List<TPChannel?>){
        _list.clear()
        _list.addAll(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return ChannelListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelListItemViewHolder, position: Int) {
        val data = _list[position]
        data?.run {
            var titleName: String = ""
            for (i in members){
                if (i.userId != userIdx){
                    titleName += "${i.username}, "
                }
            }

            try {
                titleName = titleName.substring(0, titleName.length.minus(2))
            }catch (e: Exception){}
            holder.title.text = titleName

            lastMessage?.let {
                holder.lastMessage.text = it.text.toString()
                holder.lastTime.text = TimeUtil.getFormattedDay(it.createdAt)
            }

            if (unreadCount == 0){
                holder.unreadBadge.visibility = View.GONE
            }else{
                holder.unreadBadge.visibility = View.VISIBLE
                holder.unreadBadge.text = unreadCount.toString()
            }

            holder.itemView.setOnClickListener {
                holder.unreadBadge.visibility = View.GONE
                channelItemListener.channelItemClick(data, titleName)
            }
        }
    }

    override fun getItemCount(): Int = _list.size

    class ChannelListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.channel_name)
        val lastMessage: TextView = itemView.findViewById(R.id.channel_last_message)
        val lastTime: TextView = itemView.findViewById(R.id.channel_last_time)
        val unreadBadge: TextView = itemView.findViewById(R.id.channel_unread_badge)
    }
}