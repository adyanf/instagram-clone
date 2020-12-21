package com.adyanf.clone.instagram.ui.dummies

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.adyanf.clone.instagram.data.model.Dummy
import com.adyanf.clone.instagram.databinding.ItemViewDummiesBinding
import com.adyanf.clone.instagram.di.component.ViewHolderComponent
import com.adyanf.clone.instagram.ui.base.BaseItemViewHolder

class DummyItemViewHolder(parent: ViewGroup) :
    BaseItemViewHolder<Dummy, DummyItemViewModel, ItemViewDummiesBinding>(
        parent,
        ItemViewDummiesBinding::inflate
    ) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.name.observe(this, Observer {
            binding.tvHeadLineDummy.text = it
        })

        viewModel.url.observe(this, Observer {
            Glide.with(itemView.context).load(it).into(binding.ivDummy)
        })
    }

    override fun setupView(view: View) {
        view.setOnClickListener {
            viewModel.onItemClick(adapterPosition)
        }
    }
}