package com.dicoding.picodiploma.submission3.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.submission3.db.FavDao
import com.dicoding.picodiploma.submission3.db.Favorite
import com.dicoding.picodiploma.submission3.db.UserDatabase

class FavViewModel(application: Application): AndroidViewModel(application) {
        private var userDao: FavDao? = null
        private var userDb : UserDatabase? = null

        init {
                userDb = UserDatabase.getDatabase(application)
                userDao = userDb?.favorite()
        }

        fun getFavoriteUser(): LiveData<List<Favorite>>? {
                return userDao?.getFavoriteUser()
        }
}