package roh.book.shelf.domain.model

data class BookApiResponse(
    val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedChapterDate: Long
)
