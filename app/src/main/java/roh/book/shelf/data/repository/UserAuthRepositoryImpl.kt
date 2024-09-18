package roh.book.shelf.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import roh.book.shelf.data.local.dao.UserAuthDao
import roh.book.shelf.data.repository.DataStoreManager.dataStore
import roh.book.shelf.domain.mapper.toUserDetails
import roh.book.shelf.domain.mapper.toUserEntity
import roh.book.shelf.domain.model.UserDetails
import roh.book.shelf.domain.repository.UserAuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAuthRepositoryImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val userAuthDao: UserAuthDao
) : UserAuthRepository {

    companion object {
        private const val TAG = "USER_AUTH_REPOSITORY"

        private const val KEY_USER_EMAIL = "user_email"

    }

    override val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(KEY_USER_EMAIL)] ?: ""
    }

    override suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(KEY_USER_EMAIL)] = email
        }
    }

    override suspend fun deleteUserEmail() {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(KEY_USER_EMAIL)] = ""
        }
    }

    override suspend fun isEmailExistAlready(email: String): Boolean {
        return userAuthDao.isEmailExists(email)
    }

    override suspend fun insertUserDetails(
        userDetails: UserDetails,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val rowId = userAuthDao.insert(userDetails.toUserEntity())
        Log.d(TAG, "insertUserDetails: rowId => $rowId")
        if (rowId > 0) {
            saveUserEmail(userDetails.email)
            onSuccess()
        } else {
            onError("Something went wrong!")
        }
    }


    override fun getUserDetailsFlow(email: String): Flow<UserDetails?> {
        return userAuthDao.getUserDetailsFlow(email).map {
            it?.toUserDetails()
        }
    }

    override suspend fun getUserDetails(email: String): UserDetails? {
        return userAuthDao.getUserDetails(email)?.toUserDetails()
    }

    override suspend fun getCurrentUserEmail(): String {
        // todo
        return ""
    }

    override suspend fun logOutUser() {
        deleteUserEmail()
    }
}

object DataStoreManager {
    private const val DATA_STORE_NAME = "user_preferences"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME
    )
}