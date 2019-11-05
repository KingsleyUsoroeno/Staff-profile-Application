package com.techgroup.social_vue

import java.util.regex.Pattern

object Common {

    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(emailPattern).matcher(email).matches()
    }
}