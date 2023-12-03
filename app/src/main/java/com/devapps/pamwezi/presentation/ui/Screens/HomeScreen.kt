package com.devapps.pamwezi.presentation.ui.Screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.devapps.pamwezi.R
import com.devapps.pamwezi.domain.model.SignInState
import com.devapps.pamwezi.domain.model.UserData
import com.devapps.pamwezi.domain.repository.GoogleAuthClient
import com.devapps.pamwezi.presentation.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
    val showMenu = remember { mutableStateOf(false) }
    val clientNavController = rememberNavController()
    NavHost(clientNavController, startDestination = Home.route) {
        composable(Home.route) {
            googleAuthClient.getSignedInUser()?.let { it1 ->
                HomeScreen(
                    it1,
                    onSignOut = {
                        coroutineScope.launch {
                            googleAuthClient.signOut()
                        }
                    })
            }
        }
        composable(AddBudget.route) {
            AddBudgetScreen(
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
Surface {
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
                            clientNavController.navigate(SignOutUser.route)
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
                          clientNavController.navigate(AddBudget.route)
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
fun UserBar(userData: UserData?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        if(userData?.userProfileUrl != null) {
            val req = ImageRequest.Builder(LocalContext.current)
                .data(userData.userProfileUrl)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .allowHardware(false)
                .build()
            AsyncImage(
                model = req,
                contentDescription = "${userData.username}'s profile picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier
                .width(20.dp))
            Column {
                Text(text = userData.username.toString(),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Keep track of your expenses",
                    color = Color.Gray,
                    fontSize = 14.sp)
            }


        } else {
            Image(
                painter = painterResource(id = R.drawable.nodp),
                contentDescription = "No display profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier
                .width(20.dp))
            Column {
                Text(text = userData?.username.toString(),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Keep track of your expenses, plan well",
                    color = Color.Gray,
                    fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
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
    val showMenu = remember { mutableStateOf(false) }
    val clientNavController = rememberNavController()
    NavHost(clientNavController, startDestination = Home.route) {
        composable(Home.route) {
            googleAuthClient.getSignedInUser()?.let { it1 ->
                HomeScreen(
                    it1,
                    onSignOut = {
                        coroutineScope.launch {
                            googleAuthClient.signOut()
                        }
                    })
            }
        }
        composable(SignOutUser.route) {
            LaunchedEffect(Unit) {
                onSignOut()
            }
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
                                     clientNavController.navigate(Home.route)
                                 }) {
                                     Icon(
                                         imageVector =Icons.Default.Close ,
                                         contentDescription = "exit screen icon")
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
                            clientNavController.navigate(SignOutUser.route)
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
        mutableStateOf(0.0)
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
            .padding(all = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .height(60.dp)
            )
            Text(
                text = "Create Monthly budget",
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
                value = monthlyBudget.toString(),
                onValueChange = {
                    monthlyBudget.absoluteValue
                },
                placeholder = {
                    Text(text = "Budget amount",
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
            Box(
                modifier = Modifier
                    .padding(start = 30.dp)
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



@Composable
@Preview(showBackground = true)
fun ShowClientHomePreview() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val googleAuthClient by lazy {
        GoogleAuthClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    googleAuthClient.getSignedInUser()?.let {
        HomeScreen(userData = it,
            onSignOut = {
                coroutineScope.launch {
                    googleAuthClient.signOut()
                }
            })
    }
}