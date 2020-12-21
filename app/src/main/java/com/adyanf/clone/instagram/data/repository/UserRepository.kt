package com.adyanf.clone.instagram.data.repository

import com.adyanf.clone.instagram.data.local.db.DatabaseService
import com.adyanf.clone.instagram.data.local.prefs.UserPreferences
import com.adyanf.clone.instagram.data.model.MyInfo
import com.adyanf.clone.instagram.data.model.User
import com.adyanf.clone.instagram.data.remote.NetworkService
import com.adyanf.clone.instagram.data.remote.request.LoginRequest
import com.adyanf.clone.instagram.data.remote.request.SignUpRequest
import com.adyanf.clone.instagram.data.remote.request.UpdateMyInfoRequest
import com.adyanf.clone.instagram.data.remote.response.GeneralResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
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

    fun doUserLogin(email: String, password: String): Single<User> =
        networkService.doLoginCall(LoginRequest(email, password))
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken,
                    it.profilePicUrl
                )
            }

    fun doUserSignUp(name: String, email: String, password: String): Single<User> =
        networkService.doSignUpCall(SignUpRequest(name, email, password))
            .map {
                User(
                    it.userId,
                    it.userName,
                    it.userEmail,
                    it.accessToken,
                    it.profilePicUrl
                )
            }

    fun doUserLogout(user: User): Single<Any> =
        networkService.doLogoutCall(user.id, user.accessToken)
            .map { it.message }

    fun doFetchInfo(user: User): Single<MyInfo> =
        networkService.doFetchMyInfoCall(user.id, user.accessToken)
            .map { it.data }

    fun doUpdateInfo(user: User, myInfo: MyInfo): Single<Any> {
        val request = UpdateMyInfoRequest(myInfo.name, myInfo.profilePicUrl, myInfo.tagline)
        return networkService.doUpdateMyInfoCall(request, user.id, user.accessToken)
            .map { it.message }
    }
}

