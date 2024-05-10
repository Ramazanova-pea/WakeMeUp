package com.example.wakemeup.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentAuthenticationViewPagerBinding

class AuthenticationViewPagerFragment : Fragment() {

    lateinit var binding: FragmentAuthenticationViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAuthenticationViewPagerBinding.inflate(inflater, container, false)
        val viewPager = binding.viewPager
        val adapter = AuthenticationStateAdapter(requireActivity())
        viewPager.adapter = adapter
        return binding.root
    }



    fun goToLogin() {
        binding.viewPager.currentItem = 0
    }

    fun goToSignup() {
        binding.viewPager.currentItem = 1
    }

}