package com.adyanf.clone.instagram.data.remote

import com.adyanf.clone.instagram.data.model.Post
import com.adyanf.clone.instagram.data.remote.request.LoginRequest
import com.adyanf.clone.instagram.data.remote.request.PostCreationRequest
import com.adyanf.clone.instagram.data.remote.request.PostLikeModifyRequest
import com.adyanf.clone.instagram.data.remote.request.SignUpRequest
import com.adyanf.clone.instagram.data.remote.request.UpdateMyInfoRequest
import com.adyanf.clone.instagram.data.remote.response.GeneralResponse
import com.adyanf.clone.instagram.data.remote.response.ImageResponse
import com.adyanf.clone.instagram.data.remote.response.LoginResponse
import com.adyanf.clone.instagram.data.remote.response.MyInfoResponse
import com.adyanf.clone.instagram.data.remote.response.PostCreationResponse
import com.adyanf.clone.instagram.data.remote.response.PostListResponse
import com.adyanf.clone.instagram.data.remote.response.SignUpResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import java.util.Date

class FakeNetworkService : NetworkService {

    override fun doLoginCall(request: LoginRequest, apiKey: String): Single<LoginResponse> {
        return Single.just(
            LoginResponse(
                "statusCode",
                200,
                "success",
                "accessToken",
                "userId",
                "userName",
                "userEmail",
                "profilePicUrl"
            )
        )
    }

    override fun doSignUpCall(request: SignUpRequest, apiKey: String): Single<SignUpResponse> {
        TODO("Not yet implemented")
    }

    override fun doHomePostsListCall(
        firstPostId: String?,
        lastPostId: String?,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostListResponse> {
        val creator1 = Post.User("userId1", "name1", "profilePicUrl1")
        val creator2 = Post.User("userId2", "name2", "profilePicUrl2")

        val likedBy = mutableListOf<Post.User>(
            Post.User("userId3", "name3", "profilePicUrl3"),
            Post.User("userId4", "name4", "profilePicUrl4")
        )

        val post1 = Post("postId1", "imgUrl1", 400, 400, creator1, likedBy, Date())
        val post2 = Post("postId2", "imgUrl2", 400, 400, creator2, likedBy, Date())

        val postListResponse = PostListResponse("statusCode", 200, "message", listOf(post1, post2))

        return Single.just(postListResponse)
    }

    override fun doPostLikeCall(
        request: PostLikeModifyRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doPostUnlikeCall(
        request: PostLikeModifyRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doFetchMyInfoCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<MyInfoResponse> {
        TODO("Not yet implemented")
    }

    override fun doUpdateMyInfoCall(
        request: UpdateMyInfoRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doMyPostListCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostListResponse> {
        TODO("Not yet implemented")
    }

    override fun doLogoutCall(
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<GeneralResponse> {
        TODO("Not yet implemented")
    }

    override fun doImageUploadCall(
        image: MultipartBody.Part,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<ImageResponse> {
        TODO("Not yet implemented")
    }

    override fun doCreatePostCall(
        request: PostCreationRequest,
        userId: String,
        accessToken: String,
        apiKey: String
    ): Single<PostCreationResponse> {
        TODO("Not yet implemented")
    }
}