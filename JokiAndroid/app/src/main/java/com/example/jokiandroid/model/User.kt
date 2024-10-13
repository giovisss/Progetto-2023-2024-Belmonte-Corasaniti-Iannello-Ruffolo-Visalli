package com.example.jokiandroid.model

import com.example.jokiandroid.utility.Date
import com.example.jokiandroid.viewmodel.CurrentUserViewModel
import java.util.UUID

class User {
    var id: UUID
    var firstName: String
    var lastName: String
    var username: String
    var email: String
    var birthdate: String
    var formattedBirthDate: Date

    constructor(){
        this.id = UUID.randomUUID()
        this.firstName = "firstName"
        this.lastName = "lastName"
        this.username = "username"
        this.email = "email"
        this.birthdate = "Jan 1, 1000, 12:00:00 AM"
        this.formattedBirthDate = Date(this.birthdate)
    }

    constructor(user: User) {
        this.id = user.id
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.username = user.username
        this.email = user.email
        this.birthdate = user.birthdate
        this.formattedBirthDate = Date(this.birthdate)
    }

    constructor(id: UUID, firstName: String, lastName: String, username: String, email: String, birthdate: String) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.username = username
        this.email = email
        this.birthdate = birthdate
        this.formattedBirthDate = Date(this.birthdate)
    }

    override fun toString(): String {
        if (CurrentUserViewModel.isAdmin.value == true) return "Admin(id=$id, username='$username', email='$email')"

        return "User(id=$id, firstName='$firstName', lastName='$lastName', username='$username', email='$email', birthDate=$birthdate, formattedBirthDate=$formattedBirthDate)"
    }
}