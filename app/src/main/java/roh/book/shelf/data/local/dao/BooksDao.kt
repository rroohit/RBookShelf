package roh.book.shelf.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import roh.book.shelf.data.local.entities.BookEntity

@Dao
interface BooksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM BookEntity")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("SELECT * FROM BookEntity WHERE isBookmarked = 1")
    fun getBookmarkedBooks(): Flow<List<BookEntity>>

}