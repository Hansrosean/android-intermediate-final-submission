package com.latihan.android.storyapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.android.storyapp.R
import com.latihan.android.storyapp.adapter.ListStoryAdapter
import com.latihan.android.storyapp.adapter.LoadingStateAdapter
import com.latihan.android.storyapp.databinding.ActivityMainBinding
import com.latihan.android.storyapp.viewmodels.DataStoreViewModel
import com.latihan.android.storyapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listStoryAdapter: ListStoryAdapter

    private val mainVIewModel by viewModels<MainViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    companion object {
        private const val TAG = "MainActivity"
        const val mToken = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listStoryAdapter = ListStoryAdapter()

        setupViewModel()
        showProgressBar(false)

        dataStoreViewModel.getUser().observe(this) { user ->
            val token = user.token
            Log.d(mToken, token)
            showStoryList()
            
            if (user.isLogin) {
                supportActionBar?.title = getString(R.string.greeting, user.name)
            }
        }

        binding.fabCreateStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateStoryActivity::class.java))
        }
    }

    private fun showStoryList() {
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = listStoryAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    listStoryAdapter.retry()
                }
            )
        }
    }

    private fun setupViewModel() {
        dataStoreViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                supportActionBar?.title = getString(R.string.greeting, user.name)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            mainVIewModel.getStory().observe(this) {
                listStoryAdapter.submitData(lifecycle, it)
            }
        }

        mainVIewModel.getStory().observe(this) {
            listStoryAdapter.submitData(lifecycle, it)
        }

        mainVIewModel.isLoading.observe(this) { showProgressBar(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.item_logout -> {
                dataStoreViewModel.logout()
                Log.d(TAG, "Login to continue")
                finish()
                true
            }
            R.id.item_maps -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}