package com.fanplus.fanplustalk.data.channel.list

import io.talkplus.TalkPlus
import io.talkplus.entity.channel.TPChannel

class ChannelListRepository(val dataSource: ChannelListDataSource) {
    fun getChannelList(lastChannel: TPChannel?, callbackListener: TalkPlus.CallbackListener<List<TPChannel?>?>){
        dataSource.getChannelList(lastChannel, callbackListener)
    }
}