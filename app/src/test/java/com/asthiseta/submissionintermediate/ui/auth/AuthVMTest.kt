package com.asthiseta.submissionintermediate.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.asthiseta.submissionintermediate.data.model.auth.UserLoginResult
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
class AuthVMTest {

    @get:Rule
    var instantExecutorRle = InstantTaskExecutorRule()

    @Mock
    private lateinit var authVM : AuthVM

    @Test
    fun `when get usrLogin from viewmodel should return LiveData UserLoginResult`() {
        val expectedData = MutableLiveData<UserLoginResult>()
        val dummyUserResult = UserLoginResult(
            "dummyUser",
            "dummyPassword",
            "dummyToken"
        )
        expectedData.value = dummyUserResult

        `when`(authVM.usrLogin).thenReturn(expectedData)

        val actualData = authVM.usrLogin.getOrAwaitValue()

        verify(authVM).usrLogin
        assertThat(actualData).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get message from viewmodel should return LiveData String`() {
        val expectedData = MutableLiveData<String>()
        expectedData.value = "dummyMessage"

        `when`(authVM.message).thenReturn(expectedData)

        val actualdata = authVM.message.getOrAwaitValue()

        verify(authVM).message
        assertThat(actualdata).isEqualTo(expectedData.value)
    }

    @Test
    fun `when get isLoading from viewmodel should return LiveData Boolean`() {
        val expectedData = MutableLiveData<Boolean>()
        expectedData.value = true

        `when`(authVM.isLoading).thenReturn(expectedData)

        val actualdata = authVM.isLoading.getOrAwaitValue()

        verify(authVM).isLoading
        assertThat(actualdata).isEqualTo(expectedData.value)

    }

    @Test
    fun `verify doLogin function in view model works`() {
        val dummyEmail = "someone@gmail.com"
        val dummyPassword = "someonePassHere"
        val dummyLoginResult = UserLoginResult("someone@gmail.com", "someonePassHere", "someId")

        val expectedData = MutableLiveData<UserLoginResult>()
        expectedData.value = dummyLoginResult

        authVM.doLogin(dummyEmail, dummyPassword)
        verify(authVM).doLogin(dummyEmail, dummyPassword)

        `when`(authVM.usrLogin).thenReturn(expectedData)

        val actualData = authVM.usrLogin.getOrAwaitValue()

        verify(authVM).usrLogin
        assertThat(actualData).isEqualTo(expectedData.value)

    }

    @Test
    fun `verify doRegister function in view model works`() {
        val dummyName = "someone"
        val dummyEmail = "someone@gmail.com"
        val dummyPassword = "someonePassHere"
        val expectedData = MutableLiveData<Boolean>()
        expectedData.value = true

        authVM.doRegister(dummyName, dummyEmail, dummyPassword)

        verify(authVM).doRegister(dummyName, dummyEmail, dummyPassword)

        `when`(authVM.isLoading).thenReturn(expectedData)

        val actualDAta = authVM.isLoading.getOrAwaitValue()

        verify(authVM).isLoading
        assertThat(actualDAta).isEqualTo(expectedData.value)
    }
}