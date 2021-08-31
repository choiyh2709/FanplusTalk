package com.fanplus.fanplustalk.data.login

import android.content.Intent
import com.fanplus.fanplustalk.data.model.LoggedInUser
import io.talkplus.TalkPlus
import io.talkplus.TalkPlus.CallbackListener
import io.talkplus.entity.user.TPUser
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(userIdx: String, username: String, callbackListener: CallbackListener<TPUser?>) {
        TalkPlus.loginWithAnonymous(
            userIdx,
            username,
            null as String?,
            null,
            object : CallbackListener<TPUser?> {
                override fun onSuccess(tpUser: TPUser?) {
                    callbackListener.onSuccess(tpUser)
                }

                override fun onFailure(errorCode: Int, e: Exception) {
                    callbackListener.onFailure(errorCode, e)
                }
            })
    }

    fun logout() {
    }
}