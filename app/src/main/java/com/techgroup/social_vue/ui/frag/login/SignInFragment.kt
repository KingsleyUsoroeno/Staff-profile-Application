package com.techgroup.social_vue.ui.frag.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.techgroup.social_vue.R
import com.techgroup.social_vue.databinding.FragmentSignInBinding

/**
 * A simple [Fragment] subclass.
 *
 */
class SignInFragment : Fragment(), View.OnClickListener {

    private lateinit var viewBinding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.tvCreateAccount.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_create_account -> view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}
