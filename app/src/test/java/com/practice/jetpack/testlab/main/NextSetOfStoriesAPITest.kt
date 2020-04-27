package com.practice.jetpack.testlab.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.practice.jetpack.testlab.di.apiModule
import com.practice.jetpack.testlab.di.repoModule
import com.practice.jetpack.testlab.di.schedulerModule
import com.practice.jetpack.testlab.di.testUIModule
import com.practice.jetpack.testlab.model.Story
import com.practice.jetpack.testlab.ui.main.MainViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest

/* This class is explicitly used to test topstories.json API.
* Its response is compared with expected Data*/

class NextSetOfStoriesAPITest : KoinTest{

    /* As we are using LiveData here, we require this rule */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel: MainViewModel by inject()

    @Before
    fun before(){
        StandAloneContext.startKoin(listOf(testUIModule, schedulerModule, repoModule, apiModule))
        // Before hitting Story specific data, we need to have allstories data
        viewModel.getAllStories()
    }

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun `Test to fetch single story`(){
        val expectedStoryID = viewModel.allStories[0]
        /* Set Observer to StoryResponse */
        val storyObserver = Observer<Story>{ response ->
            // Check if request and response ID are same
            assertEquals(expectedStoryID, response.id.toString())
            // URL should not be null
            assertNotNull(response.url)
            // URL should not be null
            assertNotNull(response.title)
        }

        viewModel.getStory(expectedStoryID)

        // Add and Remove observer to Live Data
        viewModel.storyLiveData.observeForever(storyObserver)
        viewModel.storyLiveData.removeObserver(storyObserver)
    }

    @Test
    fun `Test if we are hitting set of Stories`(){
        /* Set Observer to StoryResponse */
        val storyObserver = Observer<Story>{ response ->
            //Check all the next stories contain data

            // URL should not be null
            assertNotNull(response.url)
            // URL should not be null
            assertNotNull(response.title)
        }

        viewModel.getNextStories()

        // Add and Remove observer to Live Data
        viewModel.storyLiveData.observeForever(storyObserver)
        viewModel.storyLiveData.removeObserver(storyObserver)
    }

    @Test
    fun `Test if we are getting Unique Stories`(){
        val list = ArrayList<Story>()
        val shouldNotContain = false
        /* Set Observer to StoryResponse */
        val storyObserver = Observer<Story>{ response ->
            //Check all the next stories contain data
            val actualContains = list.contains(response)
            // List should not contain same data
            assertEquals(shouldNotContain, actualContains)
            // At last add to list
            list.add(response)
        }

        viewModel.getNextStories()

        // Add and Remove observer to Live Data
        viewModel.storyLiveData.observeForever(storyObserver)
        viewModel.storyLiveData.removeObserver(storyObserver)
    }

}