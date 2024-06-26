package com.example.wakemeup.ui.friends

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentFriendsBinding
import com.example.wakemeup.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var friendsViewModel: FriendsViewModel
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        friendsViewModel = ViewModelProvider(this)[FriendsViewModel::class.java]

        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_navigation_friends_to_settingsFragment)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.change_theme -> {
                    when (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
                        android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }

                        android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                    }
                    true
                }

                else -> false
            }
        }

        friendsAdapter = FriendsAdapter(ArrayList())
        binding.friendsRecyclerView.adapter = friendsAdapter
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            friendsViewModel.friends.collect { friends ->
                friendsAdapter.data.clear()
                friendsAdapter.data.addAll(friends)
                friendsAdapter.notifyDataSetChanged()
            }
        }

        binding.blockingView.setOnClickListener(null)

        binding.progressBar.setVisibilityAfterHide(View.GONE)

        return root
    }

    override fun onResume() {

        if (isNetworkAvailable(requireContext())) {
            lifecycleScope.launch {
                friendsViewModel.getFriends().collect {
                    if (it) {
                        binding.progressBar.setProgressCompat(100, true)
                        withContext(Dispatchers.IO) {
                            Thread.sleep(1000)
                        }

                        binding.progressBar.hide()
                        val animator = ObjectAnimator.ofFloat(
                            binding.blockingView,
                            "alpha",
                            0.4f,
                            0f
                        )
                        animator.duration = 500
                        animator.start()

                        withContext(Dispatchers.IO) {
                            Thread.sleep(500)
                        }

                        binding.blockingView.visibility = View.GONE

                    } else {
                        binding.blockingView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            Snackbar.make(
                binding.root,
                "No internet connection",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        super.onResume()
    }
}