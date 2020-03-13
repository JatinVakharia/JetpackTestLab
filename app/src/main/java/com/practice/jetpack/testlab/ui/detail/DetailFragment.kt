package com.practice.jetpack.testlab.ui.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.practice.jetpack.testlab.R
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.item_story.*

class DetailFragment : DialogFragment() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url : String = arguments?.get("url").toString()
        val title : String = arguments?.get("title").toString()
        webview.loadUrl(url)
        tv_story_title.text = title
        close_detail_fragment.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}
