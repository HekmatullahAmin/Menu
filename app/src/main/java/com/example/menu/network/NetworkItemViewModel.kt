package com.example.menu.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val ACCESS_KEY = "xIke4J1ztLAGJPVXdCJVXIGosJyS4v_SOqexpCsoHyc"

enum class UnsplashApiStatus {
    LOADING, SUCCESS, ERROR
}

class NetworkItemViewModel : ViewModel() {

    private var _response: MutableLiveData<ApiResponse> = MutableLiveData()
    private val response: LiveData<ApiResponse> get() = _response

    private var _items: MutableLiveData<List<Item>> = MutableLiveData()
    val items: LiveData<List<Item>> get() = _items

    private var _status: MutableLiveData<UnsplashApiStatus> = MutableLiveData()
    val status: LiveData<UnsplashApiStatus> get() = _status

    fun fetchItem(query: String) {
        viewModelScope.launch {
            _status.value = UnsplashApiStatus.LOADING
            try {
                _response.value = UnsplashApi.service.searchItem(1, query = query, ACCESS_KEY)
                response.observeForever { response ->
                    _items.value = response.results
                }
                _status.value = UnsplashApiStatus.SUCCESS
            } catch (e: Exception) {
                _status.value = UnsplashApiStatus.ERROR
                _items.value = listOf()
            }
        }
    }
}