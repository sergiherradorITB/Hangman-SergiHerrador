package com.example.hangman_sergiherrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hangman_sergiherrador.ui.theme.HangmanSergiHerradorTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HangmanSergiHerradorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.SplashScreen.route
                    ) {
                        composable(Routes.SplashScreen.route) { SplashScreen(navigationController) }
                        composable(Routes.MenuScreen.route) { MenuScreen(navigationController) }
                        composable(
                            Routes.GameScreen.route,
                            arguments = listOf(navArgument("difficulty") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            GameScreen(
                                navigationController,
                                backStackEntry.arguments?.getString("difficulty") ?: ""
                            )
                        }
                        composable(
                            Routes.ResultScreen.route,
                            arguments = listOf(
                                navArgument("resultType") { type = NavType.StringType },
                                navArgument("difficulty") { type = NavType.StringType },
                                navArgument("attempts") { type = NavType.IntType }  // Agregamos el argumento "attempts"
                            )
                        ) { backStackEntry ->
                            val resultType = ResultType.valueOf(
                                backStackEntry.arguments?.getString("resultType") ?: "WIN"
                            )
                            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: ""

                            // Obtenemos el valor de "attempts" o asignamos 10 como valor predeterminado
                            val attempts = backStackEntry.arguments?.getInt("attempts") ?: 10

                            val word = backStackEntry.arguments?.getString("word") ?: ""


                            ResultScreen(
                                navController = navigationController,
                                result = resultType,
                                attempts = attempts,
                                difficulty = difficulty,
                                word = word
                            )
                        }


                    }
                }
            }
        }
    }

    @Composable
    fun Splash(alphaAnim: Float) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo", alpha = alphaAnim
            )
        }
    }


    @Composable
    fun SplashScreen(navController: NavController) {
        var startAnimation by remember { mutableStateOf(false) }
        val alphaAnim = animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 3000)
        )
        LaunchedEffect(key1 = true) {
            startAnimation = true
            delay(4000)
            navController.popBackStack()
            navController.navigate(Routes.MenuScreen.route)
        }
        Splash(alphaAnim.value)
    }
}
