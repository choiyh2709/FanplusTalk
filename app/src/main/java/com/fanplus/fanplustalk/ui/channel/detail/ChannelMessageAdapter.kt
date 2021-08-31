package com.fanplus.fanplustalk.ui.channel.detail

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fanplus.fanplustalk.R
import com.fanplus.fanplustalk.util.TimeUtil
import io.talkplus.entity.channel.TPChannel
import io.talkplus.entity.channel.TPMessage

class ChannelMessageAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val myMessageType = 0
    private val targetMessageType = 1

    private val _list:  MutableList<TPMessage?> = mutableListOf()
    private var userIdx: String? = null
    private var _channel: TPChannel? = null

    fun addItemList(item: List<TPMessage?>){
        _list.addAll(item)
    }

    fun moreAddItemList(item: List<TPMessage?>){
        _list.addAll(0, item)
    }

    fun addItem(item: TPMessage){
        _list.add(item)
    }

    fun setChannel(channel: TPChannel?){
        _channel = channel
    }

    fun setUserIdx(idx: String){
        userIdx = idx
    }

    override fun getItemCount(): Int = _list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            myMessageType -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_message, parent, false)
                MyMessageItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_target_message, parent, false)
                TargetMessageItemViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(_list[position]?.userId){
            userIdx -> myMessageType
            else -> targetMessageType
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = _list[position]
        when(holder){
            is MyMessageItemViewHolder -> {
                data?.let {
                    if (it.fileUrl.isNullOrEmpty()) {
                        holder.message.text = it.text.toString()
                        holder.message.visibility = View.VISIBLE
                        holder.file.visibility = View.GONE
                    }else {
                        Glide.with(holder.itemView).load(it.fileUrl).diskCacheStrategy(
                            DiskCacheStrategy.ALL).placeholder(R.color.white).centerCrop().into(holder.file)
                        holder.message.visibility = View.GONE
                        holder.file.visibility = View.VISIBLE
                        holder.file.setOnClickListener { _ ->
                            val intent = Intent(holder.itemView.context, FileDetailActivity::class.java)
                            intent.putExtra("uri", it.fileUrl)
                            holder.itemView.context.startActivity(intent)
                        }
                    }

                    val itDate = TimeUtil.getFormattedLongDay(it.createdAt)
                    val itTime =  TimeUtil.getFormattedTime(it.createdAt)

                    if (position > 0){
                        val afterDate = TimeUtil.getFormattedLongDay(_list[position.minus(1)]?.createdAt ?: it.createdAt)
                        val afterTime = TimeUtil.getFormattedTime(_list[position.minus(1)]?.createdAt ?: it.createdAt)

                        if (itTime == afterTime && it.userId == _list[position.minus(1)]?.userId){
                            holder.date.visibility = View.GONE
                        }else{
                            holder.date.visibility = View.VISIBLE
                            holder.date.text = itDate
                        }

                        if (itDate == afterDate){
                            holder.date.visibility = View.GONE
                        }else{
                            holder.date.visibility = View.VISIBLE
                            holder.date.text = itDate
                        }

                        if (position < _list.size.minus(1)){
                            val beforeTime = TimeUtil.getFormattedTime(_list[position.plus(1)]?.createdAt ?: it.createdAt)
                            if (beforeTime == itTime && it.userId == _list[position.plus(1)]?.userId){
                                holder.time.visibility = View.GONE
                            }else{
                                holder.time.visibility = View.VISIBLE
                                holder.time.text = itTime
                            }
                        }else {
                            holder.time.visibility = View.VISIBLE
                            holder.time.text = itTime
                        }
                    }else{
                        holder.date.text = itDate
                        holder.time.text = itTime
                        holder.date.visibility = View.VISIBLE
                    }
                }
            }
            is  TargetMessageItemViewHolder -> {
                data?.let {
                    holder.nickname.text = it.username
                    if (it.fileUrl.isNullOrEmpty()) {
                        holder.message.text = it.text.toString()
                        holder.message.visibility = View.VISIBLE
                        holder.file.visibility = View.GONE
                    }else {
                        Glide.with(holder.itemView).load(it.fileUrl).diskCacheStrategy(
                            DiskCacheStrategy.ALL).placeholder(R.color.white).centerCrop().into(holder.file)
                        holder.message.visibility = View.GONE
                        holder.file.visibility = View.VISIBLE
                        holder.file.setOnClickListener { _ ->
                            val intent = Intent(holder.itemView.context, FileDetailActivity::class.java)
                            intent.putExtra("uri", it.fileUrl)
                            holder.itemView.context.startActivity(intent)
                        }
                    }

                    val itDate = TimeUtil.getFormattedLongDay(it.createdAt)
                    val itTime =  TimeUtil.getFormattedTime(it.createdAt)

                    if (position > 0){
                        val afterDate = TimeUtil.getFormattedLongDay(_list[position.minus(1)]?.createdAt ?: it.createdAt)
                        val afterTime = TimeUtil.getFormattedTime(_list[position.minus(1)]?.createdAt ?: it.createdAt)

                        if (itDate == afterDate){
                            holder.date.visibility = View.GONE
                        }else{
                            holder.date.visibility = View.VISIBLE
                            holder.date.text = itDate
                        }

                        if (itTime == afterTime && it.userId == _list[position.minus(1)]?.userId){
                            holder.profile.visibility = View.GONE
                            holder.nickname.visibility = View.GONE
                        }else {
                            holder.profile.visibility = View.VISIBLE
                            holder.nickname.visibility = View.VISIBLE
                        }

                        if (position < _list.size.minus(1)){
                            val beforeTime = TimeUtil.getFormattedTime(_list[position.plus(1)]?.createdAt ?: it.createdAt)
                            if (beforeTime == itTime && it.userId == _list[position.plus(1)]?.userId){
                                holder.time.visibility = View.GONE
                            }else{
                                holder.time.visibility = View.VISIBLE
                                holder.time.text = itTime
                            }
                        }else {
                            holder.time.visibility = View.VISIBLE
                            holder.time.text = itTime
                        }
                    }else{
                        holder.date.text = itDate
                        holder.time.text = itTime
                        holder.profile.visibility = View.VISIBLE
                        holder.nickname.visibility = View.VISIBLE
                        holder.date.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    class MyMessageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.talk_date)
        val message: TextView = itemView.findViewById(R.id.talk_box)
        val time: TextView = itemView.findViewById(R.id.talk_time)
        val file: ImageView = itemView.findViewById(R.id.talk_file)
    }


    class TargetMessageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.talk_date)
        val nickname: TextView = itemView.findViewById(R.id.username)
        val profile: ImageView = itemView.findViewById(R.id.user_profile)
        val message: TextView = itemView.findViewById(R.id.talk_box)
        val time: TextView = itemView.findViewById(R.id.talk_time)
        val file: ImageView = itemView.findViewById(R.id.talk_file)
    }
}