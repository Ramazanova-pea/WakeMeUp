package com.example.wakemeup.ui.room

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wakemeup.R
import com.example.wakemeup.databinding.FragmentRoomBinding
import com.example.wakemeup.domain.RoomModel
import com.example.wakemeup.toBitmap
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomFragment : Fragment() {
    lateinit var binding: FragmentRoomBinding
    lateinit var viewModel: RoomViewModel
    lateinit var adapter: MemberAdapter
    var room: RoomModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[RoomViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        adapter = MemberAdapter()
        binding.roomMembersRecyclerView.adapter = adapter
        binding.roomMembersRecyclerView.layoutManager = LinearLayoutManager(context)

        val args = arguments
        val roomId = args?.getString("roomId")
        lifecycleScope.launch {
            viewModel.getRoom(roomId!!).collect {
                if (it == null) {
                    findNavController().popBackStack()
                    Snackbar.make(binding.root, "Room not found", Snackbar.LENGTH_SHORT).show()
                } else {


                    room = it
                    binding.topAppBar.title = it.roomName
                    binding.topAppBar.subtitle = "ID: ${it.roomId}"
                    binding.roomImage.setImageBitmap(it.image.toBitmap())
                    binding.topAppBar.menu.clear()
                    val menuInflater = MenuInflater(context)
                    menuInflater.inflate(R.menu.top_app_bar_menu_rooms_users, binding.topAppBar.menu)

                    it.administrators.forEach { admin ->
                        if (admin == FirebaseAuth.getInstance().currentUser!!.uid) {
                            binding.topAppBar.menu.clear()
                            menuInflater.inflate(R.menu.top_app_bar_menu_rooms_admin, binding.topAppBar.menu)
                            return@forEach
                        }
                    }

                    binding.topAppBar.menu.forEach { item ->
                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    }
                }
            }
            viewModel.getMembers(roomId).collect { members ->
                adapter.setData(members)

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

            }
        }
        return binding.root
    }
}