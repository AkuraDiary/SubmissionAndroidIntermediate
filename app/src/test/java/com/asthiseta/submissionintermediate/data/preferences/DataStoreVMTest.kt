package com.asthiseta.submissionintermediate.data.preferences

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResult
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession
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
class DataStoreVMTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataStoreVM: DataStoreVM

    @Test
    fun `getLoginSession should not return null and should success`() {
        val dummyUsrLoginSession = UsrSession(
            "someone@gmail.com",
            "someonePassHere",
            "someId",
            true
        )

        val expectedData = MutableLiveData<UsrSession>()
        expectedData.value = dummyUsrLoginSession

        `when`(dataStoreVM.getLoginSession()).thenReturn(expectedData)
        val actualData = dataStoreVM.getLoginSession().getOrAwaitValue()

        verify(dataStoreVM).getLoginSession()
        assertThat(actualData).isNotNull()
        assertThat(expectedData.value).isEqualTo(actualData)
    }

    @Test
    fun `verify setLoginSession function is works`() {
        val dummyUsrLoginSession = UsrSession(
            "someId",
            "someone",
            "someoneToken",
            true
        )
        val data = dataStoreVM.setLoginSession(dummyUsrLoginSession)
        verify(dataStoreVM).setLoginSession(dummyUsrLoginSession)
        assertThat(data).isNotNull()
    }

    @Test
    fun `verify logout fucntion is works`() {
        val dummyUsrLoginSession = UsrSession(
            "someId",
            "someone",
            "someoneToken",
            true
        )

        val expectedAfterLogoutData = UsrSession(
            "",
            "",
            "",
            false
        )

        val expectedData = MutableLiveData<UsrSession>()
        expectedData.value = expectedAfterLogoutData

        //simulate the login
        dataStoreVM.setLoginSession(dummyUsrLoginSession)
        verify(dataStoreVM).setLoginSession(dummyUsrLoginSession)

        //do logout
        dataStoreVM.logout()
        verify(dataStoreVM).logout()

        //get the session after logout
        `when`(dataStoreVM.getLoginSession()).thenReturn(expectedData)
        val afterLogoutData = dataStoreVM.getLoginSession().getOrAwaitValue()
        verify(dataStoreVM).getLoginSession()

        assertThat(afterLogoutData.isLogin).isFalse()
    }
}