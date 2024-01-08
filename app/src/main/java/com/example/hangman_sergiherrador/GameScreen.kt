package com.example.hangman_sergiherrador

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navController: NavController, difficulty: String) {
    // Obtener la palabra y el número de intentos según la dificultad
    var (word, maxAttempts) = remember { getWordAndMaxAttempts(difficulty) }

    // Estado para la palabra actualmente adivinada
    var guessedWord by remember { mutableStateOf("_".repeat(word.length)) }

    // Estado para el número de intentos restantes
    var attemptsLeft by remember { mutableStateOf(maxAttempts) }

    // Estado para indicar si el juego ha terminado
    var gameEnded by remember { mutableStateOf(false) }

    // Estado para las letras ya clicadas
    var clickedLetters by remember { mutableStateOf(emptySet<Char>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Mostrar la palabra actualmente adivinada
        Text(
            text = "Palabra: ${guessedWord.replace("", " ").trim()}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Texto para mostrar el número de intentos restantes
        Text(
            text = "Intentos (vidas) restantes: $attemptsLeft",
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Mostrar la imagen del ahorcado
        HangmanImage(difficulty, maxAttempts, attemptsLeft)

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val buttonsPerRow = 6
            val rows = ('A'..'Z').plus('Ñ').chunked(buttonsPerRow)

            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { letter ->
                        Button(
                            onClick = {
                                if (!gameEnded && letter !in clickedLetters) {
                                    // Restar intentos si la letra no está en la palabra
                                    if (!word.contains(letter, ignoreCase = true)) {
                                        attemptsLeft--
                                    }
                                    // Actualizar la palabra actualmente adivinada
                                    guessedWord = updateGuessedWord(word, guessedWord, letter)
                                    // Agregar la letra a las letras clicadas
                                    clickedLetters = clickedLetters + letter

                                    // Verificar si el juego ha terminado
                                    if (guessedWord == word) {
                                        println("Word guessed!")
                                        gameEnded = true
                                        val attempsToAccert = maxAttempts - attemptsLeft
                                        navigateToResultScreen(
                                            navController,
                                            ResultType.WIN,
                                            difficulty,
                                            attempsToAccert,
                                            word
                                        )
                                    } else if (attemptsLeft == 0) {
                                        gameEnded = true
                                        val attempsToAccert = maxAttempts - attemptsLeft
                                        navigateToResultScreen(
                                            navController,
                                            ResultType.LOSE,
                                            difficulty,
                                            attempsToAccert,
                                            word
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (letter in clickedLetters) Color.LightGray else Color.Gray,
                            ),
                            modifier = Modifier
                                .padding(2.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = letter.toString(),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        // Botón para reiniciar el juego
        Button(
            onClick = {
                // Reiniciar el juego al presionar el botón
                val (newWord, newAttempts) = getWordAndMaxAttempts(difficulty)
                word = newWord
                maxAttempts = newAttempts
                guessedWord = "_".repeat(word.length)
                attemptsLeft = maxAttempts
                gameEnded = false
                clickedLetters = emptySet()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(8.dp)
        ) {
            Text(text = "Reiniciar Juego", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Row (Modifier.padding(20.dp)){
            Text(text = "Has usado ${clickedLetters.size} letras", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun getWordAndMaxAttempts(difficulty: Comparable<*>): Pair<String, Int> {
    return when (difficulty) {
        "Easy" -> {
            val easyWords = listOf(
                "SALAZAR",
                "MANZANA",
                "NARANJA",
                "PLATANO",
                "UVA",
                "KIWI",
                "PERA",
                "MELOCOTON",
                "CIRUELA"
            )
            Pair(easyWords.random(), 8)
        }

        "Medium" -> {
            val mediumWords = listOf(
                "ELEFANTE",
                "JIRAFA",
                "CANGURO",
                "RINOCERONTE",
                "CEBRA",
                "AVESTRUZ",
                "HIPOPOTAMO",
                "GUEPARDO"
            )
            Pair(mediumWords.random(), 6)
        }

        "Hard" -> {
            val hardWords = listOf(
                "CHIMPANCE",
                "CACAHUETE",
                "MURCIELAGO",
                "GORILA",
                "COCODRILO",
                "PANTERA",
                "PYTHON",
                "ESPAGUETTI"
            )
            Pair(hardWords.random(), 4)
        }

        else -> Pair("desconocido", 10)
    }
}

// Función para actualizar la palabra actualmente adivinada
private fun updateGuessedWord(word: String, guessedWord: String, letter: Char): String {
    val updatedWord = StringBuilder(guessedWord)
    for (i in word.indices) {
        if (word[i].equals(letter, ignoreCase = true)) {
            updatedWord[i] = letter
        }
    }
    return updatedWord.toString()
}

private fun navigateToResultScreen(
    navController: NavController,
    resultType: ResultType,
    difficulty: String,
    attempts: Int,
    word: String
) {
    navController.navigate(
        Routes.ResultScreen.createRoute(
            resultType.name,
            difficulty,
            attempts,
            word
        )
    )
}

@Composable
fun HangmanImage(difficulty: String, maxAttempts: Int, attemptsLeft: Int) {

    // Definir el array de imágenes
    val hangmanImagesEasy = arrayOf(
        R.drawable.hangman1,
        R.drawable.hangman2,
        R.drawable.hangman3,
        R.drawable.hangman4,
        R.drawable.hangman5,
        R.drawable.hangman6,
        R.drawable.hangman7,
        R.drawable.hangman8_better,
        R.drawable.hangman9_better
    )

    // Definir el array de imágenes
    val hangmanImagesMedium = arrayOf(
        R.drawable.hangman1,
        R.drawable.hangman3,
        R.drawable.hangman4,
        R.drawable.hangman5,
        R.drawable.hangman6,
        R.drawable.hangman7,
        R.drawable.hangman9_better
    )

    // Definir el array de imágenes
    val hangmanImagesHard = arrayOf(
        R.drawable.hangman1,
        R.drawable.hangman3,
        R.drawable.hangman5,
        R.drawable.hangman7,
        R.drawable.hangman9_better
    )

    val resourceId = when (difficulty) {
        "Easy" -> hangmanImagesEasy[maxAttempts - attemptsLeft]
        "Medium" -> hangmanImagesMedium[maxAttempts - attemptsLeft]
        "Hard" -> hangmanImagesHard[maxAttempts - attemptsLeft]
        else -> R.drawable.hangman1
    }

    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}