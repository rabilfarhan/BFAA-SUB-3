package com.dicoding.picodiploma.submission3.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission3.adapter.ListUserAdapter
import com.dicoding.picodiploma.submission3.data.User
import com.dicoding.picodiploma.submission3.databinding.ActivityFavBinding
import com.dicoding.picodiploma.submission3.viewmodels.FavViewModel

class FavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavBinding
    private lateinit var viewModel: FavViewModel
    private var adapter = ListUserAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = FavViewModel(application)

        val actionbar = supportActionBar
        actionbar!!.title = "Favorite"

        setAdapter()

        viewModel.getFavoriteUser()?.observe(this, {
            val dataUser = it.map { favUser ->
                User(
                    id = favUser.id,
                    username = favUser.username,
                    avatar = favUser.avatar,
                    company = "",
                    location = "",
                    name = favUser.username,
                    repository = favUser.repository,
                    followers = favUser.followers,
                    following = favUser.following
                )
            }
            adapter.setFilterUser(dataUser)
            binding.emptyList.isVisible = dataUser.isNullOrEmpty()
        })
    }

    private fun setAdapter() {
        with(binding.rvFavorite) {
            this@FavActivity.adapter.setOnItemClickCallback(object :
                ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    startActivity(
                        Intent(this@FavActivity, DetailUserActivity::class.java).putExtra(
                            DetailUserActivity.EXTRA_USER,
                            user
                        )
                    )
                }

            })
            layoutManager =
                LinearLayoutManager(this@FavActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@FavActivity.adapter
        }
    }
}