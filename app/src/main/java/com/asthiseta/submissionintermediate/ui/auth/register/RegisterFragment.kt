package com.asthiseta.submissionintermediate.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asthiseta.submissionintermediate.databinding.RegisterFragmentBinding
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var registerFragmentBinding: RegisterFragmentBinding? = null
    private val authVM by viewModels<AuthVM>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerFragmentBinding = RegisterFragmentBinding.inflate(inflater, container, false)
        return registerFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
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
        val username = registerFragmentBinding?.textInputTextUsername?.text.toString().trim()
        val usrEmail = registerFragmentBinding?.textInputTextEmail?.text.toString().trim()
        val usrPass = registerFragmentBinding?.textInputTextPass?.text.toString().trim()

        authVM.doRegister(username, usrEmail, usrPass)

        (activity as MainActivity).moveToFragment(LoginFragment())
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
        //doRegister
        doRegister()

    }

    private fun showMessage(message: String){
        FancyToast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}