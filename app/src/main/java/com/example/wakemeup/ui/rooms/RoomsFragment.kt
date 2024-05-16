package com.example.wakemeup.ui.rooms

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentFriendsBinding
import com.example.wakemeup.databinding.FragmentRoomsBinding
import com.example.wakemeup.ui.join_room_dialog.JoinRoomDialogFragment
import com.example.wakemeup.ui.new_room.AddNewRoomBottomSheet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomsFragment : Fragment() {

    private lateinit var binding: FragmentRoomsBinding
    private lateinit var roomsViewModel: RoomsViewModel
    private lateinit var roomsAdapter: RoomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        roomsViewModel =
            ViewModelProvider(this)[RoomsViewModel::class.java]

        binding = FragmentRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabSmall.setOnClickListener {
            val dialog = JoinRoomDialogFragment()
            dialog.setOnDismissListener(object : JoinRoomDialogFragment.OnDismissListener {
                override fun onDismiss() {
                    refreshRooms()
                }
            })
            dialog.show(parentFragmentManager, "JoinRoomDialogFragment")
        }

        binding.fab.setOnClickListener {
            val bottomSheet = AddNewRoomBottomSheet()
            bottomSheet.setOnDismissListener(object : AddNewRoomBottomSheet.OnDismissListener {
                override fun onDismiss() {
                    refreshRooms()
                }
            })
            bottomSheet.show(parentFragmentManager, "AddNewRoomBottomSheet")
        }

        roomsAdapter = RoomsAdapter(ArrayList())
        roomsAdapter.onRoomClickListener = {
            val bundle = Bundle()
            bundle.putString("roomId", it.roomId)
            findNavController().navigate(R.id.action_navigation_rooms_to_roomFragment, bundle)
        }
        binding.roomsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.roomsRecyclerView.adapter = roomsAdapter


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

        binding.blockingView.setOnClickListener(null)

        lifecycleScope.launch {
            roomsViewModel.rooms.collect { rooms ->
                roomsAdapter.data.clear()
                roomsAdapter.data.addAll(rooms)
                roomsAdapter.notifyDataSetChanged()
            }
        }


        return root
    }

    override fun onResume() {
        refreshRooms()
        super.onResume()
    }

    private fun refreshRooms() {
        lifecycleScope.launch {
            roomsViewModel.getRooms(FirebaseAuth.getInstance().uid!!).collect {
                if (it) {
                    binding.progressBar.setProgressCompat(100, true)
                    binding.progressBar.setVisibilityAfterHide(View.GONE)
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
                    binding.progressBar.visibility = View.VISIBLE
                    binding.blockingView.visibility = View.VISIBLE
                }
            }
        }
    }
}