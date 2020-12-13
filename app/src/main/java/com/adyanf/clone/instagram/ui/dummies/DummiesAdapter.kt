package com.adyanf.clone.instagram.ui.dummies

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.adyanf.clone.instagram.data.model.Dummy
import com.adyanf.clone.instagram.ui.base.BaseAdapter

class DummiesAdapter(
    parentLifecycle: Lifecycle,
    private val dummies: ArrayList<Dummy>
) : BaseAdapter<Dummy, DummyItemViewHolder>(parentLifecycle, dummies) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DummyItemViewHolder(parent)
}