package com.example.wakemeup

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
             supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!! as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_alarms, R.id.navigation_friends, R.id.navigation_rooms
            )
        )

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
                    navController.navigate(R.id.navigation_friends)
                }
            }
        } else {
            Snackbar.make(binding.root, "No internet connection", Snackbar.LENGTH_LONG).show()
        }


        navView.setupWithNavController(navController)
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