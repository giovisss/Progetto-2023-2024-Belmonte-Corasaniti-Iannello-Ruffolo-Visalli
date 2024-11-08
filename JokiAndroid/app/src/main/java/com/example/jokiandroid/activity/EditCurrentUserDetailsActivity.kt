package com.example.jokiandroid.activity

import android.util.Log
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun EditCurrentUserDataActivity(navController: NavController, userViewModel: UserViewModel) {
    if (TokenManager.getToken() == null) {
        AlertDialog(
            onDismissRequest = { /* Non fare nulla, l'utente deve effettuare il login */ },
            title = { Text("Login richiesto") },
            text = { Text("Per visualizzare la pagina devi effettuare il login") },
            confirmButton = {
                Button(
                    onClick = { navController.navigate("home") }
                ) {
                    Text("Torna alla home")
                }
            }
        )
    } else {
        val user = userViewModel.currentUser.observeAsState()

        if (user.value == null || user.value!!.username.isEmpty()) {
            AlertDialog(
                onDismissRequest = { navController.popBackStack() },
                title = { Text("Nessun utente trovato") },
                text = { Text("Non ci sono informazioni sull'utente") },
                confirmButton = {
                    Button(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Go back")
                    }
                }
            )
        } else {
            EditCurrentUserDataContent(navController, userViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCurrentUserDataContent(navController: NavController, userViewModel: UserViewModel) {
    val user by userViewModel.currentUser.observeAsState()
    var firstName: String by remember { mutableStateOf("") }
    var lastName: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf("") }
    var birthdateDay: Int by remember { mutableStateOf(1) }
    var birthdateMonth: Int by remember { mutableStateOf(1) }
    var birthdateYear: Int by remember { mutableStateOf(2000) }

    LaunchedEffect(user) {
        user?.let { userViewModel.fetchUserByUsername(it.username) }
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
            Log.d("EditCurrentUserDataActivity", "User: $user")
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
                        userViewModel.updateCurrentUser(User(user!!.id, firstName, lastName, user!!.username, email, Date(birthdateDay, birthdateMonth, birthdateYear).toString()))
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Salva")
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