package com.fanplus.fanplustalk.ui.channel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanplus.fanplustalk.data.channel.detail.ChannelDetailRepository
import com.google.gson.JsonObject
import io.talkplus.TalkPlus
import io.talkplus.entity.channel.TPChannel
import io.talkplus.entity.channel.TPMessage
import java.io.File

class ChannelDetailViewModel(private val channelDetailRepository: ChannelDetailRepository): ViewModel() {
    private val _messageList = MutableLiveData<List<TPMessage?>>()
    val messageList : LiveData<List<TPMessage?>> = _messageList

    private val _message = MutableLiveData<TPMessage>()
    val message : LiveData<TPMessage> = _message

    var canMoreLoading = true
    private val _failToast = MutableLiveData<String>()
    val failToast : LiveData<String> = _failToast

    fun getMessageList(channel: TPChannel?, lastMessage: TPMessage?){
        channelDetailRepository.getMessageList(channel, lastMessage, object : TalkPlus.CallbackListener<List<TPMessage?>?> {
            override fun onSuccess(p0: List<TPMessage?>?) {
                p0?.let {
                    canMoreLoading = it.size == 20
                    _messageList.postValue(it)
                }
            }

            override fun onFailure(p0: Int, p1: Exception?) {
                _failToast.postValue("$p0 : ${p1?.message}")
            }
        })
    }

    fun markAsReadChannel(channel: TPChannel?) {
        channelDetailRepository.markAsReadChannel(channel, object : TalkPlus.CallbackListener<TPChannel?>{
            override fun onSuccess(p0: TPChannel?) {}

            override fun onFailure(p0: Int, p1: java.lang.Exception?) {}
        })
    }

    fun sendMessage(channel: TPChannel?, text: String, type: String, metadata: JsonObject?){
        channelDetailRepository.sendMessage(channel, text, type, metadata, object : TalkPlus.CallbackListener<TPMessage>{
            override fun onSuccess(p0: TPMessage?) {
                p0?.let {
                    _message.postValue(it) }
            }

            override fun onFailure(p0: Int, p1: java.lang.Exception?) {
                _failToast.postValue("$p0 : ${p1?.message}")
            }

        })
    }

    fun sendFile(channel: TPChannel?, text: String?, type: String, file: File, metadata: JsonObject?){
        channelDetailRepository.sendFile(channel, text, type, metadata, file, object : TalkPlus.CallbackListener<TPMessage>{
            override fun onSuccess(p0: TPMessage?) {
                p0?.let {
                    _message.postValue(it) }
            }

            override fun onFailure(p0: Int, p1: java.lang.Exception?) {
                _failToast.postValue("$p0 : ${p1?.message}")
            }

        })
    }
}