package com.asthiseta.submissionintermediate.ui.addStory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asthiseta.submissionintermediate.testUtilities.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
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
class UploadVMTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var uploadVM : UploadVM

    @Mock
    private lateinit var dummyMockFile : File

    @Test
    fun `when get message should return LiveData String`() {
        val expectedData = MutableLiveData<String>()
        expectedData.value = "dummyMessage"

        `when`(uploadVM.message).thenReturn(expectedData)

        val actualData = uploadVM.message.getOrAwaitValue()

        verify(uploadVM).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `verify uploadStory function is works by checking the message`() {
        val dummyToken = "dummyToken"
        val expectedData = MutableLiveData<String>()
        expectedData.value = "dummyListStory"

        uploadVM.uploadStory(
            dummyToken,
            dummyMockFile,
            "this is description"
        )

        verify(uploadVM).uploadStory(
            dummyToken,
            dummyMockFile,
            "this is description"
        )

        `when`(uploadVM.message).thenReturn(expectedData)

        val actualData = uploadVM.message.getOrAwaitValue()

        verify(uploadVM).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `verify uploadStoryWithLocation function is works by checking the message value`() {
        val dummyToken = "dummyToken"
        val expectedData = MutableLiveData<String>()
        expectedData.value = "dummyListStory"

        uploadVM.uploadStoryWithLocation(
            dummyToken,
            dummyMockFile,
            "this is description",
            42.069,
            69.420
        )

        verify(uploadVM).uploadStoryWithLocation(
            dummyToken,
            dummyMockFile,
            "this is description",
            42.069,
            69.420
        )

        `when`(uploadVM.message).thenReturn(expectedData)

        val actualData = uploadVM.message.getOrAwaitValue()

        verify(uploadVM).message
        assertThat(actualData).isEqualTo(expectedData.value)
    }
}