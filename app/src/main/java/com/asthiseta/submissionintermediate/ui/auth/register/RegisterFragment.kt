package com.asthiseta.submissionintermediate.ui.auth.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asthiseta.submissionintermediate.BuildConfig
import com.asthiseta.submissionintermediate.MainActivity
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.RegisterFragmentBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterFragment : Fragment() {
    private lateinit var registerFragmentBinding: RegisterFragmentBinding
    private lateinit var authVM: AuthVM
    private lateinit var pref: SharedPreferences
    lateinit var usrLoginPref: UserLoginPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerFragmentBinding = RegisterFragmentBinding.inflate(inflater, container, false)
        initVM()
        initPref()
        return registerFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
    }

    private fun initPref(){
        pref = requireContext().getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        usrLoginPref = UserLoginPreferences(requireContext())
    }



    private fun initView(){
        registerFragmentBinding.apply {
            registerButton.setOnClickListener{
                validateAndRegister()

            }
            toLogin.setOnClickListener{
                (activity as MainActivity).moveToFragment(LoginFragment())
            }
        }
    }

    private fun doRegister(){
        val username = registerFragmentBinding.textInputTextUsername.text.toString().trim()
        val usrEmail = registerFragmentBinding.textInputTextEmail.text.toString().trim()
        val usrPass =registerFragmentBinding.textInputTextPass.text.toString().trim()

        authVM.apply {
            showMessage("Loading")
            showLoading(true)
            doRegister(username, usrEmail, usrPass)
        }

    }


    private fun initVM(){
        authVM = ViewModelProvider(requireActivity())[AuthVM::class.java]

        authVM.isLoading.observe(viewLifecycleOwner){showLoading(it)}
        authVM.message.observe(viewLifecycleOwner){showMessage(it)}
    }

    private fun validateAndRegister() {
        when {
            registerFragmentBinding.textInputTextEmail.text!!.isBlank() -> {
                registerFragmentBinding.textInputTextEmail.error = "Username is required"
                return
            }
            registerFragmentBinding.textInputTextPass.text!!.isBlank() -> {
                registerFragmentBinding.textInputTextPass.error = "Password is required"
                return
            }
        }
        //doLogin
        doRegister()

    }

    private fun showLoading(isLoading : Boolean){
        registerFragmentBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String){
        FancyToast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}