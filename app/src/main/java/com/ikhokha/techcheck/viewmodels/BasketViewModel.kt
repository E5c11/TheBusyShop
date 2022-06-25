package com.ikhokha.techcheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ikhokha.techcheck.data.entities.Product
import com.ikhokha.techcheck.repositories.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val localRepo: LocalRepository
): ViewModel() {

    val products: LiveData<List<Product>> = localRepo.basketItems.asLiveData()

}