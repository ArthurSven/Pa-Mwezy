package com.devapps.pamwezi.presentation.ui.Screens

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devapps.pamwezi.domain.model.BudgetLocal
import com.devapps.pamwezi.domain.model.UserData
import com.devapps.pamwezi.domain.repository.GoogleAuthClient
import com.devapps.pamwezi.presentation.ui.Components.UserBar
import com.devapps.pamwezi.presentation.ui.viewmodels.AuthViewModel
import com.devapps.pamwezi.presentation.ui.viewmodels.BudgetViewModel
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            BudgetCard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetCard() {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val authViewModel = viewModel<AuthViewModel>()




    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val username = googleAuthClient.getSignedInUser()?.username.toString()

    val budgetViewModel: BudgetViewModel = hiltViewModel()

    val state by budgetViewModel.state.collectAsState()

    val months = arrayOf<String>(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var selectedMonth = remember { mutableStateOf(months[0]) }
    val expanded = remember { mutableStateOf(false) }

    val month = selectedMonth.value

    var budgetTitle by remember {
        mutableStateOf("")
    }

    var monthlyBudget by remember {
        mutableStateOf(0.0)
    }

    var monthAdded by remember {
        mutableStateOf(month)
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
                    Text(
                        text = "Budget title",
                        color = Color.Black
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .border(width = 2.dp, color = Color.LightGray)
                    .background(color = Color.LightGray),
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
                value = monthlyBudget.toString(),
                onValueChange = {
                    monthlyBudget = it.toDoubleOrNull() ?: 0.0
                },
                placeholder = {
                    Text(
                        text = "Budget amount",
                        color = Color.Black
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .border(width = 2.dp, color = Color.LightGray)
                    .background(color = Color.LightGray),
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
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {

                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    },
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxWidth(),
                ) {
                    TextField(
                        value = selectedMonth.value,
                        onValueChange = {
                            selectedMonth.value = it
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .background(color = Color.White)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        modifier = Modifier
                            .background(color = Color.White)
                    ) {
                        months.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it,
                                        color = Color.Black
                                    )
                                },
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
                    .height(20.dp)
            )
            Button(


                onClick = {
                    if (budgetTitle.isNotEmpty() && monthlyBudget.toString().isNotEmpty()) {
                        val budgetLocal = BudgetLocal(
                            title = budgetTitle,
                            amount = monthlyBudget,
                            month = monthAdded,
                            createdBy = createdBy
                        )

                        coroutineScope.launch {
                            budgetViewModel.insertBudget(budgetLocal)
                        }
                        budgetTitle = ""
                        monthlyBudget = 0.0
                        selectedMonth =  mutableStateOf(months[0])
                    } else {
                        Toast.makeText(
                            context,
                            "Ensure that the title is not empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    // Call insertBudget in the ViewModel
                    coroutineScope.launch {

                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.White),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(text = "Create Budget")
            }
            Spacer(
                modifier = Modifier
                    .height(30.dp)
            )
            LaunchedEffect(key1 = state.isInsertedBudgetSuccessful) {
                if (state.isInsertedBudgetSuccessful) {
                    Toast.makeText(
                        context,
                        "Budget successfully added",
                        Toast.LENGTH_LONG
                    ).show()
                    budgetViewModel.resetState()
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailScreen(
    budgetId: Int?,
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController) {
    val showMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val budgetViewModel: BudgetViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    // Apply LaunchedEffect for asynchronous database access

    val selectedBudget = remember { mutableStateOf<BudgetLocal?>(null) }

    LaunchedEffect(budgetId) {
        if (budgetId != null) {
            val budget = budgetViewModel.getBudgetByBudgetId(budgetId)

            // Update the selectedBudget value
            selectedBudget.value = budget
        }
    }


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
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onSignOut()
                },
                contentColor = Color.White,
                containerColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Add monthly budget",
                )
                Text(text = "Add Expense")
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
                .height(20.dp))
            // State to track whether the delete confirmation dialog is open
            var isDialogOpen by remember { mutableStateOf(false) }

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
                        .height(150.dp) // Reduced height
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp,
                            start = 20.dp,
                            end = 20.dp
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 10.dp, start = 20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (selectedBudget != null) {
                                Text(
                                    text = "${selectedBudget.value?.title}",
                                    color = Color.White, fontSize = 20.sp
                                )
                                Text(
                                    text = "MWK: ${selectedBudget.value?.amount}",
                                    color = Color.White, fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "Budget not available",
                                    color = Color.White, fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier
                .height(20.dp))
            Text(text = "Recent Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 20.dp))
            ExpenseCard()
            ExpenseCard()
        }
    }
}

@Composable
fun ExpenseCard() {
        // State to track whether the delete confirmation dialog is open
        var isDialogOpen by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(0.8f)
                    .height(100.dp) // Reduced height
                    .padding(
                        top = 10.dp,
                        bottom = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    ) // Adjusted padding,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.60f)
                            .padding(top = 10.dp, start = 20.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Apple",
                            color = Color.Black, fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "14/12/2023",
                            color = Color.Black, fontSize = 16.sp,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.40f)
                            .padding(top = 30.dp, end = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "MWK: 6000",
                            color = Color.Black, fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

@Composable
@Preview(showBackground = true)
fun ViewTheUI() {

}

