package roh.book.shelf.domain.repository

import kotlinx.coroutines.flow.Flow
import roh.book.shelf.data.local.entities.BookEntity
import roh.book.shelf.domain.model.CountryApiResponse

interface BooksRepository {
    fun getBooks(): Flow<List<BookEntity>>

    suspend fun fetchBooks()

    suspend fun updateBookmark(book: BookEntity)

    fun getBookmarkBooks(): Flow<List<BookEntity>>

    suspend fun getCountries(): List<CountryApiResponse>
}