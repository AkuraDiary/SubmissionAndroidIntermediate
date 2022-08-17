package com.asthiseta.submissionintermediate.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asthiseta.submissionintermediate.MainActivity
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.databinding.HomeFragmentBinding
import com.asthiseta.submissionintermediate.ui.addStory.UploadStoryActivity

class HomeFragment : Fragment() {
    private  var homeBinding: HomeFragmentBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(layoutInflater)

        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyAdapter = StoryAdapter()
        initViewModel()
        (activity as MainActivity).supportActionBar?.apply {
            title = "STORIES"
            show()
        }

        initView()
    }
    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        homeViewModel.apply {

            getAllStoriesData((activity as MainActivity).usrLoginPref.getLoginData().token)
            listStoryData.observe(requireActivity()) {
                if (it != null) {
                    storyAdapter.setStoryData(it)
                }
            }

        }
    }





    private fun initView() {

        homeBinding?.apply {
            rvStory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = storyAdapter
            }

            fabAddStory.setOnClickListener {

                val intent = Intent(requireActivity(), UploadStoryActivity::class.java)
                startActivity(intent)
                FragmentManager.POP_BACK_STACK_INCLUSIVE
                requireActivity().finish()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        homeBinding = null
    }

}