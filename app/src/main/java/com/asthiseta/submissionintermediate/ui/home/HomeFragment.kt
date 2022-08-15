package com.asthiseta.submissionintermediate.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asthiseta.submissionintermediate.MainActivity
import com.asthiseta.submissionintermediate.R
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.HomeFragmentBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.shashank.sony.fancytoastlib.FancyToast

class HomeFragment : Fragment() {
    private lateinit var homeBinding: HomeFragmentBinding
    lateinit var viewModel: HomeViewModel

    lateinit var _adapter : StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        _adapter = StoryAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(layoutInflater)
        initViewModel()
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.apply {
            title = "STORIES"
            show()
        }

        initView()
    }



    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        viewModel.apply {
            getAllStoriesData((activity as MainActivity).usrLoginPref.getLoginData().token)
            listStoryData.observe(requireActivity()){
                if (it != null) {
                    _adapter.setStoryData(it)
                }
            }
            isLoading.observe(requireActivity()) { showLoading(it) }
            //message.observe(requireActivity()) { (activity as MainActivity).showMessage(it) }
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
    }
    private fun showLoading(isLoading : Boolean){
        homeBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}