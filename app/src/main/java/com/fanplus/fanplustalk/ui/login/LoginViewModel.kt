package com.fanplus.fanplustalk.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.fanplus.fanplustalk.data.login.LoginRepository

import com.fanplus.fanplustalk.R
import io.talkplus.TalkPlus
import io.talkplus.entity.user.TPUser
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(userIdx: String, username: String) {
        // can be launched in a separate asynchronous job
        loginRepository.login(userIdx, username, object : TalkPlus.CallbackListener<TPUser?> {
            override fun onSuccess(p0: TPUser?) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = username))
            }

            override fun onFailure(p0: Int, p1: Exception?) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }

        })
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }
}