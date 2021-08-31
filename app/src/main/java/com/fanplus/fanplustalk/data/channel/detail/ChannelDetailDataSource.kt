package com.fanplus.fanplustalk.data.channel.detail

import com.google.gson.JsonObject
import io.talkplus.entity.channel.TPChannel

import io.talkplus.TalkPlus
import io.talkplus.TalkPlus.CallbackListener
import io.talkplus.entity.channel.TPMessage
import java.io.File
import java.lang.Exception


class ChannelDetailDataSource {

    fun getMessageList(
        channel: TPChannel?,
        lastMessage: TPMessage?,
        callbackListener: CallbackListener<List<TPMessage?>?>
    ) {
        TalkPlus.getMessageList(channel, lastMessage, object : CallbackListener<List<TPMessage?>?> {
            override fun onFailure(i: Int, e: Exception) {
                callbackListener.onFailure(i, e)
            }

            override fun onSuccess(p0: List<TPMessage?>?) {
                callbackListener.onSuccess(p0)
            }
        })
    }

    fun markAsReadChannel(channel: TPChannel?, callbackListener: CallbackListener<TPChannel?>) {
        TalkPlus.markAsReadChannel(channel, object : CallbackListener<TPChannel?> {
            override fun onSuccess(tpChannel: TPChannel?) {
                callbackListener.onSuccess(tpChannel)
            }

            override fun onFailure(i: Int, e: Exception) {
                callbackListener.onFailure(i, e)
            }
        })
    }

    fun sendMessage(
        channel: TPChannel?,
        text: String,
        type: String,
        metadata: JsonObject?,
        callbackListener: CallbackListener<TPMessage>
    ) {
        TalkPlus.sendMessage(channel, text, type, metadata, object : CallbackListener<TPMessage> {
            override fun onSuccess(p0: TPMessage?) {
                callbackListener.onSuccess(p0)
            }

            override fun onFailure(p0: Int, p1: Exception?) {
                callbackListener.onFailure(p0, p1)
            }

        })
    }

    fun sendFile(
        channel: TPChannel?,
        text: String?,
        type: String,
        metadata: JsonObject?,
        file: File,
        callbackListener: CallbackListener<TPMessage>
    ) {
        TalkPlus.sendFileMessage(channel, text, type, metadata, file, object : CallbackListener<TPMessage> {
            override fun onSuccess(p0: TPMessage?) {
                callbackListener.onSuccess(p0)
            }

            override fun onFailure(p0: Int, p1: Exception?) {
                callbackListener.onFailure(p0, p1)
            }

        })
    }
}