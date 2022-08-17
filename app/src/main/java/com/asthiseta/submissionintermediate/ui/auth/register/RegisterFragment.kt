package com.asthiseta.submissionintermediate.ui.auth.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asthiseta.submissionintermediate.BuildConfig
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.RegisterFragmentBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterFragment : Fragment() {
    private var registerFragmentBinding: RegisterFragmentBinding? = null
    private lateinit var authVM: AuthVM
    private lateinit var pref: SharedPreferences
    private lateinit var usrLoginPref: UserLoginPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerFragmentBinding = RegisterFragmentBinding.inflate(inflater, container, false)
        initVM()
        initPref()
        return registerFragmentBinding!!.root
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

    override fun onDetach() {
        super.onDetach()
        registerFragmentBinding = null
    }



    private fun initView(){
        registerFragmentBinding?.apply {
            registerButton.setOnClickListener{
                showMessage("Registering")
                validateAndRegister()

            }
            toLogin.setOnClickListener{
                (activity as MainActivity).moveToFragment(LoginFragment())
            }
        }
    }

    private fun doRegister(){
        showLoading(true)
        val username = registerFragmentBinding?.textInputTextUsername?.text.toString().trim()
        val usrEmail = registerFragmentBinding?.textInputTextEmail?.text.toString().trim()
        val usrPass = registerFragmentBinding?.textInputTextPass?.text.toString().trim()

        authVM.apply {
            doRegister(username, usrEmail, usrPass)
        }
        showLoading(false)

    }


    private fun initVM(){
        authVM = ViewModelProvider(requireActivity())[AuthVM::class.java]

        authVM.isLoading.observe(viewLifecycleOwner){showLoading(it)}
        authVM.message.observe(viewLifecycleOwner){showMessage(it)}
    }

    private fun validateAndRegister() {
        when {
            registerFragmentBinding?.textInputTextEmail?.text!!.isBlank() -> {
                registerFragmentBinding?.textInputTextEmail!!.error = "Username is required"
                return
            }
            registerFragmentBinding!!.textInputTextPass.text!!.isBlank() -> {
                registerFragmentBinding!!.textInputTextPass.error = "Password is required"
                return
            }
        }
        //doLogin
        doRegister()

    }

    private fun showLoading(isLoading : Boolean){
        (activity as MainActivity).showLoading(isLoading)
    }

    private fun showMessage(message: String){
        FancyToast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}