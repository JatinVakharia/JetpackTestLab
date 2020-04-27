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

/* This Test class is explicitly used to test isPassWordValid() method. */

@RunWith(Parameterized::class)
class PasswordTest(
    private val password : String,
    private val expected : Boolean,
    private val scenario : String
) : KoinTest{

    private val viewModel: LoginViewModel by inject()

    // Testing password with parametrised way
    companion object{
        @JvmStatic
        @Parameterized.Parameters(name = "Password test : {2}")
        fun data(): Collection<Array<*>> = listOf(
            arrayOf("", false, "Empty Password"),
            arrayOf("Pass@adfasdf", false, "No Digit"),
            arrayOf("PASS@24523", false, "No small chars"),
            arrayOf("pass@24523", false, "No Capital Chars"),
            arrayOf("Pass12345", false, "No Special Char"),
            arrayOf("Pass@12", false, "Only 7 Chars"),
            arrayOf("Pass@123Pass@1234", false, "17 Chars"),
            arrayOf("Pass@123", true, "Exact 8 Chars"),
            arrayOf("Pass@123Pass@123", true, "Exact 16 Chars"),
            arrayOf(Constants.validPassword, true, "Actual Password")
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
    fun testIsPasswordValid(){
        val isValid = viewModel.isPassWordValid(password)
        assertEquals(expected, isValid)
    }

}