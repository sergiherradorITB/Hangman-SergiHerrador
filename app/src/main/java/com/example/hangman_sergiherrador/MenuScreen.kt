package com.example.hangman_sergiherrador

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.fondo),
                contentScale = ContentScale.FillBounds)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo con padding
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .padding(16.dp)
        )
        // Selector de dificultad
        var selectedText by remember { mutableStateOf("Easy") }
        var expanded by remember { mutableStateOf(false) }
        var show by remember { mutableStateOf(false) }

        val difficulties = listOf("Easy", "Medium", "Hard")

        Box(Modifier.padding(20.dp)) {
            // DropdownMenu primero
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                difficulties.forEach { difficulty ->
                    DropdownMenuItem(text = { Text(text = difficulty) }, onClick = {
                        expanded = false
                        selectedText = difficulty
                    })
                }
            }

            // Luego el OutlinedTextField
            OutlinedTextField(
                value = selectedText,
                onValueChange = { selectedText = it },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .clickable { expanded = true }
                    .fillMaxWidth()
            )
        }


        // Botón de Play
        Button(
            onClick = {
                navController.navigate(Routes.GameScreen.createRoute(selectedText))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Text(text = "Play", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Botón de Help
        Button(
            onClick = { show = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Magenta
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f),
        ) {
            Text(text = "Help", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        MyDialog(show) { show = false }
    }
}

@Composable
fun MyDialog(show: Boolean, onDismiss: () -> Unit) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Bienvenido al Hangman hecho por Sergi \n \n" +
                            "Puedes escoger entre tres modos: \n" +
                            "Easy: Tienes 8 intentos y las palabras más fáciles \n" +
                            "Medium: Tienes 6 intentos y las palabras de dificultad ni fácil ni díficill \n" +
                            "Hard: Tienes 4 intentos y las palabras más díficiles \n \n" +
                            "Las normas son: \n" +
                            "Se genera una palabra aleatoria y debes de adivinarla tocando las letras. " +
                            "Una vez pulsada una letra ya es usada se bloqueará en otro color " +
                            "Cada vez que falles se sumará una parte al dibujo del hangman y por ende se te restará un intento/vida\n" +
                            "Si aciertas suma la letra y no resta vida \n\n" +
                            "Si se te agotan los intentos o la adivinas irás a otra pantalla para poder escoger: \n" +
                            "1- Volver a jugar con la misma dificultad y otra palabra\n" +
                            "2- Volver al menú que te permitirá volver al menú y hacer lo que quieras dentro de sus posibilidades\n" +
                            "A disfrutar!! ",
                    fontSize = 15.sp
                )

            }
        }
    }
}