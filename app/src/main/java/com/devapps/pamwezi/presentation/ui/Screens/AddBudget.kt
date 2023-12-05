package com.devapps.pamwezi.presentation.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.devapps.pamwezi.presentation.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudget(
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

    val budgetNavController = rememberNavController()
    NavHost(budgetNavController, startDestination = AddBudget.route) {
        composable(AddBudget.route) {
            googleAuthClient.getSignedInUser()?.let { it1 ->
                AddBudgetScreen(
                    it1,
                    onSignOut = {
                        coroutineScope.launch {
                            googleAuthClient.signOut()
                        }
                    },
                    budgetNavController)
            }
        }
        composable(Home.route) {
            HomeScreen(
                userData,
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
fun AddBudgetScreen(
    userData: UserData,
    onSignOut: () -> Unit,
    navController: NavController
) {
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
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Home.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack ,
                            contentDescription = "exit screen icon",
                            tint = Color.Black
                        )
                    }
                },
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
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            )
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
            Spacer(modifier = Modifier
                .height(40.dp))
            BudgetCard(userData)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard(userData: UserData?) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authViewModel = viewModel<AuthViewModel>()

    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val username = userData?.username.toString()

    val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var selectedMonth = remember { mutableStateOf(months[0]) }
    val expanded = remember { mutableStateOf(false) }

    var budgetTitle by remember {
        mutableStateOf("")
    }

    var monthlyBudget by remember {
        mutableStateOf("Budget amount")
    }

    var monthAdded by remember {
        mutableStateOf("")
    }

    var createdBy by remember {
        mutableStateOf(username)
    }



    ElevatedCard(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .padding(all = 20.dp)
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        ) {
            Spacer(
                modifier = Modifier
                    .height(60.dp)
            )
            Text(
                text = "Plan Budget",
                color = Color.Black,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            OutlinedTextField(
                value = budgetTitle,
                onValueChange = {
                    budgetTitle = it
                },
                placeholder = {
                    Text(text = "Budget title",
                        color = Color.Black)
                },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
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
                value = monthlyBudget,
                onValueChange = {
                    monthlyBudget = it
                },
                placeholder = {
                    Text(text = "Budget amount",
                        color = Color.Black)
                },
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
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
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .background(color = Color.White)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    }) {
                    TextField(
                        value = selectedMonth.value,
                        onValueChange = {
                            selectedMonth.value = it
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        months.forEach {
                            DropdownMenuItem(
                                text = { Text(text = it) },
                                onClick = {
                                    selectedMonth.value = it
                                    expanded.value = false
                                })
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
        }
    }
}
