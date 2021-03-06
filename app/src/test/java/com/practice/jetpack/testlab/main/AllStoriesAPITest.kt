package com.practice.jetpack.testlab.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.practice.jetpack.testlab.di.apiModule
import com.practice.jetpack.testlab.di.repoModule
import com.practice.jetpack.testlab.di.schedulerModule
import com.practice.jetpack.testlab.di.testUIModule
import com.practice.jetpack.testlab.ui.main.MainViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest

/* This class is explicitly used to test item/{storyId}.json API.
* Its response is compared with expected data*/

class AllStoriesAPITest : KoinTest{

    /* As we are using LiveData here, we require this rule */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel: MainViewModel by inject()

    @Before
    fun before(){
        StandAloneContext.startKoin(listOf(testUIModule, schedulerModule, repoModule, apiModule))
    }

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun `Test to fetch all Story IDs`(){
        val expectedDataReceived = true
        val expectedDataEntries = 500
        /* Set Observer to StoriesResponse */
        val storiesObserver = Observer<Boolean>{ response ->
            // Compare Result
            assertEquals(expectedDataReceived, response)
            // Compare Data Entries
            val actualDataEntries = viewModel.allStories.size
            // Compare Result
            assertEquals(expectedDataEntries, actualDataEntries)
        }

        viewModel.getAllStories()

        // Add and Remove observer to Live Data
        viewModel.allStoriesLiveData.observeForever(storiesObserver)
        viewModel.allStoriesLiveData.removeObserver(storiesObserver)
    }

}