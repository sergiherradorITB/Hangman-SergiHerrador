package com.example.hangman_sergiherrador

sealed class Routes(val route: String) {

    object SplashScreen:Routes("splash_screen")
    object MenuScreen:Routes("menu_screen")
    object GameScreen:Routes("game_screen/{difficulty}"){
        fun createRoute(difficulty:String) = "game_screen/$difficulty"
    }
    object ResultScreen : Routes("result_screen/{resultType}/{difficulty}/{attempts}/{word}") {
        fun createRoute(resultType: String, difficulty: String, attempts: Int, word:String) =
            "result_screen/$resultType/$difficulty/$attempts/$word"
    }
}
