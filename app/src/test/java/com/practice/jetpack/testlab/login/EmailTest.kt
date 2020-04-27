package com.practice.jetpack.testlab.login

import com.practice.jetpack.testlab.di.*
import com.practice.jetpack.testlab.ui.login.LoginViewModel
import com.practice.jetpack.testlab.utility.Constants
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest

/* This Test class is explicitly used to test isEmailValid() method. */

@RunWith(Parameterized::class)
class EmailTest(
    private val email : String,
    private val expected : Boolean,
    private val scenario : String
) : KoinTest{

    private val viewModel: LoginViewModel by inject()

    // Testing email with parametrised way
    companion object{
        @JvmStatic
        @Parameterized.Parameters(name = "Email test : {2}")
        fun data(): Collection<Array<*>> = listOf(
            arrayOf("", false, "Empty Email"),
            arrayOf("asdf@", false, "No Email"),
            arrayOf("asdf@fasf", false, "No Trailing"),
            arrayOf("@fasf.com", false, "No specification"),
            arrayOf(" d@fasf.com", false, "Starting With Space"),
            arrayOf("d@fasf.com ", false, "Ending With Space"),
            arrayOf("d@fa sf.com", false, "Space InBetween"),
            arrayOf(Constants.validUsername, true, "Actual Email")
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
    fun testIsEmailValid(){
        val isValid = viewModel.isEmailValid(email)
        assertEquals(expected, isValid)
    }

}