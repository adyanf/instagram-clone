package com.adyanf.clone.instagram.data.repository

import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.remote.NetworkService
import com.adyanf.clone.instagram.data.remote.Networking
import com.adyanf.clone.instagram.data.remote.response.PostListResponse
import com.adyanf.clone.instagram.utils.coroutine.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var networkService: NetworkService

    private lateinit var postRepository: PostRepository

    @Before
    fun setup() {
        Networking.API_KEY = "FAKE_API_KEY"
        postRepository = PostRepository(networkService)
    }

    @Test
    fun fetchHomePostList_requestDoHomePostListCall() = testCoroutineRule.runBlockingTest {
        // given
        val firstPostId = "firstPostId"
        val lastPostId = "lastPostId"
        val user = User("userId", "userName", "userEmail", "accessToken", "profilePicUrl")
        doReturn(PostListResponse("statusCode", 200, "message", emptyList()))
            .`when`(networkService)
            .doHomePostsListCall(
                firstPostId,
                lastPostId,
                user.id,
                user.accessToken,
                Networking.API_KEY
            )

        // when
        postRepository.fetchHomePostList(user, firstPostId, lastPostId)

        // then
        verify(networkService).doHomePostsListCall(
            firstPostId,
            lastPostId,
            user.id,
            user.accessToken,
            Networking.API_KEY
        )
    }
}