package com.techgroup.social_vue.ui.frag.login

interface LoginView {

    fun getEmail(): String

    fun getFullName(): String

    fun showFullNameError(error: String)

    fun showEmailError(error: String)

    fun showLoginError(error: String)

}