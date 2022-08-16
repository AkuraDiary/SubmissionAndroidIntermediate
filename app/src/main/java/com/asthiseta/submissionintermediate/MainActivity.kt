package com.asthiseta.submissionintermediate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.asthiseta.submissionintermediate.adapter.StoryAdapter
import com.asthiseta.submissionintermediate.data.preferences.UserLoginPreferences
import com.asthiseta.submissionintermediate.databinding.ActivityMainBinding
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.asthiseta.submissionintermediate.ui.home.HomeFragment
import com.asthiseta.submissionintermediate.ui.home.HomeViewModel

class MainActivity : AppCompatActivity() {
    private var mainActivityMainBinding: ActivityMainBinding? = null
    private lateinit var usrLoginPref: UserLoginPreferences
    private lateinit var homeViewModel: HomeViewModel
    lateinit var storyAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityMainBinding!!.root)
        usrLoginPref = UserLoginPreferences(this)
        supportActionBar?.hide()
        initViewModel()
        storyAdapter = StoryAdapter()
        checkSession()
    }

    fun moveToFragment(fragment: Fragment){
        this.supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

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
        if(!usrLoginPref.getLoginData().isLogin){

            moveToFragment(LoginFragment())

        }else{
            homeViewModel.apply {

                getAllStoriesData(usrLoginPref.getLoginData().token)
                listStoryData.observe(this@MainActivity) {
                    if (it != null) {
                        storyAdapter.setStoryData(it)
                    }
                }

            }
            moveToFragment(HomeFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityMainBinding = null
    }
    private fun doLogout(){
        usrLoginPref.logout()
        moveToFragment(LoginFragment())
    }

}