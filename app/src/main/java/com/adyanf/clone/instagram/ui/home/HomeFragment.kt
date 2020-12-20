package com.adyanf.clone.instagram.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.di.component.FragmentComponent
import com.adyanf.clone.instagram.ui.base.BaseFragment
import com.adyanf.clone.instagram.ui.home.post.PostsAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeViewModel>() {

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var postsAdapter: PostsAdapter

    override fun provideLayoutId(): Int = R.layout.fragment_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.posts.observe(this, Observer {
            it.data?.run { postsAdapter.appendData(this) }
        })

        viewModel.loading.observe(this, Observer {
            progressBar.isEnabled = it
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun setupView(view: View) {
        rvPosts.apply {
            layoutManager = linearLayoutManager
            adapter = postsAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if (this is LinearLayoutManager &&
                            itemCount > 0 && itemCount == findLastVisibleItemPosition() + 1) {
                            viewModel.onLoadMore()
                        }
                    }
                }
            })
        }
    }

    companion object {
        const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}