package com.dicoding.picodiploma.submission3.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.submission3.db.FavDao
import com.dicoding.picodiploma.submission3.db.Favorite
import com.dicoding.picodiploma.submission3.db.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): ViewModel() {

    private var userDao: FavDao? = null
    private var userDb: UserDatabase? = null

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favorite()
    }

    fun insert(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavorite.value != null && isFavorite.value == true) {
                userDao?.removeFavorite(favorite.id)
            } else {
                userDao?.addFavorite(favorite)
            }
            isFavorite(favorite.id)
        }
    }

    fun isFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userDao?.checkUser(id)
            if (result != null) {
                _isFavorite.postValue(result > 0)
            }
        }
    }

}