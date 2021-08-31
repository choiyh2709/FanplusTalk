package com.fanplus.fanplustalk.data.channel.list

import io.talkplus.entity.channel.TPChannel

import io.talkplus.TalkPlus
import io.talkplus.TalkPlus.CallbackListener
import java.lang.Exception


class ChannelListDataSource {
    fun getChannelList(lastChannel: TPChannel?, callbackListener: CallbackListener<List<TPChannel?>?>){
        TalkPlus.getChannelList(lastChannel, object : CallbackListener<List<TPChannel?>?> {
            override fun onSuccess(tpChannels: List<TPChannel?>?) {
                callbackListener.onSuccess(tpChannels)
            }
            override fun onFailure(i: Int, e: Exception) {
                callbackListener.onFailure(i, e)
            }
        })
    }
}