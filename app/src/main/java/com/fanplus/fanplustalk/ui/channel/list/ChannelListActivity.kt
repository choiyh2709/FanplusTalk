package com.fanplus.fanplustalk.ui.channel.list

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.fanplus.fanplustalk.databinding.ActivityChannelListBinding
import com.fanplus.fanplustalk.ui.channel.detail.ChannelDetailActivity
import com.fanplus.fanplustalk.ui.login.LoginActivity
import io.talkplus.entity.channel.TPChannel
import io.talkplus.entity.channel.TPMessage
import io.talkplus.util.CommonUtil
import java.io.File

class ChannelListActivity : AppCompatActivity(), ChannelListAdapter.ChannelItemListener {
    private lateinit var channelListViewModel: ChannelListViewModel
    private lateinit var binding: ActivityChannelListBinding
    private val adapter = ChannelListAdapter(this)

    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        channelListViewModel.getChannelList(null)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        channelListViewModel = ViewModelProvider(this, ChannelListViewModelFactory())
            .get(ChannelListViewModel::class.java)
        binding = ActivityChannelListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val channelList = binding.channelList
        val channelRefresh = binding.channelRefresh
        val backBtn = binding.backArrow

        adapter.setUserIdx(CommonUtil.getProperty(this, LoginActivity.KEY_USER_ID, "1"))

        channelListViewModel.channelList.observe(this, Observer {
            if (it.isNullOrEmpty()) return@Observer
            adapter.addItem(it)
            adapter.notifyDataSetChanged()
            channelRefresh.isRefreshing = false
        })

        channelList.adapter = adapter

        channelRefresh.setOnRefreshListener {
            channelListViewModel.getChannelList(null)
        }

        backBtn.setOnClickListener { finish() }
    }

    override fun channelItemClick(channel: TPChannel, title: String) {
        val intent = Intent(this, ChannelDetailActivity::class.java)
        intent.putExtra("channel", channel)
        intent.putExtra("channel_name", title)
        requestActivity.launch(intent)
    }
}