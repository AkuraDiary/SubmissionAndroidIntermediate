package com.asthiseta.submissionintermediate.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.asthiseta.submissionintermediate.data.local.entity.RemoteKeyEntity
import com.asthiseta.submissionintermediate.data.local.room.StoryDatabase
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.data.remote.ApiService
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator @Inject constructor(
    private val db: StoryDatabase,
    private val service: ApiService,
    private val pref: UserLoginPreferences
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
        }

        try {
            val token : String = pref.getLoginData().first().token
            val response = service.getAllStory("Bearer $token", page, state.config.pageSize).listStory

            val endPage = response.isEmpty()
            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.remoteKeyDao().deleteAllRemoteKey()
                    db.storyDao().deleteAllStory()
                }

                val previousKey = if(page == 1) null else page-1
                val nextKey = if(endPage) null else page+1

                val key = response.map {
                    RemoteKeyEntity(
                        id = it.id,
                        prevKey = previousKey,
                        nextKey = nextKey
                    )
                }

                db.remoteKeyDao().insertAllData(key)
                db.storyDao().insertStory(response)
            }
            return MediatorResult.Success(endOfPaginationReached = endPage)
        }catch (exception : Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeyEntity? {
        return state.pages.lastOrNull()?.data?.lastOrNull()?.id?.let {
            db.remoteKeyDao().getRemoteKeysId(it)
        }
    }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let {
            db.remoteKeyDao().getRemoteKeysId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.last()?.id?.let {
            db.remoteKeyDao().getRemoteKeysId(it)
        }
    }

}