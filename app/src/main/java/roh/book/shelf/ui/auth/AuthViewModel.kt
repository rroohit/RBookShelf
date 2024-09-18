package roh.book.shelf.ui.auth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import roh.book.shelf.domain.model.CountryApiResponse
import roh.book.shelf.domain.model.UserDetails
import roh.book.shelf.domain.repository.BooksRepository
import roh.book.shelf.domain.repository.UserAuthRepository
import roh.book.shelf.ui.auth.login.LogInEvent
import roh.book.shelf.ui.auth.signup.SignupEvent
import roh.book.shelf.util.isValidEmail
import roh.book.shelf.util.isValidPassword
import javax.inject.Inject

private const val TAG = "AUTH_VIEW_MODEL"

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val userAuthRepository: UserAuthRepository,
    private val booksRepository: BooksRepository
) : ViewModel() {

    private val _countries = mutableStateListOf<CountryApiResponse>()
    val countries: List<CountryApiResponse> = _countries

    init {
        viewModelScope.launch {
            _countries.addAll(booksRepository.getCountries())
        }
    }

    suspend fun fetchCountries(): List<CountryApiResponse> {
        return booksRepository.getCountries()
    }

    fun getSelectedCountry(context: Context): CountryApiResponse? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle permission request
            return null
        }
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location?.let { it ->
            val geocoder = Geocoder(context, java.util.Locale.getDefault())
            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val countryCode = addresses[0].countryCode
                return _countries.find { rsp ->
                    rsp.country.contains(countryCode)
                }
            }
        }
        return null
    }

    private val _loginEvent = MutableStateFlow<LogInEvent>(LogInEvent.Idle)
    val loginEvent: StateFlow<LogInEvent> = _loginEvent.asStateFlow()
    fun logIn(email: String, password: String) {
        Log.d(TAG, "logIn :: email - $email : password - $password")
        viewModelScope.launch(Dispatchers.IO) {
            updateLoginEvent(LogInEvent.LoginInProgress)
            delay(1000L)

            if (!isValidEmail(email)) {
                updateLoginEvent(LogInEvent.LoginError("Invalid Email!"))
                return@launch
            }

            if (!userAuthRepository.isEmailExistAlready(email)) {
                updateLoginEvent(LogInEvent.LoginError("Email not registered! Please sign up."))
                return@launch
            }

            if (!isValidPassword(password)) {
                updateLoginEvent(LogInEvent.LoginError("Invalid Password!"))
                return@launch
            }

            val userDetails = userAuthRepository.getUserDetails(email)
            Log.d(TAG, "login userDetails ==> $userDetails")
            if (userDetails?.password != password) {
                // password not matched
                updateLoginEvent(LogInEvent.LoginError("Wrong Password!"))
                return@launch
            }

            // login
            userAuthRepository.saveUserEmail(email)
        }
    }

    private suspend fun updateLoginEvent(logInEvent: LogInEvent) {
        viewModelScope.launch {
            Log.d(TAG, "updateLoginEvent: $logInEvent")
            _loginEvent.value = logInEvent
            delay(1000)
            when(logInEvent) {
                LogInEvent.Idle -> Unit
                is LogInEvent.LoginError,
                LogInEvent.LoginInProgress,
                LogInEvent.LoginSuccess -> _loginEvent.value = LogInEvent.Idle
            }
        }.join()
    }

    private val _signupEvent = MutableStateFlow<SignupEvent>(SignupEvent.Idle)
    val signupEvent: StateFlow<SignupEvent> = _signupEvent.asStateFlow()
    fun signUp(email: String, password: String, country: String) {
        Log.d(TAG, "signUp :: email - $email : password - $password : country - $country")
        viewModelScope.launch(Dispatchers.IO) {
            updateSignUpEvent(SignupEvent.SignupInProgress)
            delay(1000L)

            if (!isValidEmail(email)) {
                updateSignUpEvent(SignupEvent.SignupError("Invalid Email!"))
                return@launch
            }

            if (userAuthRepository.isEmailExistAlready(email)) {
                updateSignUpEvent(SignupEvent.SignupError("Email already exists! Please try different email."))
                return@launch
            }

            if (!isValidPassword(password)) {
                updateSignUpEvent(SignupEvent.SignupError("Invalid Password!"))
                return@launch
            }

            if (country.isEmpty()) {
                updateSignUpEvent(SignupEvent.SignupError("Please Select Country!"))
                return@launch
            }

            userAuthRepository.insertUserDetails(
                UserDetails(
                    email = email,
                    password = password,
                    country = country,
                ),
                onSuccess = {
                    viewModelScope.launch {
                        updateSignUpEvent(SignupEvent.SignupSuccess)
                    }
                },
                onError = { errorMsg ->
                    viewModelScope.launch {
                        updateSignUpEvent(SignupEvent.SignupError(errorMsg))
                    }
                }
            )
        }
    }

    private suspend fun updateSignUpEvent(signupEvent: SignupEvent) {
        viewModelScope.launch {
            Log.d(TAG, "updateSignUpEvent: $signupEvent")
            _signupEvent.value = signupEvent
            delay(1000) // need to improve this can be used channel instead
            when (signupEvent) {
                SignupEvent.Idle -> Unit
                SignupEvent.SignupInProgress -> Unit

                SignupEvent.SignupSuccess,
                is SignupEvent.SignupError ->  _signupEvent.value = SignupEvent.Idle
            }
        }.join()
    }

}