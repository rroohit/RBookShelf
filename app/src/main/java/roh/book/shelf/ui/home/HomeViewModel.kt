package roh.book.shelf.ui.home

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import roh.book.shelf.data.local.entities.BookEntity
import roh.book.shelf.domain.repository.BooksRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {

    val books: Flow<List<BookEntity>> = booksRepository.getBooks()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.fetchBooks()
        }
    }

    fun updateBookmark(book: BookEntity) {
        viewModelScope.launch {
            booksRepository.updateBookmark(book.copy(isBookmarked = !book.isBookmarked))
        }
    }

}