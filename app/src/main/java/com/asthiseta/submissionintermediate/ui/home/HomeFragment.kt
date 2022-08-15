package com.asthiseta.submissionintermediate.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asthiseta.submissionintermediate.MainActivity
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.HomeFragmentBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment

class HomeFragment : Fragment() {
    private lateinit var homeBinding: HomeFragmentBinding

    lateinit var usrLoginPref: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usrLoginPref = UserLoginPreferences(requireContext())
        checkSession()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(layoutInflater)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun checkSession(){
        if(!usrLoginPref.getLoginData().isLogin){
            (activity as MainActivity).moveToFragment(LoginFragment())
        }
    }

    private fun initView(){
//        homeBinding.logoutButton.setOnClickListener{
//            usrLoginPref.logout()
//            (activity as MainActivity).moveToFragment(LoginFragment())
//        }
    }

    private fun doLogout(){

    }
}