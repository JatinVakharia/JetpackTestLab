package com.practice.jetpack.testlab.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.practice.jetpack.androidtask.api.response.LoginResponse
import com.practice.jetpack.testlab.di.apiModule
import com.practice.jetpack.testlab.di.repoModule
import com.practice.jetpack.testlab.di.schedulerModule
import com.practice.jetpack.testlab.di.testUIModule
import com.practice.jetpack.testlab.ui.login.LoginViewModel
import com.practice.jetpack.testlab.utility.Constants
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import retrofit2.Response

/* This Test class is explicitly used to test Login API.
* Dummy data is passed to check all the scenario
* Which covers 200, 400, 401 cases*/

@RunWith(Parameterized::class)
class LoginAPITest(
    private val email : String,
    private val password : String,
    private val isNetwork : Boolean,
    private val expectedCode : Int,
    private val scenario : String
) : KoinTest{

    /* As we are using LiveData here, we require this rule is required */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel: LoginViewModel by inject()

    /* Testing in parametrised way, which reduces redundant data, class size and
    * helps to cover maximum cases */
    companion object{
        @JvmStatic
        @Parameterized.Parameters(name = "Credential test : {4}")
        fun data(): Collection<Array<*>> = listOf(
            arrayOf(Constants.validUsername, Constants.validPassword, false, 400, "No Network"),
            arrayOf("abc@abc.com", Constants.validPassword, true, 401, "Incorrect UserName"),
            arrayOf(Constants.validUsername, "Pass@123", true, 401, "Incorrect Password"),
            arrayOf(Constants.validUsername, Constants.validPassword, true, 200, "Correct Credentials")
        )
    }

    @Before
    fun before(){
        StandAloneContext.startKoin(listOf(testUIModule, schedulerModule, repoModule, apiModule))
    }

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun testLoginAPI(){
        /* Set Network explicitly, if set false, we would get 400 as errorCode */
        viewModel.isNetworkAvailable = isNetwork

        /* Set Observer to LoginResponse */
        val loginObserver = Observer<Response<LoginResponse>>{ response ->
            // Check response
            val actualCode = response.code()
            // Compare Result
            assertEquals(expectedCode, actualCode)
        }

        /*Call Login function*/
        viewModel.onLogin(email, password)

        /*Add and remove Observer of LiveData*/
        viewModel.loginResponse.observeForever(loginObserver)
        viewModel.loginResponse.removeObserver(loginObserver)
    }

}