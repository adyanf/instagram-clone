package com.adyanf.clone.instagram.data.remote

import com.adyanf.clone.instagram.data.remote.request.DummyRequest
import com.adyanf.clone.instagram.data.remote.request.LoginRequest
import com.adyanf.clone.instagram.data.remote.request.PostLikeModifyRequest
import com.adyanf.clone.instagram.data.remote.request.SignUpRequest
import com.adyanf.clone.instagram.data.remote.response.DummyResponse
import com.adyanf.clone.instagram.data.remote.response.GeneralResponse
import com.adyanf.clone.instagram.data.remote.response.LoginResponse
import com.adyanf.clone.instagram.data.remote.response.MyInfoResponse
import com.adyanf.clone.instagram.data.remote.response.PostListResponse
import com.adyanf.clone.instagram.data.remote.response.SignUpResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST(Endpoints.DUMMY)
    fun doDummyCall(
        @Body request: DummyRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<DummyResponse>

    @POST(Endpoints.LOGIN)
    fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<LoginResponse>

    @POST(Endpoints.SIGNUP)
    fun doSignUpCall(
        @Body request: SignUpRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): Single<SignUpResponse>

    @GET(Endpoints.HOME_POSTS_LIST)
    fun doHomePostsListCall(
        @Query("firstPostId") firstPostId: String?,
        @Query("lastPostId") lastPostId: String?,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<PostListResponse>

    @PUT(Endpoints.POST_LIKE)
    fun doPostLikeCall(
        @Body request: PostLikeModifyRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<GeneralResponse>

    @PUT(Endpoints.POST_UNLIKE)
    fun doPostUnlikeCall(
        @Body request: PostLikeModifyRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<GeneralResponse>

    @GET(Endpoints.FETCH_MY_INFO)
    fun doFetchMyInfoCall(
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<MyInfoResponse>

    @GET(Endpoints.MY_POST_LIST)
    fun doMyPostListCall(
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<PostListResponse>

    @DELETE(Endpoints.LOGOUT)
    fun doLogoutCall(
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): Single<GeneralResponse>

    /*
     * Example to add other headers
     *
     *  @POST(Endpoints.DUMMY_PROTECTED)
        fun sampleDummyProtectedCall(
            @Body request: DummyRequest,
            @Header(Networking.HEADER_USER_ID) userId: String, // pass using UserRepository
            @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String, // pass using UserRepository
            @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
        ): Single<DummyResponse>
     */
}