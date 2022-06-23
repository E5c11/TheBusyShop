package com.ikhokha.techcheck.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.ikhokha.techcheck.data.datastore.GUEST_EMAIL
import com.ikhokha.techcheck.data.datastore.UserPreferences
import com.ikhokha.techcheck.repositories.LoginFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loginRepo: LoginFirebaseRepository,
    userPref: UserPreferences
): ViewModel() {

    private val loginPref = userPref.loginPref
    private val homeChannel = Channel<HomeEvents>()
    val homeEvent = homeChannel.receiveAsFlow()

    init {
        viewModelScope.launch(IO) {
            checkLogin()
            Log.d("myT", "home: viewmodel ")
        }
    }

    private fun checkLogin() = viewModelScope.launch(IO) {
        Log.d("myT", "checkLogin: ")
        loginPref.collect {
            if (it.email != GUEST_EMAIL) {
                try {
                    loginRepo.signInUser(it.email, it.password).let {
                        homeChannel.send(HomeEvents.LoggedInEvent)
                    }
                } catch (e: FirebaseAuthException) {
                    homeChannel.send(HomeEvents.NotLoggedInEvent)
                }
            } else homeChannel.send(HomeEvents.NotLoggedInEvent)
        }
    }

    sealed class HomeEvents {
        object NotLoggedInEvent: HomeEvents()
        object LoggedInEvent: HomeEvents()
    }

}