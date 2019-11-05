package com.techgroup.social_vue.ui.frag.sign_up

interface SignUpView {

    fun getFullName(): String

    fun getEmailAddress(): String

    fun getStateOfOrigin(): String

    fun getDob(): String

    fun getUserImage(): String?

    fun showError(error: String)

    fun showSuccessMsg(error: String)

}