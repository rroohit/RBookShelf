package roh.book.shelf.domain.model

data class UserDetails(
    val name: String = "",
    val email: String,
    val password: String,
    val country: String
)