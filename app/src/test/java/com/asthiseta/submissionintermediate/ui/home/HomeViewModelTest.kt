package com.asthiseta.submissionintermediate.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.testUtilities.DummyData
import com.asthiseta.submissionintermediate.testUtilities.MainCoroutineRule
import com.asthiseta.submissionintermediate.testUtilities.PagedTestDataSources
import com.asthiseta.submissionintermediate.testUtilities.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `when get isLoading from view model should return LiveData Boolean`() {
        val expectedData = MutableLiveData<Boolean>()
        val dummyIsLoading = true
        expectedData.value = dummyIsLoading

        `when`(homeViewModel.isLoading).thenReturn(expectedData)

        val actualData = homeViewModel.isLoading.getOrAwaitValue()

        verify(homeViewModel).isLoading
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get message from view model should return LiveData Boolean`() {
        val expectedData = MutableLiveData<String>()
        val dummyMessage = "dummyMessage"
        expectedData.value = dummyMessage

        `when`(homeViewModel.message).thenReturn(expectedData)

        val actualData = homeViewModel.message.getOrAwaitValue()

        verify(homeViewModel).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get stories should not Null`() = runTest{
        val dummyListStory = DummyData.generateDummyStoryResponseData()
        val storiesData = PagedTestDataSources.itemSnapshot(dummyListStory)

        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = storiesData

        `when`(homeViewModel.stories).thenReturn(stories)

        val actualData = homeViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryCallback,
            updateCallback = PagedTestDataSources.listUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher
        )

        differ.submitData(actualData)
        advanceUntilIdle()

        verify(homeViewModel).stories

        assertThat(actualData).isNotNull()

        assertThat(differ.snapshot()).isNotNull()
        assertThat(dummyListStory.size).isEqualTo(differ.snapshot().size)
        assertThat(dummyListStory[0].name).isEqualTo(differ.snapshot()[0]?.name)
    }
}