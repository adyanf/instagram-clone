package com.adyanf.clone.instagram.data.repository

import com.adyanf.clone.instagram.data.local.prefs.UserPreferences
import com.adyanf.clone.instagram.data.model.MyInfo
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.remote.NetworkService
import com.adyanf.clone.instagram.data.remote.request.LoginRequest
import com.adyanf.clone.instagram.data.remote.request.SignUpRequest
import com.adyanf.clone.instagram.data.remote.request.UpdateMyInfoRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val userPreferences: UserPreferences
) {

    fun saveCurrentUser(user: User) {
        userPreferences.setUserId(user.id)
        userPreferences.setUserName(user.name)
        userPreferences.setUserEmail(user.email)
        userPreferences.setAccessToken(user.accessToken)
    }

    fun removeCurrentUser() {
        userPreferences.removeUserId()
        userPreferences.removeUserName()
        userPreferences.removeUserEmail()
        userPreferences.removeAccessToken()
    }

    fun updateUserName(userName: String) {
        userPreferences.setUserName(userName)
    }

    fun getCurrentUser(): User? {

        val userId = userPreferences.getUserId()
        val userName = userPreferences.getUserName()
        val userEmail = userPreferences.getUserEmail()
        val accessToken = userPreferences.getAccessToken()

        return if (userId !== null && userName != null && userEmail != null && accessToken != null)
            User(userId, userName, userEmail, accessToken)
        else
            null
    }

    suspend fun doUserLogin(email: String, password: String): User {
        val response = networkService.doLoginCall(LoginRequest(email, password))
        return response.run {
            User(userId, userName, userEmail, accessToken, profilePicUrl)
        }
    }

    suspend fun doUserSignUp(name: String, email: String, password: String): User {
        val response = networkService.doSignUpCall(SignUpRequest(name, email, password))
        return response.run {
            User(userId, userName, userEmail, accessToken, profilePicUrl)
        }
    }

    suspend fun doUserLogout(user: User): Any {
        val response = networkService.doLogoutCall(user.id, user.accessToken)
        return response.message
    }

    suspend fun doFetchInfo(user: User): MyInfo {
        val response = networkService.doFetchMyInfoCall(user.id, user.accessToken)
        return response.data
    }

    suspend fun doUpdateInfo(user: User, myInfo: MyInfo): Any {
        val request = UpdateMyInfoRequest(myInfo.name, myInfo.profilePicUrl, myInfo.tagline)
        val response = networkService.doUpdateMyInfoCall(request, user.id, user.accessToken)
        return response.message
    }
}

