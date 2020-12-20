package com.adyanf.clone.instagram.ui.home.post

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.ui.base.BaseAdapter

class PostsAdapter(
    parentLifecycle: Lifecycle,
    posts: ArrayList<Post>
) : BaseAdapter<Post, PostItemViewHolder>(parentLifecycle, posts) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostItemViewHolder(parent)

}