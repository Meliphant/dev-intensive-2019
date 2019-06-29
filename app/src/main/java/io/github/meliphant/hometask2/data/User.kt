package io.github.meliphant.hometask2.data

import java.util.*

data class User(
        val id: String,
        var firstName: String?,
        var lastName: String?,
        var avatar: String? = null,
        var rating: Int = 0,
        var respect: Int = 0,
        var lastVisit: Date? = Date(),
        var isOnline: Boolean = false
) {

    init {
        if (firstName.isNullOrEmpty() && lastName.isNullOrEmpty()) {
            println("User name is empty")
        } else {
            println("User name is ${if (firstName.isNullOrEmpty()) "" else firstName}${if (lastName.isNullOrEmpty()) "" else " " + lastName}")
        }
    }

    companion object Factory {

        private var lastId = -1

        fun makeUser(fullName: String?): User {
            lastId++
            var firstName: String? = null
            var lastName: String? = null
            fullName?.split(" ")?.let {
                firstName = it.getOrNull(0)
                lastName = it.getOrNull(1)
            }
            return User(id = "$lastId", firstName = firstName, lastName = lastName)
        }

    }
}