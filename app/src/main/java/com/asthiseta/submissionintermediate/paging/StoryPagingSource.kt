package com.asthiseta.submissionintermediate.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.data.remote.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val pref: UserLoginPreferences,
    private val service: ApiService
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: 1
            val usrToken = pref.getLoginData().first().token
            val response = service.getAllStory(
                "Bearer $usrToken",
                page,
                params.loadSize
            )
            LoadResult.Page(
                data = response.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.listStory.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}