package roh.book.shelf.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import roh.book.shelf.data.local.dao.BooksDao
import roh.book.shelf.data.local.dao.UserAuthDao
import roh.book.shelf.data.local.entities.BookEntity
import roh.book.shelf.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, BookEntity::class],
    version = 1
)
abstract class BookShelfDatabase : RoomDatabase() {
    abstract fun userAuthDao(): UserAuthDao
    abstract fun booksDao(): BooksDao
}