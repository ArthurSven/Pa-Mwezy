package com.devapps.pamwezi.presentation

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapps.pamwezi.R
import com.devapps.pamwezi.domain.repository.GoogleAuthClient
import com.devapps.pamwezi.presentation.theme.PaMweziTheme
import com.devapps.pamwezi.presentation.ui.Screens.Home
import com.devapps.pamwezi.presentation.ui.Screens.HomeScreen
import com.devapps.pamwezi.presentation.ui.Screens.LandingPage
import com.devapps.pamwezi.presentation.ui.Screens.SplashScreen
import com.devapps.pamwezi.presentation.ui.viewmodels.AuthViewModel
import com.devapps.pamwezi.presentation.ui.viewmodels.SplashViewModel
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaMweziTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Navigation()
                }
            }
        }
    }

    @Composable
    fun Navigation() {
        val context = LocalContext.current.applicationContext
        val coroutineScope = rememberCoroutineScope()
        val authViewModel = viewModel<AuthViewModel>()
        val state by authViewModel.state.collectAsStateWithLifecycle()

        val googleAuthClient by lazy {
            GoogleAuthClient(
                context = context,
                oneTapClient = Identity.getSignInClient(context)
            )
        }

        val landingNavController = rememberNavController()
        NavHost(navController = landingNavController, startDestination = SplashScreen.route) {
            composable(SplashScreen.route) {
                SplashScreen(navController = landingNavController)

            }
            composable(route = "start_screen") {
                /*
                Checks if the user was signed in or not,
                if the user was prev signed in it will navigate to home page,
                if user was not logged in, it leads to login page
                */
                LaunchedEffect(key1 = Unit) {
                    if (googleAuthClient.getSignedInUser() != null) {
                        if (state.isSignInSuccessful) {
                        }
                        landingNavController.navigate(Home.route)
                    } else {
                        landingNavController.navigate(route = "landing_screen")
                    }
                }
            }

            composable(route = "landing_screen") {
                LandingPage(landingNavController)
            }

            composable(Home.route) {
                HomeScreen(
                    userData = googleAuthClient.getSignedInUser(),
                    onSignOut = {
                        coroutineScope . launch {
                            googleAuthClient.signOut()
                            Toast.makeText(
                                context,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()

                            landingNavController.popBackStack()
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun SplashScreen(navController: NavController) {
        val scale = remember {
            Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.3f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    }
                )
            )
            delay(3000)
            navController.navigate("start_screen")
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center) {
            val imageModifier = Modifier
                .size(150.dp)
            Image(
                painterResource(id = R.drawable.pa_mwezy),
                contentDescription = null,
                modifier = imageModifier
            )
        }
    }


    @Composable
    fun LandingPage(navController: NavController) {
        val context = LocalContext.current.applicationContext
        val coroutineScope = rememberCoroutineScope()
        val authViewModel = viewModel<AuthViewModel>()
        val state by authViewModel.state.collectAsStateWithLifecycle()

        val googleAuthClient by lazy {
            GoogleAuthClient(
                context = context,
                oneTapClient = Identity.getSignInClient(context)
            )
        }

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
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.money),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )
            Text(
                text = "Pa Mwezy",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier
                    .height(150.dp)
            )

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            authViewModel.onSignInResult(signInResult)
                        }
                    }
                })

            SocialMediaSignupButton(
                onClick = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }

                },
                color = Color.Black,
                text = "Sign in with Google", iconResId = R.drawable.google_black
            )
            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in Successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(Home.route)
                    authViewModel.resetState()
                }
            }

        }

    }

    @Composable
    fun SocialMediaSignupButton(
        onClick: () -> Unit,
        color: Color,
        text: String,
        iconResId: Int
    ) {
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


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
    }
}



