package com.techgroup.social_vue.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.techgroup.social_vue.R

/**
 * A simple [Fragment] subclass.
 *
 */
class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val tvCreateAccount = view.findViewById<TextView>(R.id.tv_create_account)

        val user = arguments?.let {
            Log.i("SignInFragment", "User Details is $it")
            SignInFragmentArgs.fromBundle(it)
        }

        return view
    }
}
