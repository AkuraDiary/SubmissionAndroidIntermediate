package com.asthiseta.submissionintermediate.ui.auth.login

import android.app.AlertDialog
import android.content.Context

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asthiseta.submissionintermediate.BuildConfig.PREF_NAME
import com.asthiseta.submissionintermediate.MainActivity
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.LoginFragmentBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.register.RegisterFragment
import com.asthiseta.submissionintermediate.ui.home.HomeFragment
import com.shashank.sony.fancytoastlib.FancyToast

class LoginFragment : Fragment() {
    private lateinit var loginFragmentBinding: LoginFragmentBinding
    private lateinit var authVM: AuthVM
    private lateinit var pref: SharedPreferences
    lateinit var usrLoginPref: UserLoginPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginFragmentBinding = LoginFragmentBinding.inflate(inflater, container, false)
        initVM()
        initPref()
        return loginFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
    }

    private fun initPref(){
        pref = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        usrLoginPref = UserLoginPreferences(requireContext())
    }



    private fun initView(){
        loginFragmentBinding.apply {
            loginButton.setOnClickListener{
                validateAndLogin()
            }
            toRegister.setOnClickListener{
                (activity as MainActivity).moveToFragment(RegisterFragment())
            }
        }
    }

    private fun doLogin(){
        val usrEmail = loginFragmentBinding.textInputTextEmail.text.toString().trim()
        val usrPass =loginFragmentBinding.textInputTextPass.text.toString().trim()

        authVM.apply {
            doLogin(usrEmail, usrPass)
            usrLogin.observe(viewLifecycleOwner){
                showLoading(true)
                showMessage("Loging in, please wait")
                if (it != null){
                    //save the login session
                    usrLoginPref.setUsrLogin(UsrSession(
                        it.name,
                        it.token,
                        it.userId,
                        true
                    ))

                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Login Succesfully")
                        setMessage("Logged in as ${it.name}!")
                        setPositiveButton("Ok") { _, _ ->
                            (activity as MainActivity).moveToFragment(HomeFragment())
                        }
                        create()
                        show()
                    }
                }

            }
        }

    }

    private fun initVM(){
        authVM = ViewModelProvider(requireActivity())[AuthVM::class.java]

        authVM.isLoading.observe(viewLifecycleOwner){showLoading(it)}
        authVM.message.observe(viewLifecycleOwner){showMessage(it)}
    }

    private fun validateAndLogin() {
        when {
            loginFragmentBinding.textInputTextEmail.text!!.isBlank() -> {
                loginFragmentBinding.textInputTextEmail.error = "Username is required"
                return
            }
            loginFragmentBinding.textInputTextPass.text!!.isBlank() -> {
                loginFragmentBinding.textInputTextPass.error = "Password is required"
                return
            }
        }
        //doLogin
        doLogin()

    }

    private fun showLoading(isLoading : Boolean){
        loginFragmentBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String){
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }
}