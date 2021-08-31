package com.fanplus.fanplustalk.ui.channel.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.request.transition.Transition
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.fanplus.fanplustalk.databinding.ActivityChannelDetailBinding
import com.fanplus.fanplustalk.ui.login.LoginActivity
import io.talkplus.TalkPlus
import io.talkplus.entity.channel.TPChannel
import io.talkplus.entity.channel.TPMessage
import io.talkplus.entity.user.TPUser
import io.talkplus.util.CommonUtil
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.math.log

class ChannelDetailActivity : AppCompatActivity(), TalkPlus.ChannelListener {
    private lateinit var binding: ActivityChannelDetailBinding
    private lateinit var viewModel: ChannelDetailViewModel
    private lateinit var channel: TPChannel
    private lateinit var channelName: String
    private lateinit var listView: RecyclerView
    private lateinit var editText: EditText
    private val adapter = ChannelMessageAdapter()
    private var isMoreLoading = false
    private val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.data?.let { uri->
            if (uri.toString().contains("gif")){
                Thread {
                    val file = Glide.with(this).downloadOnly().load(uri).submit().get()
                    viewModel.sendFile(channel, "사진을 보냈습니다.", TPMessage.TYPE_TEXT, file, null)
                }.start()
            }else{
                cropRequestActivity.launch(
                    options(uri) {
                        setGuidelines(CropImageView.Guidelines.ON)
                    }
                )
            }
        }
    }

    private val cropRequestActivity = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriFilePath = result.getUriFilePath(this)
            uriFilePath?.let {
                val file = File(it)
                viewModel.sendFile(channel, "사진을 보냈습니다.", TPMessage.TYPE_TEXT, file, null)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        channel = intent.getSerializableExtra("channel") as TPChannel
        channelName = intent.getStringExtra("channel_name")!!

        viewModel = ViewModelProvider(this, ChannelDetailViewModelFactory())
            .get(ChannelDetailViewModel::class.java)
        binding = ActivityChannelDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TalkPlus.addChannelListener("channel_listener",this)

        val title = binding.chattingTitle
        val postMessage = binding.postMessage
        val postFile = binding.postCamera
        val backBtn = binding.backArrow
        listView = binding.chattingList
        editText = binding.editMessage

        viewModel.messageList.observe(this, Observer {
            if (it.isNullOrEmpty()) return@Observer
            if (isMoreLoading.not()){
                adapter.addItemList(it.reversed())
                adapter.notifyDataSetChanged()
                scrollToBottom()
            }else {
                adapter.moreAddItemList(it.reversed())
                adapter.notifyItemRangeInserted(0, it.size)
                listView.scrollToPosition(it.size.minus(1))
            }
        })

        viewModel.message.observe(this, Observer {
            adapter.addItem(it)
            adapter.notifyItemInserted(adapter.itemCount.minus(1))
            adapter.notifyItemChanged(adapter.itemCount.minus(2))
            scrollToBottom()
        })

        viewModel.failToast.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        title.text = channelName
        adapter.setUserIdx(CommonUtil.getProperty(this, LoginActivity.KEY_USER_ID, "1"))
        adapter.setChannel(channel)
        val manger = LinearLayoutManager(this)
        manger.stackFromEnd = true //키보드 올라왔을 때 recyclerView 최하단으로 이동
        listView.layoutManager = manger
        listView.adapter = adapter
        listView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val topVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
                if (topVisibleItemPosition == 0 && viewModel.canMoreLoading) {
                    isMoreLoading = true
                    viewModel.getMessageList(channel, viewModel.messageList.value?.last())
                }


            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (editText.isFocused) {
                    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        editText.windowToken,
                        0
                    )
                }
            }
        })

        viewModel.getMessageList(channel, null)
        viewModel.markAsReadChannel(channel)

        postMessage.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                viewModel.sendMessage(
                    channel,
                    editText.text.toString(),
                    TPMessage.TYPE_TEXT,
                    null
                )
                editText.text = null
            }
        }

        postFile.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            requestActivity.launch(intent)
        }

        backBtn.setOnClickListener { finish() }
    }

    private fun scrollToBottom(){
        listView.scrollToPosition(adapter.itemCount.minus(1))
    }

    override fun onMemberAdded(p0: TPChannel?, p1: MutableList<TPUser>?) {

    }

    override fun onMemberLeft(p0: TPChannel?, p1: MutableList<TPUser>?) {
    }

    override fun onMessageReceived(p0: TPChannel?, p1: TPMessage?) {
        p1?.let {
            adapter.addItem(it)
            adapter.notifyItemInserted(adapter.itemCount.minus(1))
            adapter.notifyItemChanged(adapter.itemCount.minus(2))
            scrollToBottom()
        }
    }

    override fun onChannelAdded(p0: TPChannel?) {
    }

    override fun onChannelChanged(p0: TPChannel?) {
    }

    override fun onChannelRemoved(p0: TPChannel?) {
    }
}