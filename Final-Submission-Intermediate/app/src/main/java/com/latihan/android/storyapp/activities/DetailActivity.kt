package com.latihan.android.storyapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.latihan.android.storyapp.R
import com.latihan.android.storyapp.databinding.ActivityDetailBinding
import com.latihan.android.storyapp.helper.withDateFormat

class DetailActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_PHOTO_IMAGE = "extra_photo"
        const val EXTRA_DATE = "extra_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Detail post"

        getDetailStory()
    }

    private fun getDetailStory() {
        val name = intent.getStringExtra(EXTRA_NAME)
        val photoImage = intent.getStringExtra(EXTRA_PHOTO_IMAGE)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val date = intent.getStringExtra(EXTRA_DATE)

        binding.apply {
            tvItemUsername.text = name
            Glide.with(this@DetailActivity)
                .load(photoImage)
                .into(imgItemPhoto)
            tvItemDescription.text = description
            tvItemDate.text = getString(R.string.dateFormat, date?.withDateFormat())
        }
    }
}