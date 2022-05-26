package com.dicoding.picodiploma.submission3.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.submission3.R
import com.dicoding.picodiploma.submission3.SetPreferences
import com.dicoding.picodiploma.submission3.adapter.ListUserAdapter
import com.dicoding.picodiploma.submission3.api.UserResponse
import com.dicoding.picodiploma.submission3.api.UserResponseItem
import com.dicoding.picodiploma.submission3.data.User
import com.dicoding.picodiploma.submission3.databinding.ActivityMainBinding
import com.dicoding.picodiploma.submission3.ui.DetailUserActivity.Companion.EXTRA_USER
import com.dicoding.picodiploma.submission3.viewmodels.MainViewModel
import com.dicoding.submission2.api.ApiConfig
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private var list = ArrayList<User>()
    private lateinit var adapter : ListUserAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var preferences: SetPreferences
    private lateinit var mainViewModel: MainViewModel
    private var isDarkMode = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = SetPreferences.getInstance(dataStore)
        mainViewModel = MainViewModel(preferences)

        mainViewModel.getTheme().observe(this, {
            isDarkMode = it
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        binding.rvUsers.setHasFixedSize(true)
        GithubUsers()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) GithubUsers()
                else searchGithubUsers(query)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.isEmpty()) GithubUsers()
                else searchGithubUsers(query)
                return false
            }
        })

        val change =
            menu.findItem(R.id.mySwitch)?.actionView?.findViewById<SwitchMaterial>(R.id.switch_material)
        change?.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.saveTheme(isChecked)
        }
        change?.isChecked = isDarkMode
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite) {
            startActivity(Intent(this, FavActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun GithubUsers() {
        showsLoading(true)
        val client = ApiConfig.getApiService().getUsers()
        client.enqueue(object : Callback<List<UserResponseItem>> {
            override fun onResponse(
                call: Call<List<UserResponseItem>>,
                response: Response<List<UserResponseItem>>
            ) {
                showsLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (!responseBody.isNullOrEmpty()) {
                        setUsersData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponseItem>>, t: Throwable) {
                showsLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUsersData(consumerReviews: List<UserResponseItem>) {
        val listUser = arrayListOf<User>()
        consumerReviews.forEach {
            val user = User(
                it.id,
                it.login,
                it.avatarUrl,
                "",
                "",
                "",
                it.publicRepos.toString(),
                it.followers.toString(),
                it.following.toString()
            )
            if (!list.contains(user)) {
                list.add(user)
            }
            listUser.add(user)
        }

        binding.userNotFound.isVisible = listUser.isEmpty()

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = ListUserAdapter(listUser)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                startActivity(
                    Intent(this@MainActivity, DetailUserActivity::class.java).putExtra(
                        EXTRA_USER,
                        user
                    )
                )
            }
        }
        )
    }

    private fun searchGithubUsers(data: String) {
        showsLoading(true)
        val client = ApiConfig.getApiService().getSearchUser(data)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                showsLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUsersData(responseBody.userResponse)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showsLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showsLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}