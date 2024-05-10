package com.example.wakemeup.ui.authentication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wakemeup.ui.authentication.login.LoginFragment
import com.example.wakemeup.ui.authentication.signup.SignupFragment

class AuthenticationStateAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) LoginFragment() else SignupFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}