package com.practice.jetpack.testlab.ui.main

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
import com.practice.jetpack.testlab.utility.PaginationScrollListener
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), OnRefreshListener {

    private lateinit var adapter : StoriesListAdapter
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    var stories = ArrayList<Story>()

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel : MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun fetchAllStoryData(){
        // fetch all story ids
        swipeRefreshList.isRefreshing = true
        mainViewModel.getAllStories().removeObservers(viewLifecycleOwner)
        mainViewModel.getAllStories().observe(viewLifecycleOwner, Observer {
            swipeRefreshList.isRefreshing = false
            getMoreItems()
        })
    }

    override fun onRefresh() {
        swipeRefreshList.isRefreshing = false
//        mainViewModel.allStories.clear()
//        fetchAllStoryData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchAllStoryData()

        swipeRefreshList.setOnRefreshListener(this)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = StoriesListAdapter(this.requireContext())
        adapter.setMyStoryList(ArrayList<Story>())

        recyclerView.adapter = adapter

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call loadmore items to get more data
                getMoreItems()
            }
        })
    }

    fun getMoreItems() {
        swipeRefreshList.isRefreshing = true
        stories.clear()
        mainViewModel.getNext20().removeObservers(viewLifecycleOwner)
        mainViewModel.getNext20().observe( viewLifecycleOwner, Observer<Story> {
            stories.add(it)
            if(stories.size == 20) {
                swipeRefreshList.isRefreshing = false
                adapter.addMyStories(stories)
                isLoading = false
            }
        })
    }

}
