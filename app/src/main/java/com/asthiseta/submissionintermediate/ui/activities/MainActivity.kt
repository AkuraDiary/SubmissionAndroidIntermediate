package com.asthiseta.submissionintermediate.ui.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.asthiseta.submissionintermediate.R
import com.asthiseta.submissionintermediate.data.model.auth.UsrSession
import com.asthiseta.submissionintermediate.data.preferences.DataStoreVM
import com.asthiseta.submissionintermediate.databinding.ActivityMainBinding
import com.asthiseta.submissionintermediate.ui.auth.AuthVM
import com.asthiseta.submissionintermediate.ui.auth.login.LoginFragment
import com.asthiseta.submissionintermediate.ui.home.HomeFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var mainActivityMainBinding: ActivityMainBinding? = null
    private val dataStoreVM by viewModels<DataStoreVM>()
    private val authVM by viewModels<AuthVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityMainBinding!!.root)

        supportActionBar?.hide()
        initVM()
        checkSession()
    }


    private fun initVM() {
        authVM.isLoading.observe(this) { showLoading(it) }
        authVM.message.observe(this) { showMessage(it) }
    }

    private fun showMessage(message: String?) {
        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false)

    }

    fun moveToFragment(fragment: Fragment) {
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
        return when (item.itemId) {
            R.id.logoutMenu -> {
                doLogout()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun checkSession() {
        dataStoreVM.getLoginSession().observe(this) {

            if (!it.isLogin) {
                moveToFragment(LoginFragment())
            } else {
                moveToFragment(HomeFragment())
            }
        }
    }

    fun saveLoginSession(email: String, pass: String) {
        authVM.apply {
            showLoading(true)
            doLogin(email, pass)
            usrLogin.observe(this@MainActivity) {
                if (it != null) {
                    val currentUser = UsrSession(
                        it.userId,
                        it.name,
                        it.token,
                        true
                    )
                    //save the login session
                    dataStoreVM.setLoginSession(currentUser)
                }
                showLoading(false)

            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        Log.d("MainActivity : ", "isLoading state : $isLoading")
        mainActivityMainBinding?.progressBar!!.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    fun showShimmerLoading(isLoading: Boolean) {
        Log.d("MainActivity Shimmer Loading: ", "isLoading state : $isLoading")
        mainActivityMainBinding?.progressBarShimmer?.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityMainBinding = null
    }

    private fun doLogout() {
        dataStoreVM.logout()
        moveToFragment(LoginFragment())
    }

}