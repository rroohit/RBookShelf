package roh.book.shelf.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["id"], unique = true)]
)
data class BookEntity(
    @PrimaryKey val id: String,
    val image: String,
    val score: Double,
    val popularity: Int,
    val title: String,
    val publishedChapterDate: Int,
    val isBookmarked: Boolean = false
)