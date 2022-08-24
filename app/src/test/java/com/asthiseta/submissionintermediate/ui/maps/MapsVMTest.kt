package com.asthiseta.submissionintermediate.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.testUtilities.DummyData
import com.asthiseta.submissionintermediate.testUtilities.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsVMTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mapsVM: MapsVM

    @Test
    fun `when get storyList should return LiveData List Story`() {
        val dummyListStory = DummyData.generateDummyStoryResponseData()
        val expectedData = MutableLiveData<List<Story>>()
        expectedData.value =dummyListStory

        `when`(mapsVM.storyList).thenReturn(expectedData)

        val actualData = mapsVM.storyList.getOrAwaitValue()

        verify(mapsVM).storyList
        assertThat(actualData).isEqualTo(expectedData.value)

    }

    @Test
    fun `verify getStoriesWithLocations function in viewmodel works`() {
        val dummyToken = "dummyToken"
        val dummyStoriesList = DummyData.generateDummyStoryResponseData()
        val expectedData = MutableLiveData<List<Story>>()
        expectedData.value = dummyStoriesList

        mapsVM.getStoriesWithLocations(dummyToken)

        verify(mapsVM).getStoriesWithLocations(dummyToken)

        `when`(mapsVM.storyList).thenReturn(expectedData)

        val actualData = mapsVM.storyList.getOrAwaitValue()

        verify(mapsVM).storyList

        assertThat(actualData).isEqualTo(expectedData.value)
        assertThat(actualData[0].lon).isNotNull()
        assertThat(actualData[0].lat).isNotNull()
    }
}