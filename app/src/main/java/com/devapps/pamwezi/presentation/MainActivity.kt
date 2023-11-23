package com.devapps.pamwezi.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapps.pamwezi.R
import com.devapps.pamwezi.presentation.theme.PaMweziTheme
import com.devapps.pamwezi.presentation.viewmodels.SplashViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

class MainActivity : ComponentActivity() {


    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                splashViewModel.isLoading.value
            }
        }

        setContent {
            PaMweziTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Navigator()
                }
            }
        }
    }
}

@Composable
fun Navigator() {
    val landingNavController = rememberNavController()
    NavHost(navController = landingNavController, startDestination = "landing_screen") {
        composable(route = "landing_screen") {
            LandingPage(landingNavController)
        }
        composable(route = "auth_screen") {
            CreateAccountScreen(landingNavController)
        }
    }
}

@Composable
fun LandingPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to",
            color = Color.Black,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier
            .height(5.dp))
        Image(
            painter = painterResource(id = R.drawable.money),
            contentDescription = null,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        )
        Spacer(modifier = Modifier
            .height(5.dp))
        Text(
            text = "Pa Mwezy",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier
            .height(150.dp))
        SocialMediaSignupButton(
            onClick = { navController.navigate(route = "auth_screen") },
            color = Color.Black,
            text = "Create account", iconResId = R.drawable.baseline_person_add_24
        )
        Spacer(modifier = Modifier
            .height(20.dp))
        SocialMediaSignupButton(
            onClick = { null },
            color = Color.Black,
            text = "Sign in with Google", iconResId = R.drawable.google_black
        )

    }


}

@Composable
fun SocialMediaSignupButton(
    onClick: () -> Unit,
    color: Color,
    text: String,
    iconResId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color.White)
            .border(1.dp, color, shape = RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),

    ) {
        Surface(
            modifier = Modifier
                .background(color = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = null, // Provide a meaningful description if needed
                    modifier = Modifier.size(32.dp) // Adjust the size as needed
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontSize = 18.sp,

                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(navController: NavController) {
    val context = LocalContext.current.applicationContext
    var textState by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    modifier = Modifier
                        .background(color = Color.White),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(route = "landing_screen") }) {

                        }
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    },
                )
            },
            modifier = Modifier
                .background(color = Color.White)
            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(color = Color.White)
            ) {

                Spacer(
                    modifier = Modifier
                        .height(60.dp)
                )
                Text(
                    text = "Create Account",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 30.dp)
                )
                Spacer(
                    modifier = Modifier
                        .height(30.dp)
                )
                OutlinedTextField(
                    value = textState,
                    onValueChange = {
                        textState = it
                    },
                    placeholder = {
                        Text(text = "Username",
                            color = Color.Black)
                    },
                    keyboardOptions = KeyboardOptions.Default,
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .border(width = 2.dp, color = Color.LightGray),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Black
                    )
                )
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    placeholder = {
                        Text(text = "Email",
                            color = Color.Black)
                    },
                    keyboardOptions = KeyboardOptions.Default,
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .border(width = 2.dp, color = Color.LightGray),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Black
                    )
                )
                Spacer(
                    modifier = Modifier
                        .height(15.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholder = {
                        Text(text = "Password",
                            color = Color.Black)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .border(width = 2.dp, color = Color.LightGray),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Black
                    )
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Button(
                    onClick = {
                    Toast.makeText(
                        context,
                        "Button Clicked",
                        Toast.LENGTH_SHORT)
                        .show() },
                    colors = ButtonDefaults.buttonColors(
                        Color.Black
                    ),
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .width(280.dp)
                    ) {
                    Text(
                        text = "Create Account",
                        modifier = Modifier,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val landingNavController = rememberNavController()
    CreateAccountScreen(landingNavController)
}