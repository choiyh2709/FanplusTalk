package com.fanplus.fanplustalk.ui.channel.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fanplus.fanplustalk.data.channel.list.ChannelListRepository
import io.talkplus.TalkPlus
import io.talkplus.entity.channel.TPChannel
import java.lang.Exception

class ChannelListViewModel(private val channelListRepository: ChannelListRepository): ViewModel() {
    private val _channelList = MutableLiveData<List<TPChannel?>>()
    val channelList : LiveData<List<TPChannel?>> = _channelList

    init {
        getChannelList(null)
    }

    fun getChannelList(lastChannel: TPChannel?){
        channelListRepository.getChannelList(lastChannel, object : TalkPlus.CallbackListener<List<TPChannel?>?> {
            override fun onSuccess(p0: List<TPChannel?>?) {
                p0?.let {
                    _channelList.postValue(it)
                }
            }

            override fun onFailure(p0: Int, p1: Exception?) {
            }
        })
    }
}