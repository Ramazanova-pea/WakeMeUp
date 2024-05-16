package com.example.wakemeup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.wakemeup.databinding.ActivityMainBinding
import com.example.wakemeup.ui.authentication.AuthenticationStateAdapter
import com.example.wakemeup.ui.authentication.AuthenticationViewPagerFragment
import com.example.wakemeup.ui.authentication.login.LoginFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var currentFragmentId: Int = R.id.navigation_alarms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
             supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!! as NavHostFragment
        navController = navHostFragment.navController
        AppBarConfiguration(
            setOf(
                R.id.navigation_alarms, R.id.navigation_friends, R.id.navigation_rooms
            )
        )
        navController = navHostFragment.navController
        setupNavController()
        if (savedInstanceState != null) {
            currentFragmentId = savedInstanceState.getInt("currentFragmentId")
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_alarms, R.id.navigation_friends, R.id.navigation_rooms -> navView.visibility =
                    View.VISIBLE

                else -> navView.visibility = View.GONE
            }
        }

        if (isNetworkAvailable(this)) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.reload()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentFragmentId != R.id.startingFragment) {
                        navController.navigate(currentFragmentId)
                    }
                    else {
                        navController.navigate(R.id.navigation_friends)
                    }
                } else {
                    navController.navigate(R.id.authenticationViewPagerFragment)
                }
            } ?: navController.navigate(R.id.authenticationViewPagerFragment)
        } else {
            Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG).show()
        }


        navView.setupWithNavController(navController)

        setContentView(binding.root)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentFragmentId", currentFragmentId)
    }
    private fun setupNavController() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragmentId = destination.id
        }
    }

    fun goToSignup() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments.first()
        if (currentFragment is AuthenticationViewPagerFragment) {
            currentFragment.goToSignup()
        }
    }

    fun goToLogin() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments.first()
        if (currentFragment is AuthenticationViewPagerFragment) {
            currentFragment.goToLogin()
        }
    }

}