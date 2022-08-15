package com.asthiseta.submissionintermediate.ui.detail

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.asthiseta.submissionintermediate.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(){
    private lateinit var detailBinding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        supportActionBar?.hide()
        initView()
        showDetail()
    }

    private fun initView(){
        detailBinding.fabShowInfo.setOnClickListener {
            detailBinding.detailStoryContainer.visibility = View.VISIBLE
        }

        detailBinding.tvDetailClose.setOnClickListener{
            detailBinding.detailStoryContainer.visibility = View.GONE
        }
    }

    private fun showDetail(){
        val name = intent.getStringExtra("NAME")
        val desc = intent.getStringExtra("DESC")
        val img = intent.getStringExtra("IMAGE")

        detailBinding.apply {
            tvStoryDesc.text = desc
            tvStoryTitle.text = name
            Glide.with(this@DetailActivity)
                .load(img)
                .into(storyImage)
        }
    }
}