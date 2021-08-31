package com.fanplus.fanplustalk.ui.channel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fanplus.fanplustalk.data.channel.list.ChannelListDataSource
import com.fanplus.fanplustalk.data.channel.list.ChannelListRepository
import com.fanplus.fanplustalk.data.login.LoginDataSource
import com.fanplus.fanplustalk.data.login.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ChannelListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelListViewModel::class.java)) {
            return ChannelListViewModel(
                channelListRepository = ChannelListRepository(
                    dataSource = ChannelListDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}