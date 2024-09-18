package roh.book.shelf.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import roh.book.shelf.data.local.dao.BooksDao
import roh.book.shelf.data.local.entities.BookEntity
import roh.book.shelf.data.network.BookApiService
import roh.book.shelf.domain.model.CountryApiResponse
import roh.book.shelf.domain.repository.BooksRepository
import roh.book.shelf.util.getYearFromTimestamp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepositoryImpl
@Inject
constructor(
    private val bookApiService: BookApiService,
    private val booksDao: BooksDao,
) : BooksRepository {

    companion object {
        private const val TAG = "BOOKS_REPOSITORY"
    }

    override fun getBooks(): Flow<List<BookEntity>> {
        return booksDao.getAllBooks()
    }

    override fun getBookmarkBooks(): Flow<List<BookEntity>> {
        return booksDao.getBookmarkedBooks()
    }

    override suspend fun getCountries(): List<CountryApiResponse> {
        return bookApiService.getCountries()
    }

    override suspend fun fetchBooks() {
        try {
            val books = bookApiService.getBooks()
                .map {
                    BookEntity(
                        id = it.id,
                        image = it.image,
                        score = it.score,
                        popularity = it.popularity,
                        title = it.title,
                        publishedChapterDate = getYearFromTimestamp(it.publishedChapterDate)
                    )
                }
            Log.d(TAG, "fetchBooks: books ==> $books")
            booksDao.insertAll(books)
        } catch (e: Exception) {
            // need inform user
        }
    }

    override suspend fun updateBookmark(book: BookEntity) {
        booksDao.updateBook(book)
    }

}