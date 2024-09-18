package roh.book.shelf.domain.repository

import kotlinx.coroutines.flow.Flow
import roh.book.shelf.domain.model.UserDetails

interface UserAuthRepository {

    val userEmail: Flow<String>

    suspend fun saveUserEmail(email: String)

    suspend fun deleteUserEmail()

    suspend fun isEmailExistAlready(email: String): Boolean

    suspend fun insertUserDetails(userDetails: UserDetails, onSuccess: () -> Unit, onError: (String) -> Unit)

    fun getUserDetailsFlow(email: String): Flow<UserDetails?>

    suspend fun getUserDetails(email: String): UserDetails?

    suspend fun logOutUser()
}