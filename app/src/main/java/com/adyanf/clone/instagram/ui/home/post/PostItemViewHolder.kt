package com.adyanf.clone.instagram.ui.home.post

import android.view.View
import android.view.ViewGroup
import com.adyanf.clone.instagram.R
import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.databinding.ItemViewPostBinding
import com.adyanf.clone.instagram.di.component.ViewHolderComponent
import com.adyanf.clone.instagram.ui.base.BaseItemViewHolder
import com.adyanf.clone.instagram.utils.common.GlideHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PostItemViewHolder(parent: ViewGroup) :
    BaseItemViewHolder<Post, PostItemViewModel, ItemViewPostBinding>(
        parent,
        ItemViewPostBinding::inflate
    ) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.profileImage.observe(this) {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivProfile.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_selected))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = binding.ivProfile.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    binding.ivProfile.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_unselected))
                }
                glideRequest.into(binding.ivProfile)
            }
        }

        viewModel.name.observe(this) {
            binding.tvName.text = it
        }

        viewModel.imageDetail.observe(this) {
            it?.run {
                val glideRequest = Glide
                    .with(binding.ivPost.context)
                    .load(GlideHelper.getProtectedUrl(url, headers))

                if (placeholderWidth > 0 && placeholderHeight > 0) {
                    val params = binding.ivPost.layoutParams as ViewGroup.LayoutParams
                    params.width = placeholderWidth
                    params.height = placeholderHeight
                    binding.ivPost.layoutParams = params
                    glideRequest
                        .apply(RequestOptions.overrideOf(placeholderWidth, placeholderHeight))
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_photo))
                }
                glideRequest.into(binding.ivPost)
            }
        }

        viewModel.isLiked.observe(this) {
            binding.ivLike.setImageResource(
                if (it) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected
            )
        }

        viewModel.likesCount.observe(this) {
            binding.tvLikesCount.text = itemView.context.getString(R.string.post_like_label, it)
        }

        viewModel.postTime.observe(this) {
            binding.tvTime.text = it
        }
    }

    override fun setupView(view: View) {
        binding.ivLike.setOnClickListener { viewModel.onLikeClick() }
    }
}