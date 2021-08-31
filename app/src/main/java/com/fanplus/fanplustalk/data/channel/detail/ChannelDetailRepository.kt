package com.fanplus.fanplustalk.data.channel.detail

import com.google.gson.JsonObject
import io.talkplus.TalkPlus
import io.talkplus.entity.channel.TPChannel
import io.talkplus.entity.channel.TPMessage
import java.io.File

class ChannelDetailRepository(val dataSource: ChannelDetailDataSource) {
    fun getMessageList(
        channel: TPChannel?,
        lastMessage: TPMessage?,
        callbackListener: TalkPlus.CallbackListener<List<TPMessage?>?>
    ) {
        dataSource.getMessageList(channel, lastMessage, callbackListener)
    }

    fun markAsReadChannel(
        channel: TPChannel?,
        callbackListener: TalkPlus.CallbackListener<TPChannel?>
    ) {
        dataSource.markAsReadChannel(channel, callbackListener)
    }

    fun sendMessage(
        channel: TPChannel?,
        text: String,
        type: String,
        metadata: JsonObject?,
        callbackListener: TalkPlus.CallbackListener<TPMessage>
    ) {
        dataSource.sendMessage(channel, text, type, metadata, callbackListener)
    }

    fun sendFile(
        channel: TPChannel?,
        text: String?,
        type: String,
        metadata: JsonObject?,
        file: File,
        callbackListener: TalkPlus.CallbackListener<TPMessage>
    ) {
        dataSource.sendFile(channel, text, type, metadata, file, callbackListener)
    }
}