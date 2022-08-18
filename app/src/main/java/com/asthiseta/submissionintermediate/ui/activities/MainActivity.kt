package com.asthiseta.submissionintermediate.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.asthiseta.submissionintermediate.R
import com.asthiseta.submissionintermediate.data.preferences.DataStoreVM
import com.asthiseta.submissionintermediate.databinding.ActivityMainBinding
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.asthiseta.submissionintermediate.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var mainActivityMainBinding: ActivityMainBinding? = null
    private val dataStoreVM by viewModels<DataStoreVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityMainBinding!!.root)

        supportActionBar?.hide()
        checkSession()
    }

    fun moveToFragment(fragment: Fragment){
        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logoutMenu -> {
                doLogout()
                true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    private fun checkSession(){
        dataStoreVM.getLoginSession().observe(this){
            if(!it.isLogin){
                moveToFragment(LoginFragment())
            }else{
                moveToFragment(HomeFragment())
            }
        }
    }

    fun showLoading(isLoading : Boolean){
        mainActivityMainBinding?.progressBar!!.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    fun showShimmerLoading(isLoading : Boolean){
        mainActivityMainBinding?.progressBarShimmer?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityMainBinding = null
    }
    private fun doLogout(){
        dataStoreVM.logout()
        moveToFragment(LoginFragment())
    }

}