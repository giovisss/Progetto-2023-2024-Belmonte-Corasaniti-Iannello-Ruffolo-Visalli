package com.example.jokiandroid.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jokiandroid.model.User
import com.example.jokiandroid.utility.Date
import com.example.jokiandroid.viewmodel.UserViewModel

@Composable
fun EditUserDataActivity(navController: NavController, userViewModel: UserViewModel, searchedUsername: String) {
    val users = userViewModel.usersList.observeAsState()

    if(users.value == null || users.value!!.isEmpty()) {
        AlertDialog(
            onDismissRequest = { navController.popBackStack() },
            title = { Text("No user found") },
            text = { Text("There are no user to show") },
            confirmButton = {
                Button(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Go back")
                }
            }
        )
    }

    if (searchedUsername == "") {
        LazyColumn {
            itemsIndexed(users.value!!) { index: Int, user: User ->
                EditUsersListItem(
                    item = user,
                    onGameClick = { navController.navigate("edit_users/${user.username}") }
                )
            }
        }
    } else {
        EditUserDataContent(navController, userViewModel, searchedUsername)
    }
}

@Composable
fun EditUsersListItem(item : User, onGameClick: (User) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .clickable { onGameClick(item) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = item.username,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = item.email,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserDataContent(navController: NavController, userViewModel: UserViewModel, searchedUsername: String) {
    val user by userViewModel.selectedUser.observeAsState()
    var firstName: String by remember { mutableStateOf("") }
    var lastName: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf("") }
    var birthdateDay: Int by remember { mutableStateOf(1) }
    var birthdateMonth: Int by remember { mutableStateOf(1) }
    var birthdateYear: Int by remember { mutableStateOf(2000) }

    LaunchedEffect(searchedUsername) {
        userViewModel.fetchUserByUsername(searchedUsername)
    }

    LaunchedEffect(user) {
        user?.let {
            firstName = it.firstName
            lastName = it.lastName
            email = it.email
            birthdateDay = it.formattedBirthDate.day
            birthdateMonth = it.formattedBirthDate.month
            birthdateYear = it.formattedBirthDate.year
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(user?.username ?: "Edit User")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (user != null) {
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    TextField(
                        value = birthdateDay.toString(),
                        onValueChange = {
                            if (it.isEmpty()) birthdateDay = 1
                            else {
                                val old = birthdateDay

                                try {
                                    val new = it.toInt()

                                    if(new > 31) birthdateDay = 31
                                    else if (new < 1) birthdateDay = 1
                                    else birthdateDay = new
                                } catch (e: Exception) {
                                    birthdateDay = old
                                }
                            }
                        },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = birthdateMonth.toString(),
                        onValueChange = {
                            if (it.isEmpty()) birthdateMonth = 1
                            else {
                                val old = birthdateMonth

                                try {
                                    val new = it.toInt()

                                    if(new > 12) birthdateMonth = 12
                                    else if (new < 1) birthdateMonth = 1
                                    else birthdateMonth = new
                                } catch (e: Exception) {
                                    birthdateMonth = old
                                }
                            }
                        },
                        label = { Text("Month") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = birthdateYear.toString(),
                        onValueChange = {
                            if (it.isEmpty()) birthdateYear = 2000
                            else {
                                val old = birthdateYear

                                try {
                                    birthdateYear = it.toInt()
                                } catch (e: Exception) {
                                    birthdateYear = old
                                }
                            }
                        },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        userViewModel.updateUser(User(user!!.id, firstName, lastName, user!!.username, email, Date(birthdateDay, birthdateMonth, birthdateYear).toString()))

                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("User not found or error loading data")
                    Button(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Go back")
                    }
                }
            }
        }
    }
}