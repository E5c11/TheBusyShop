package com.ikhokha.techcheck.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuthException
import com.ikhokha.techcheck.data.datastore.GUEST_EMAIL
import com.ikhokha.techcheck.data.datastore.UserPreferences
import com.ikhokha.techcheck.data.entities.Product
import com.ikhokha.techcheck.repositories.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fbRepo: FirebaseRepository,
    userPref: UserPreferences
): ViewModel() {

    private val loginPref = userPref.loginPref
    private lateinit var _products: MutableLiveData<List<Product>>
    lateinit var products: LiveData<List<Product>>
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
                    fbRepo.signInUser(it.email, it.password).let {
                        fetchProducts()
                        homeChannel.send(HomeEvents.LoggedInEvent)
                    }
                } catch (e: FirebaseAuthException) {
                    homeChannel.send(HomeEvents.NotLoggedInEvent)
                }
            } else homeChannel.send(HomeEvents.NotLoggedInEvent)
        }
    }

    private fun fetchProducts() {
        products = fbRepo.getProducts().asLiveData()
    }

    sealed class HomeEvents {
        object NotLoggedInEvent: HomeEvents()
        object LoggedInEvent: HomeEvents()
    }

}