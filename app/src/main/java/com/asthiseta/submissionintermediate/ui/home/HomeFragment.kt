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
    private lateinit var homeBinding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel

    private lateinit var _adapter: StoryAdapter
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
        viewModel.apply {
            showLoading(true)
            getAllStoriesData((activity as MainActivity).usrLoginPref.getLoginData().token)
            listStoryData.observe(requireActivity()) {
                if (it != null) {
                    _adapter.setStoryData(it)
                    _adapter.notifyDataSetChanged()
                }
            }
            isLoading.observe(requireActivity()) { showLoading(it) }
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

    }

    private fun initView() {

        homeBinding.apply {
            rvStory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = _adapter
            }

            fabAddStory.setOnClickListener {

                val intent = Intent(requireActivity(), UploadStoryActivity::class.java)
                startActivity(intent)
                FragmentManager.POP_BACK_STACK_INCLUSIVE
                requireActivity().finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        homeBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}