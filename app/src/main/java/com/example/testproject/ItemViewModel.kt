package com.example.testproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    private val mutableFragmentIndex = MutableLiveData<Int>()
    val fragmentIndex: LiveData<Int> get() = mutableFragmentIndex

    fun setFragmentIndex(fragmentIndex: Int) {
        mutableFragmentIndex.value = fragmentIndex
    }

}