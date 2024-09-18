package roh.book.shelf.ui.auth.login

sealed class LogInEvent {
    data object Idle : LogInEvent()
    data object LoginInProgress : LogInEvent()
    data object LoginSuccess : LogInEvent()
    data class LoginError(val message: String) : LogInEvent()
}