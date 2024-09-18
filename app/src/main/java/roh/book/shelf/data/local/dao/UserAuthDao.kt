package roh.book.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import roh.book.shelf.data.local.entities.UserEntity

@Dao
interface UserAuthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    fun getUserDetailsFlow(email: String): Flow<UserEntity?>

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    suspend fun getUserDetails(email: String): UserEntity?

    @Query("SELECT EXISTS(SELECT * FROM UserEntity WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Query("DELETE FROM UserEntity")
    suspend fun deleteUser()
}