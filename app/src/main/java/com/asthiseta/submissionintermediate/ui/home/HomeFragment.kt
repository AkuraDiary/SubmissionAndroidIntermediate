package com.asthiseta.submissionintermediate.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asthiseta.submissionintermediate.MainActivity
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.HomeFragmentBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.shashank.sony.fancytoastlib.FancyToast

class HomeFragment : Fragment() {
    private lateinit var homeBinding: HomeFragmentBinding
    lateinit var viewModel: HomeViewModel
    lateinit var usrLoginPref: UserLoginPreferences
    lateinit var _adapter : StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usrLoginPref = UserLoginPreferences(requireContext())
        _adapter = StoryAdapter()
        checkSession()
        initViewModel()
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

    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        viewModel.apply {
            getAllStoriesData(usrLoginPref.getLoginData().token)
            listStoryData.observe(requireActivity()){
                if (it != null) {
                    _adapter.setStoryData(it)
                }
            }
            isLoading.observe(requireActivity()) { showLoading(it) }
            message.observe(requireActivity()) { showMessage(it) }
        }
    }
    private fun initView(){

        homeBinding.apply {
            rvStory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = _adapter
            }
        }
//        homeBinding.logoutButton.setOnClickListener{

//        }
    }
    private fun showLoading(isLoading : Boolean){
        homeBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String){
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }

    private fun doLogout(){
        usrLoginPref.logout()
        (activity as MainActivity).moveToFragment(LoginFragment())
    }
}