package com.devapps.pamwezi.presentation.ui.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapps.pamwezi.domain.model.UserData
import com.devapps.pamwezi.domain.repository.GoogleAuthClient
import com.devapps.pamwezi.presentation.ui.Components.UserBar
import com.devapps.pamwezi.presentation.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authViewModel = viewModel<AuthViewModel>()

    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val clientNavController = rememberNavController()
    NavHost(clientNavController, startDestination = Home.route) {
        composable(Home.route) {

                HomeContent(
                    userData = googleAuthClient.getSignedInUser(),
                    onSignOut = {
                        coroutineScope.launch {
                            googleAuthClient.signOut()
                        }
                    },
                    clientNavController)


        }
        composable(AddBudget.route) {
                AddBudget(
                   userData = googleAuthClient.getSignedInUser(),
                    onSignOut = {
                        coroutineScope.launch {
                            googleAuthClient.signOut()
                        }
                    })

        }
        composable(SignOutUser.route) {
            LaunchedEffect(Unit) {
                onSignOut()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController
) {
    Surface {
        val showMenu = remember { mutableStateOf(false) }
        Scaffold(
            containerColor = Color.White,
            contentColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Pa Mwezy",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black)
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        Color.White
                    ),
                    actions = {
                        IconButton(onClick = { showMenu.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "home menu",
                                tint = Color.Black)
                        }
                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false },
                            modifier = Modifier
                                .background(color = Color.White)
                                .width(80.dp)
                        ) {
                            DropdownMenuItem(text = {
                                Text(text = "Logout",
                                    color = Color.Black)
                            }, onClick = {
                                navController.navigate(SignOutUser.route)
                                onSignOut()
                            })
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        Log.d("Navigation", "Navigating to ${AddBudget.route}")
                        navController.navigate(AddBudget.route)
                    },
                    contentColor = Color.White,
                    containerColor = Color.Black
                ) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Add monthly budget",
                    )
                    Text(text = "Add Budget")
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .background(color = Color.LightGray)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier
                    .height(20.dp))
                UserBar(userData)
            }
        }
    }
}

@Composable
fun BudgetListCard() {

}

@Composable
@Preview(showBackground = true)
fun showBudgetItem() {

}
