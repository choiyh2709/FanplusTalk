package com.fanplus.fanplustalk.ui.channel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fanplus.fanplustalk.data.channel.detail.ChannelDetailDataSource
import com.fanplus.fanplustalk.data.channel.detail.ChannelDetailRepository
import com.fanplus.fanplustalk.data.channel.list.ChannelListDataSource
import com.fanplus.fanplustalk.data.channel.list.ChannelListRepository
import com.fanplus.fanplustalk.data.login.LoginDataSource
import com.fanplus.fanplustalk.data.login.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ChannelDetailViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelDetailViewModel::class.java)) {
            return ChannelDetailViewModel(
                channelDetailRepository = ChannelDetailRepository(
                    dataSource = ChannelDetailDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}