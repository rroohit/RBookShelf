package roh.book.shelf.ui.auth.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import roh.book.shelf.ui.auth.AuthViewModel
import roh.book.shelf.ui.auth.signup.SignupEvent
import roh.book.shelf.ui.components.AnimatedBook
import roh.book.shelf.ui.components.DefaultButton
import roh.book.shelf.ui.components.DefaultEditBox
import roh.book.shelf.ui.components.DefaultPasswordEditBox
import roh.book.shelf.ui.theme.TextColor

@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()
    var isLoginInProgress by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val logInEvent = authViewModel.loginEvent.collectAsStateWithLifecycle(
        minActiveState = androidx.lifecycle.Lifecycle.State.STARTED
    )
    when(logInEvent.value) {
        LogInEvent.Idle -> {
            isLoginInProgress = false
        }
        LogInEvent.LoginInProgress -> {
            isLoginInProgress = true
        }
        LogInEvent.LoginSuccess -> {
            isLoginInProgress = false
        }
        is LogInEvent.LoginError -> {
            isLoginInProgress = false
            val errorMessage = (logInEvent.value as LogInEvent.LoginError).message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        //
        AnimatedBook(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Log In",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Column(
            modifier = modifier
                .weight(0.4f)
                .padding(18.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            DefaultEditBox(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            DefaultPasswordEditBox(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )

        }

        Column(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AnimatedVisibility(visible = isLoginInProgress) {
                CircularProgressIndicator()
            }

            AnimatedVisibility(visible = !isLoginInProgress) {
                DefaultButton(
                    title = "Login"
                ) {
                    authViewModel.logIn(email, password)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            SignUpText {
                navController.navigate("signup")
            }
        }
    }
}

@Composable
fun SignUpText(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,

        ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Don't have account? ",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .clickable { onSignUpClick() },
            textAlign = TextAlign.Center,
            text = "Sign Up",
            color = TextColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}