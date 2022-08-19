package com.asthiseta.submissionintermediate.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asthiseta.submissionintermediate.databinding.PagingLoadingLayoutBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PagingLoadingLayoutBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(state: LoadState) {
            if (state is LoadState.Error) {
                binding.errorMessage.text = state.error.localizedMessage
            }
            binding.apply {
                progressBar.isVisible = state is LoadState.Loading
                retryButton.isVisible = state is LoadState.Error
                errorMessage.isVisible = state is LoadState.Error
            }

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = PagingLoadingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, retry)
    }


}
