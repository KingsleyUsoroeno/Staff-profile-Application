package com.techgroup.social_vue.ui.frag.sign_up

import com.techgroup.social_vue.Common
import com.techgroup.social_vue.data.StaffViewModel
import com.techgroup.social_vue.data.model.Staff


class SignUpPresenter(
    private val signUpView: SignUpView,
    private val staffViewModel: StaffViewModel
) {


    fun saveUser() {
        val fullName = signUpView.getFullName()
        if (fullName.isEmpty()) {
            signUpView.showError("Full Name is not provided")
            return
        }

        val email = signUpView.getEmailAddress()
        if (email.isEmpty() || !Common.isEmailValid(email)) {
            signUpView.showError("email is not provided or email is invalid")
            return
        }

        val dob = signUpView.getDob()
        if (dob.isEmpty()) {
            signUpView.showError("Date of birth is required")
            return
        }

        val stateOfOrigin = signUpView.getStateOfOrigin()
        if (stateOfOrigin.isEmpty()) {
            signUpView.showError("state of origin is required")
            return
        }

        val imageUrl = signUpView.getUserImage()
        if (imageUrl!!.isEmpty()) {
            signUpView.showError("Please select a profile image")
            return
        }
        val staff = Staff(0, fullName, email, stateOfOrigin, dob, imageUrl)
        staffViewModel.insertStaff(staff)
        signUpView.showSuccessMsg("Credentials saved successfully")
    }
}