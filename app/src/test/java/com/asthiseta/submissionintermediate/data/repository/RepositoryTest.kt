@file:OptIn(ExperimentalCoroutinesApi::class)

package com.asthiseta.submissionintermediate.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResult
import com.asthiseta.submissionintermediate.data.model.stories.Story
import com.asthiseta.submissionintermediate.testUtilities.DummyData
import com.asthiseta.submissionintermediate.testUtilities.MainCoroutineRule
import com.asthiseta.submissionintermediate.testUtilities.PagedTestDataSources
import com.asthiseta.submissionintermediate.testUtilities.PagedTestDataSources.Companion.listUpdateCallback
import com.asthiseta.submissionintermediate.testUtilities.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var dummyMockFile: File


    @Test
    fun `when get userLogin should return LiveData UserLoginResult`() {
        val expectedData = MutableLiveData<UserLoginResult>()
        val dummyUserResult = UserLoginResult(
            "dummyUser",
            "dummyPassword",
            "dummyToken"
        )
        expectedData.value = dummyUserResult

        `when`(repository.userLogin).thenReturn(expectedData)

        val actualData = repository.userLogin.getOrAwaitValue()

        verify(repository).userLogin
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get message should return LiveData String`() {
        val expectedData = MutableLiveData<String>()
        val dummyMessage = "dummyMessage"
        expectedData.value = dummyMessage

        `when`(repository.message).thenReturn(expectedData)

        val actualData = repository.message.getOrAwaitValue()

        verify(repository).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get isLoading should return LiveData Boolean`() {
        val expectedData = MutableLiveData<Boolean>()
        val dummyIsLoading = true
        expectedData.value = dummyIsLoading

        `when`(repository.isLoading).thenReturn(expectedData)

        val actualData = repository.isLoading.getOrAwaitValue()

        verify(repository).isLoading
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get listStory should return LiveData List Story`() {
        val dummyListStory = DummyData.generateDummyStoryResponseData()
        val expectedData = MutableLiveData<List<Story>>()
        expectedData.value = dummyListStory

        `when`(repository.storyList).thenReturn(expectedData)

        val actualData = repository.storyList.getOrAwaitValue()

        verify(repository).storyList
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `verify getStoriesData should not Null`() = runTest {
        val dummyListStory = DummyData.generateDummyStoryResponseData()
        val storiesData = PagedTestDataSources.itemSnapshot(dummyListStory)

        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = storiesData

        `when`(repository.getStoriesData()).thenReturn(stories)

        val actualData = repository.getStoriesData().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryCallback,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher
        )

        differ.submitData(actualData)
        advanceUntilIdle()

        verify(repository).getStoriesData()

        assertThat(actualData).isNotNull()

        assertThat(differ.snapshot()).isNotNull()
        assertThat(dummyListStory.size).isEqualTo(differ.snapshot().size)
        assertThat(dummyListStory[0].name).isEqualTo(differ.snapshot()[0]?.name)
    }

    @Test
    fun `verify getStoriesData with location is working`() {
        val dummyToken = "dummyToken"
        val dummyStoriesList = DummyData.generateDummyStoryResponseData()
        val expectedData = MutableLiveData<List<Story>>()
        expectedData.value = dummyStoriesList

        repository.getStoriesWithLocation(dummyToken)

        verify(repository).getStoriesWithLocation(dummyToken)

        `when`(repository.storyList).thenReturn(expectedData)

        val actualData = repository.storyList.getOrAwaitValue()

        verify(repository).storyList

        assertThat(actualData).isEqualTo(expectedData.value)
        assertThat(actualData[0].lon).isNotNull()
        assertThat(actualData[0].lat).isNotNull()
    }

    @Test
    fun `verify uploadStory function is worked by checking the message response`() {
        val dummyToken = "dummyToken"
        val expectedData = MutableLiveData<String>()
        expectedData.value = "dummyListStory"

        repository.uploadStory(
            dummyToken,
            dummyMockFile,
            "this is description"
        )

        verify(repository).uploadStory(
            dummyToken,
            dummyMockFile,
            "this is description"
        )

        `when`(repository.message).thenReturn(expectedData)

        val actualData = repository.message.getOrAwaitValue()

        verify(repository).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `verify uploadStoryWithLocation function is worked by checking the message response`() {
        val dummyToken = "dummyToken"
        val expectedData = MutableLiveData<String>()
        expectedData.value = "dummyListStory"

        repository.uploadStoryWithLocation(
            dummyToken,
            dummyMockFile,
            "this is description",
            42.069F,
            69.420F
        )

        verify(repository).uploadStoryWithLocation(
            dummyToken,
            dummyMockFile,
            "this is description",
            42.069F,
            69.420F
        )

        `when`(repository.message).thenReturn(expectedData)

        val actualData = repository.message.getOrAwaitValue()

        verify(repository).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `verifiy doLoginUser function is works by check the return value should be UserLoginResult`() {
        val dummyEmail = "someone@gmail.com"
        val dummyPassword = "someonePassHere"
        val dummyLoginResult = UserLoginResult("someone@gmail.com", "someonePassHere", "someId")

        val expectedData = MutableLiveData<UserLoginResult>()
        expectedData.value = dummyLoginResult

        repository.doLoginUser(dummyEmail, dummyPassword)
        verify(repository).doLoginUser(dummyEmail, dummyPassword)

        `when`(repository.userLogin).thenReturn(expectedData)

        val actualData = repository.userLogin.getOrAwaitValue()

        verify(repository).userLogin
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `verify doRegisterUser function is works by check the isLoading value when its called`() {
        val dummyName = "someone"
        val dummyEmail = "someone@gmail.com"
        val dummyPassword = "someonePassHere"
        val expectedData = MutableLiveData<Boolean>()
        expectedData.value = true

        repository.doRegisterUser(dummyName, dummyEmail, dummyPassword)

        verify(repository).doRegisterUser(dummyName, dummyEmail, dummyPassword)

        `when`(repository.isLoading).thenReturn(expectedData)

        val actualDAta = repository.isLoading.getOrAwaitValue()

        verify(repository).isLoading
        assertThat(actualDAta).isEqualTo(expectedData.value)
    }


}