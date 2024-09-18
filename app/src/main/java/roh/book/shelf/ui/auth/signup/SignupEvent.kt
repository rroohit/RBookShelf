package roh.book.shelf.ui.auth.signup

sealed class SignupEvent {
    data object Idle : SignupEvent()
    data object SignupInProgress : SignupEvent()
    data object SignupSuccess : SignupEvent()
    data class SignupError(val message: String) : SignupEvent()
}