package com.asthiseta.submissionintermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.asthiseta.submissionintermediate.databinding.ActivityMainBinding
import com.asthiseta.submissionintermediate.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityMainBinding.root)
        supportActionBar?.hide()
        moveToFragment(HomeFragment())
    }

    fun moveToFragment(fragment: Fragment){
        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}