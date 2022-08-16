package com.asthiseta.submissionintermediate.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.asthiseta.submissionintermediate.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity(){
    private var detailBinding: ActivityDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding!!.root)
        supportActionBar?.hide()
        initView()
        showDetail()
    }

    private fun initView(){
        detailBinding?.fabShowInfo?.setOnClickListener {
            detailBinding?.detailStoryContainer!!.visibility = View.VISIBLE
        }

        detailBinding?.tvDetailClose?.setOnClickListener{
            detailBinding?.detailStoryContainer!!.visibility = View.GONE
        }
    }

    private fun showDetail(){
        val name = intent.getStringExtra("NAME")
        val desc = intent.getStringExtra("DESC")
        val img = intent.getStringExtra("IMAGE")

        detailBinding?.apply {
            tvStoryDesc.text = desc
            tvStoryTitle.text = name
            Glide.with(this@DetailActivity)
                .load(img)
                .into(storyImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailBinding = null
    }
}