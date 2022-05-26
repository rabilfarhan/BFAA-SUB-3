package com.dicoding.picodiploma.submission3.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission3.adapter.ListUserAdapter
import com.dicoding.picodiploma.submission3.api.UserResponseItem
import com.dicoding.picodiploma.submission3.data.User
import com.dicoding.submission2.api.ApiConfig
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.dicoding.picodiploma.submission3.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {


    companion object {
        const val EXTRA_USERNAME = "username"
    }

    private lateinit var binding: FragmentFollowBinding
    private val adapter = ListUserAdapter(arrayListOf())
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setListener()

        username = arguments?.getString(EXTRA_USERNAME)
        SearchUsersFollowers(username ?: "")
    }

    private fun setAdapter() {
        with(binding.rvFollow) {
            this@FollowFragment.adapter.setOnItemClickCallback(object :
                ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    startActivity(Intent(requireContext(), DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USER, user)
                    })
                }

            })
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@FollowFragment.adapter
        }
    }

    private fun setListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        SearchUsersFollowers(username ?: "")
                    }
                    1 -> {
                        SearchUsersFollowing(username ?: "")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })
    }

    private fun SearchUsersFollowers(username: String) {
        binding.progressBar.isVisible = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UserResponseItem>> {
            override fun onResponse(
                call: Call<List<UserResponseItem>>,
                response: Response<List<UserResponseItem>>
            ) {
                val responseBody = response.body();
                if (responseBody != null) {
                    val dataUser = responseBody.map { user ->
                        User(
                            user.id,
                            user.login,
                            user.avatarUrl,
                            "",
                            "",
                            "",
                            user.publicRepos.toString(),
                            user.followers.toString(),
                            user.following.toString()
                        )
                    }

                    adapter.setFilterUser(dataUser)
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                binding.progressBar.isVisible = false
            }
        })
    }

    private fun SearchUsersFollowing(username: String) {
        binding.progressBar.isVisible = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UserResponseItem>> {
            override fun onResponse(
                call: Call<List<UserResponseItem>>,
                response: Response<List<UserResponseItem>>
            ) {
                val responseBody = response.body();
                if (responseBody != null) {
                    val dataUser = responseBody.map { user ->
                        User(
                            user.id,
                            user.login,
                            user.avatarUrl,
                            "",
                            "",
                            "",
                            user.publicRepos.toString(),
                            user.followers.toString(),
                            user.following.toString()
                        )
                    }

                    adapter.setFilterUser(dataUser)
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                binding.progressBar.isVisible = false
            }
        })
    }

}