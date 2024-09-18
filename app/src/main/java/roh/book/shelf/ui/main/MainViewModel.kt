package roh.book.shelf.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import roh.book.shelf.domain.repository.UserAuthRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val userAuthRepository: UserAuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        validateAuthDetails()
    }

    private fun validateAuthDetails() {
        viewModelScope.launch {
            userAuthRepository.userEmail
                .flowOn(Dispatchers.IO)
                .catch {}
                .collect { currentUserEmail ->
                    _isLoggedIn.value = currentUserEmail.isNotEmpty()
                }
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            userAuthRepository.logOutUser()
        }
    }

}