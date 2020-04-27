package com.practice.jetpack.testlab.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.practice.jetpack.testlab.R
import com.practice.jetpack.testlab.model.Story
import com.practice.jetpack.testlab.ui.login.LoginFragment
import com.practice.jetpack.testlab.utility.PaginationScrollListener
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), OnRefreshListener {

    private lateinit var listAdapter : StoriesListAdapter
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    var stories = ArrayList<Story>()
    var recyclerView : RecyclerView? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel : MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private fun fetchAllStoryData(){
        // fetch all story ids
        swipeRefreshList.isRefreshing = true
        // Adding observer
        mainViewModel.getAllStories().observe(viewLifecycleOwner, Observer {
            swipeRefreshList.isRefreshing = false
            if(mainViewModel.allStories.isNotEmpty())
                getStoryItems()
        })
    }

    override fun onRefresh() {
        // Clear RecyclerView
        listAdapter.setMyStoryList(ArrayList())
        mainViewModel.refreshAllStories()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPef : SharedPreferences? = activity?.getSharedPreferences(
            LoginFragment.DARK_THEME,
            LoginFragment.PRIVATE_MODE
        )

        // checking current theme and adjusting toggle & theme
        if(sharedPef?.getBoolean(LoginFragment.DARK_THEME, false) == true){
            activity?.setTheme(R.style.AppThemeDark)
        } else {
            activity?.setTheme(R.style.AppThemeLight)
        }

        // Fetch all story ID
        fetchAllStoryData()

        // Swipe to Refresh Listener
        swipeRefreshList.setOnRefreshListener(this)

        // All RecyclerView Properties
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this.requireContext())
        listAdapter = StoriesListAdapter(this.requireContext())
        listAdapter.setMyStoryList(ArrayList())

        recyclerView?.adapter = listAdapter

        // Adding a scroll Listener, to fetch next 20 records on Page End
        recyclerView?.addOnScrollListener(object : PaginationScrollListener(recyclerView?.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                swipeRefreshList.isRefreshing = true
                // Clear Local Stories
                stories.clear()
                // Load More Stories as page has ended
                mainViewModel.loadMoreStories()
            }
        })
    }

    private fun getStoryItems() {
        swipeRefreshList.isRefreshing = true
        // Clear Local Stories
        stories.clear()
        mainViewModel.getNextStories().observe( viewLifecycleOwner, Observer<Story> {
            stories.add(it)
            if(stories.size == 20) {
                swipeRefreshList.isRefreshing = false
                // Add Stories to UI, when collected 20
                listAdapter.addMyStories(stories)

                isLoading = false
            }
        })
    }

}
