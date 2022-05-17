package com.latihan.android.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihan.android.storyapp.R
import com.latihan.android.storyapp.activities.DetailActivity
import com.latihan.android.storyapp.api.ListStoryItem
import com.latihan.android.storyapp.databinding.ItemRowStoryBinding
import com.latihan.android.storyapp.helper.withDateFormat

class ListStoryAdapter :
    PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_PHOTO_IMAGE = "extra_photo"
        const val EXTRA_DATE = "extra_date"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    class ListViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imgItemPhoto)
                tvItemUsername.text = data.name
                tvItemDescription.text = data.description
                tvItemDate.text = itemView.context.getString(
                    R.string.dateFormat,
                    data.createdAt.withDateFormat()
                )

                cardView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(EXTRA_NAME, data.name)
                    intent.putExtra(EXTRA_PHOTO_IMAGE, data.photoUrl)
                    intent.putExtra(EXTRA_DESCRIPTION, data.description)
                    intent.putExtra(EXTRA_DATE, data.createdAt)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val storyData = getItem(position)
        if (storyData != null) {
            holder.bind(storyData)
        }
    }
}