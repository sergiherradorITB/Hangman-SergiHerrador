package com.example.hangman_sergiherrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ResultScreen(
    navController: NavController,
    result: ResultType,
    attempts: Int,
    difficulty: String,
    word: String
) {
    // Estado para almacenar la dificultad actual
    val currentDifficulty by remember { mutableStateOf(difficulty) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .paint(
                painterResource(id = R.drawable.fondo2),
                contentScale = ContentScale.FillBounds
            )
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val icon = when (result) {
            ResultType.WIN -> Icons.Default.Star // Si ganamos el icono será una estrellita
            ResultType.LOSE -> Icons.Default.Clear // Si perdemos el icono será una 'X'
        }

        Icon(
            imageVector = icon,
            contentDescription = "Icono que se muestra al perder/ganar",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(120.dp)
        )

        Text(
            text = when (result) {
                ResultType.WIN -> "FELICIDADES!\nHas ganado después de gastar $attempts vidas."
                ResultType.LOSE -> "Lo siento, has perdido. La palabra era: $word."
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top=20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    // Navegar a la pantalla de juego con la dificultad actual
                    navController.navigate(Routes.GameScreen.createRoute(currentDifficulty))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Text(text = "Jugar otra vez")
                }
            }

            Button(
                onClick = {
                    navController.navigate(Routes.MenuScreen.route)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    Text(text = "Volver menú")
                }
            }
        }
    }
}


enum class ResultType {
    WIN,
    LOSE
}
