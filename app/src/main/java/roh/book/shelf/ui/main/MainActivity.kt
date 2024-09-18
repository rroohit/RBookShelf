package roh.book.shelf.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import roh.book.shelf.ui.auth.LandingScreen
import roh.book.shelf.ui.auth.login.LogInScreen
import roh.book.shelf.ui.auth.signup.SignUpScreen
import roh.book.shelf.ui.home.HomeScreen
import roh.book.shelf.ui.theme.RBookShelfTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen =  installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoggedIn.value == null
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RBookShelfTheme {
                MainNavigation(modifier = Modifier.fillMaxSize(), viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn = viewModel.isLoggedIn.collectAsState()
    val startDestination = when(isLoggedIn.value) {
        true -> "home"
        false -> "landing"
        null -> "loading"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable("loading") {
            LoadingScreen()
        }

        composable("home") {
            HomeScreen() {
                viewModel.logOutUser()
            }
        }

        composable("landing") {
            LandingScreen(navController = navController)
        }

        composable("login") {
            LogInScreen(navController = navController)
        }

        composable("signup") {
            SignUpScreen(navController = navController)
        }
    }
}

