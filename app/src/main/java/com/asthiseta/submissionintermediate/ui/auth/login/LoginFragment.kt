package com.asthiseta.submissionintermediate.ui.auth.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession
import com.asthiseta.submissionintermediate.data.preferences.DataStoreVM
import com.asthiseta.submissionintermediate.databinding.LoginFragmentBinding
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.register.RegisterFragment
import com.asthiseta.submissionintermediate.ui.home.HomeFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var loginFragmentBinding: LoginFragmentBinding? = null
    private val authVM by viewModels<AuthVM>()
    private val dataStoreVM by viewModels<DataStoreVM>()

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
        initVM()

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
        showLoading(true)
        val usrEmail = loginFragmentBinding?.textInputTextEmail?.text.toString().trim()
        val usrPass = loginFragmentBinding?.textInputTextPass?.text.toString().trim()
        (activity as MainActivity).saveLoginSession(usrEmail, usrPass)
//        authVM.apply {
//            doLogin(usrEmail, usrPass)
//            Log.d("MainActivity", usrLogin.toString())
//            usrLogin.observe(viewLifecycleOwner) {
//                if (it != null) {
//                    //save the login session
//                    val currentUser = UsrSession(
//                        it.userId,
//                        it.name,
//                        it.token,
//                        true
//                    )
//
//                    //save the login session
//                    Log.d("MainActivity", currentUser.toString())
//                    dataStoreVM.setLoginSession(currentUser)
//                    Log.d("MainActivity", "after dataStoreVM.setLoginSession")
//
//                }
//            }
//            AlertDialog.Builder(requireContext()).apply {
//                setTitle("Login Succesfully")
//                setMessage("Logged in as ${usrEmail}!")
//                setPositiveButton("Ok") { _, _ ->
//                    if (isAdded) {
//                        showLoading(false)
//                        (activity as MainActivity).moveToFragment(HomeFragment())
//                    }
//
//                }
//                create()
//                show()
//            }
//        }
    }

    private fun initVM() {
        authVM.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        authVM.message.observe(viewLifecycleOwner) { showMessage(it) }
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

    private fun showLoading(isLoading: Boolean) {
        (activity as MainActivity).showLoading(isLoading)
        Log.d("Login isLoading", isLoading.toString())
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