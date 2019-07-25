package com.techgroup.social_vue.auth


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.techgroup.social_vue.R
import com.techgroup.social_vue.auth.data.User
import com.techgroup.social_vue.databinding.FragmentSignUpBinding


/**
 * A simple [Fragment] subclass.
 *
 */
class SignUpFragment : Fragment() {

    private lateinit var signUpFragmentBinding: FragmentSignUpBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signUpFragmentBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        signUpFragmentBinding.btnSignUp.setOnClickListener {
            createNewAccount()
        }

        return signUpFragmentBinding.root
    }

    private fun createNewAccount() {
        val firstName = signUpFragmentBinding.firstNameEditText.text.toString().trim()
        val lastName = signUpFragmentBinding.lastNameEditText.text.toString().trim()
        val username = signUpFragmentBinding.usernameEditText.text.toString().trim()
        val email = signUpFragmentBinding.emailEditText.text.toString().trim()
        val password = signUpFragmentBinding.passwordEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Something is Not right", Toast.LENGTH_LONG).show()
            return
        }
        val user = User(firstName, lastName, username, email, password)

        Handler().postDelayed({
            navController.navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment().setUsers(user))

        }, 4000)
    }


}
