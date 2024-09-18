package roh.book.shelf.ui.auth.signup

import android.content.Context
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import roh.book.shelf.domain.model.CountryApiResponse
import roh.book.shelf.ui.auth.AuthViewModel
import roh.book.shelf.ui.components.AnimatedBook
import roh.book.shelf.ui.components.DefaultButton
import roh.book.shelf.ui.components.DefaultEditBox
import roh.book.shelf.ui.components.DefaultPasswordEditBox
import roh.book.shelf.ui.theme.TextColor


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    // rohit@gmail.co
    // rohit@123

    val context = LocalContext.current
    val authViewModel: AuthViewModel = hiltViewModel()

    var isSignUpInProgress by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signupEvent = authViewModel.signupEvent.collectAsStateWithLifecycle(
        minActiveState = androidx.lifecycle.Lifecycle.State.STARTED
    )
    when (signupEvent.value) {
        SignupEvent.Idle -> Unit
        SignupEvent.SignupInProgress -> {
            isSignUpInProgress = true
        }

        SignupEvent.SignupSuccess -> {
            isSignUpInProgress = false
            /* navController.navigate("home") {
                 popUpTo(0)
             }
             Toast.makeText(context, "Account Created.", Toast.LENGTH_SHORT).show()*/
        }

        is SignupEvent.SignupError -> {
            isSignUpInProgress = false
            val errorMessage = (signupEvent.value as SignupEvent.SignupError).message
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
                text = "Sign Up",
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

            /*DefaultEditBox(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = "Name"
            )
            Spacer(modifier = Modifier.height(16.dp))*/

            DefaultEditBox(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CountryDropdownMenuBox(
                context = context,
                viewModel = authViewModel
            ) { selectedCountry ->
                country = selectedCountry
            }

            Spacer(modifier = Modifier.height(16.dp))

            DefaultPasswordEditBox(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*DefaultPasswordEditBox(
                value = password,
                onValueChange = { password = it },
                label = "Confirm Password",
                modifier = Modifier.fillMaxWidth()
            )*/
        }
        Column(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            AnimatedVisibility(visible = isSignUpInProgress) {
                CircularProgressIndicator()
            }

            AnimatedVisibility(visible = !isSignUpInProgress) {
                DefaultButton(
                    title = "Sign Up"
                ) {
                    authViewModel.signUp(email, password, country)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LogInText {
                navController.navigateUp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdownMenuBox(
    modifier: Modifier = Modifier,
    context: Context,
    viewModel: AuthViewModel,
    onCountrySelected: (String) -> Unit,
) {

    val countries = viewModel.countries
    // todo : Need to add permission implementation to get current location
    val selectedCountry =
        viewModel.getSelectedCountry(context) ?: CountryApiResponse("India", "Asia")

    var expanded by remember { mutableStateOf(false) }
    var country by remember { mutableStateOf(selectedCountry) }
    onCountrySelected(country.country)

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = country.country,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Select Country") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            countries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.country, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        country = option
                        onCountrySelected(country.country)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun LogInText(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,

        ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Already have account? ",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier
                .clickable { onSignUpClick() },
            textAlign = TextAlign.Center,
            text = "Log In",
            color = TextColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}