package com.dicoding.picodiploma.submission3.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.submission3.SetPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SetPreferences) : ViewModel() {
    fun getTheme(): LiveData<Boolean> {
        return pref.getTheme().asLiveData()
    }

    fun saveTheme(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveTheme(isDarkModeActive)
        }
    }
}