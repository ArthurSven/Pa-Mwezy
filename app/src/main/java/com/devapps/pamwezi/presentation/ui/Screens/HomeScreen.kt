package com.devapps.pamwezi.presentation.ui.Screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devapps.pamwezi.domain.model.UserData
import com.devapps.pamwezi.domain.repository.GoogleAuthClient
import com.devapps.pamwezi.presentation.ui.Components.UserBar
import com.devapps.pamwezi.presentation.ui.viewmodels.AuthViewModel
import com.devapps.pamwezi.presentation.ui.viewmodels.BudgetViewModel
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
                Spacer(modifier = Modifier
                    .height(40.dp))
                BudgetList()
            }
        }
    }
}

@Composable
fun BudgetList() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }


    val budgetViewModel: BudgetViewModel = hiltViewModel()
    val createdBy = googleAuthClient.getSignedInUser()?.username.toString()

    // Set the createdBy value in the ViewModel
    LaunchedEffect(createdBy) {
        budgetViewModel.setCreatedBy(createdBy)
    }

    // Observe the userBudgets StateFlow
    val userBudgetState by remember { budgetViewModel.userBudgets }.collectAsState(emptyList())


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        if (userBudgetState.isEmpty()) {
            item {
                Text(
                    text = "Empty Budget list",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
            }
        } else {
            items(userBudgetState) { budget ->
                BudgetListCard(
                    budgetTitle = budget.title,
                    budgetAmount = budget.amount
                )
            }
        }
    }
}

@Composable
fun BudgetListCard(
    budgetTitle: String,
    budgetAmount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(0.8f)
                .height(140.dp)
                .padding(all = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = budgetTitle,
                        color = Color.White, fontSize = 16.sp
                    )
                    Text(
                        text = "MWK: $budgetAmount",
                        color = Color.White, fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(top = 20.dp))
    }

}

@Composable
@Preview(showBackground = true)
fun showBudgetItem() {

}
