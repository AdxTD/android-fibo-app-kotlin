package com.syncvr.fiboapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.syncvr.fiboapp.FiboApplication

class FiboRequestsViewModelFactory (
    private val application: FiboApplication
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FiboRequestsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FiboRequestsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}