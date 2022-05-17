package com.latihan.android.storyapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.latihan.android.storyapp.api.ListStoryItem
import com.latihan.android.storyapp.preference.UserPreferences
import com.latihan.android.storyapp.retrofit.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val preferences: UserPreferences,
    private val service: ApiService
) : PagingSource<Int, ListStoryItem>() {

    companion object {
        private const val PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val loadPage = params.key ?: PAGE_INDEX
            val token: String = preferences.getUser().first().token
            val data = service.getStory("Bearer $token", loadPage, params.loadSize)

            LoadResult.Page(
                data = data.listStoryItem,
                prevKey = if (loadPage == 1) null else loadPage - 1,
                nextKey = if (data.listStoryItem.isNullOrEmpty()) null else loadPage + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}