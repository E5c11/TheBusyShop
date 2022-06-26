package com.ikhokha.techcheck.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    private val _total: MutableLiveData<Double> = MutableLiveData()
    val total: LiveData<Double> = _total

    val products: LiveData<List<Product>> = Transformations.map(localRepo.basketItems.asLiveData()) { list ->
        var tempTotal: Double = 0.0
        list.forEach { tempTotal += it.price ?: 0.0}
        _total.postValue(tempTotal)
        list
    }

}