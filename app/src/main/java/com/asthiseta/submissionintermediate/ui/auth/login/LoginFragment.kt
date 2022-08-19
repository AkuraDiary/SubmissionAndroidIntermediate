package com.asthiseta.submissionintermediate.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asthiseta.submissionintermediate.data.preferences.DataStoreVM
import com.asthiseta.submissionintermediate.databinding.LoginFragmentBinding
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.register.RegisterFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var loginFragmentBinding: LoginFragmentBinding? = null
//    private val authVM by viewModels<AuthVM>()
//    private val dataStoreVM by viewModels<DataStoreVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginFragmentBinding = LoginFragmentBinding.inflate(inflater, container, false)
        initView()
        return loginFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
    }

    private fun initView() {
        loginFragmentBinding?.apply {
            loginButton.setOnClickListener {
                showMessage("Loging in, please wait")
                validateAndLogin()
            }
            toRegister.setOnClickListener {
                (activity as MainActivity).moveToFragment(RegisterFragment())
            }
        }
    }

    private fun doLogin() {
        val usrEmail = loginFragmentBinding?.textInputTextEmail?.text.toString().trim()
        val usrPass = loginFragmentBinding?.textInputTextPass?.text.toString().trim()
        (activity as MainActivity).saveLoginSession(usrEmail, usrPass)
    }



    private fun validateAndLogin() {
        when {
            loginFragmentBinding?.textInputTextEmail?.text!!.isBlank() -> {
                loginFragmentBinding?.textInputTextEmail?.error = "Username is required"
                return
            }
            loginFragmentBinding?.textInputTextPass?.text!!.isBlank() -> {
                loginFragmentBinding?.textInputTextPass!!.error = "Password is required"
                return
            }
        }
        //doLogin
        doLogin()


    }


    private fun showMessage(message: String) {
        FancyToast.makeText(
            requireContext(),
            message,
            FancyToast.LENGTH_SHORT,
            FancyToast.INFO,
            false
        ).show()
    }

    override fun onDetach() {
        super.onDetach()
        loginFragmentBinding = null
    }
}