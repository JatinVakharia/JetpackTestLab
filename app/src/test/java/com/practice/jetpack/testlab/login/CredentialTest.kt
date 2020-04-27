package com.practice.jetpack.testlab.login

import com.practice.jetpack.testlab.di.*
import com.practice.jetpack.testlab.ui.login.LoginViewModel
import com.practice.jetpack.testlab.utility.Constants
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest

/* This Test class is explicitly used to test validateCredentials() method. */

@RunWith(Parameterized::class)
class CredentialTest(
    private val email : String,
    private val password : String,
    private val isNetwork : Boolean,
    private val expectedCode : Int,
    private val scenario : String
) : KoinTest{

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
    fun testIsValidCredentials(){
        /* Set Network explicitly, if set false, we would get 400 as errorCode */
        viewModel.isNetworkAvailable = isNetwork

        /* Check credentials Logic*/
        val actualCode = viewModel.validateCredentials(email, password)

        // Compare Result
        assertEquals(expectedCode, actualCode)
    }
}