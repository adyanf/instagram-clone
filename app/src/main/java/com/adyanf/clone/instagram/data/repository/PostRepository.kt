package com.adyanf.clone.instagram.data.repository

import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.remote.NetworkService
import com.adyanf.clone.instagram.data.remote.request.PostCreationRequest
import com.adyanf.clone.instagram.data.remote.request.PostLikeModifyRequest
import javax.inject.Inject

class PostRepository @Inject constructor(private val networkService: NetworkService) {

    suspend fun fetchHomePostList(user: User, firstPostId: String?, lastPostId: String?): List<Post> {
        val response = networkService.doHomePostsListCall(
            firstPostId,
            lastPostId,
            user.id,
            user.accessToken
        )
        return response.data
    }

    suspend fun getMyPostList(user: User): List<Post> {
        val response = networkService.doMyPostListCall(
            user.id,
            user.accessToken
        )
        return response.data
    }

    suspend fun makeLikePost(post: Post, user: User): Post {
        networkService.doPostLikeCall(
            PostLikeModifyRequest(post.id),
            user.id,
            user.accessToken
        )
        post.likedBy?.apply {
            this.find { postUser -> postUser.id == user.id } ?: this.add(
                Post.User(
                    user.id,
                    user.name,
                    user.profilePicUrl
                )
            )
        }
        return post
    }

    suspend fun makeUnlikePost(post: Post, user: User): Post {
        networkService.doPostUnlikeCall(
            PostLikeModifyRequest(post.id),
            user.id,
            user.accessToken
        )
        post.likedBy?.apply {
            this.find { postUser -> postUser.id == user.id }?.let { this.remove(it) }
        }
        return post
    }

    suspend fun createPost(imgUrl: String, imgWidth: Int, imgHeight: Int, user: User): Post {
        val response = networkService.doCreatePostCall(
            PostCreationRequest(imgUrl, imgWidth, imgHeight), user.id, user.accessToken
        )
        return response.run {
            Post(
                data.id,
                data.imageUrl,
                data.imageWidth,
                data.imageHeight,
                Post.User(
                    user.id,
                    user.name,
                    user.profilePicUrl
                ),
                mutableListOf(),
                data.createdAt
            )
        }
    }
}