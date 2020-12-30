package com.adyanf.clone.instagram.data.remote

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
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST(Endpoints.LOGIN)
    suspend fun doLoginCall(
        @Body request: LoginRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): LoginResponse

    @POST(Endpoints.SIGNUP)
    suspend fun doSignUpCall(
        @Body request: SignUpRequest,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
    ): SignUpResponse

    @GET(Endpoints.HOME_POSTS_LIST)
    suspend fun doHomePostsListCall(
        @Query("firstPostId") firstPostId: String?,
        @Query("lastPostId") lastPostId: String?,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): PostListResponse

    @PUT(Endpoints.POST_LIKE)
    suspend fun doPostLikeCall(
        @Body request: PostLikeModifyRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): GeneralResponse

    @PUT(Endpoints.POST_UNLIKE)
    suspend fun doPostUnlikeCall(
        @Body request: PostLikeModifyRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): GeneralResponse

    @GET(Endpoints.MY_INFO)
    suspend fun doFetchMyInfoCall(
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): MyInfoResponse

    @PUT(Endpoints.MY_INFO)
    suspend fun doUpdateMyInfoCall(
        @Body request: UpdateMyInfoRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): GeneralResponse

    @GET(Endpoints.MY_POST_LIST)
    suspend fun doMyPostListCall(
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): PostListResponse

    @DELETE(Endpoints.LOGOUT)
    suspend fun doLogoutCall(
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): GeneralResponse

    @Multipart
    @POST(Endpoints.UPLOAD_IMAGE)
    suspend fun doImageUploadCall(
        @Part image: MultipartBody.Part,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): ImageResponse

    @POST(Endpoints.CREATE_POST)
    suspend fun doCreatePostCall(
        @Body request: PostCreationRequest,
        @Header(Networking.HEADER_USER_ID) userId: String,
        @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String,
        @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY
    ): PostCreationResponse

    /*
     * Example to add other headers
     *
     *  @POST(Endpoints.DUMMY_PROTECTED)
        fun sampleDummyProtectedCall(
            @Body request: DummyRequest,
            @Header(Networking.HEADER_USER_ID) userId: String, // pass using UserRepository
            @Header(Networking.HEADER_ACCESS_TOKEN) accessToken: String, // pass using UserRepository
            @Header(Networking.HEADER_API_KEY) apiKey: String = Networking.API_KEY // default value set when Networking create is called
        ): DummyResponse
     */
}