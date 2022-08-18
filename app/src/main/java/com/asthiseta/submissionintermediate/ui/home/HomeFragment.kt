package com.asthiseta.submissionintermediate.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asthiseta.submissionintermediate.ui.activities.MainActivity
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.databinding.HomeFragmentBinding
import com.asthiseta.submissionintermediate.ui.addStory.UploadStoryActivity
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var homeBinding: HomeFragmentBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()
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

        (activity as MainActivity).supportActionBar?.apply {
            title = "STORIES"
            show()
        }
        initView()

        initViewModel()

    }

    private fun initViewModel() {

        homeViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        homeViewModel.message.observe(viewLifecycleOwner) { showMessage(it) }

        homeViewModel.stories.observe(viewLifecycleOwner){
            storyAdapter.submitData(lifecycle, it)
        }

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

    private fun showLoading(isLoading: Boolean) {
        (activity as MainActivity).showShimmerLoading(isLoading)
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