package com.devapps.pamwezi.presentation.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
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
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "home menu",
                            tint = Color.Black)
                    }
                }
                )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {  },
                contentColor = Color.White,
                containerColor = Color.Black
                ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Add expense",
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

        }
    }
}
}

@Composable
fun UserBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp)
    ) {

    }
}

@Composable
@Preview(showBackground = true)
fun showClientHomePreview() {
    HomeScreen()
}