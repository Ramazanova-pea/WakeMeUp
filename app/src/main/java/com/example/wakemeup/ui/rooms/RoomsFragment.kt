package com.example.wakemeup.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wakemeup.databinding.FragmentRoomsBinding
import com.example.wakemeup.ui.new_room.AddNewRoomBottomSheet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RoomsFragment : Fragment() {

    private var _binding: FragmentRoomsBinding? = null
    private val binding get() = _binding!!
    private lateinit var roomsViewModel: RoomsViewModel
    private lateinit var roomsAdapter: RoomsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        roomsViewModel =
            ViewModelProvider(this)[RoomsViewModel::class.java]

        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.fab.setOnClickListener {
            val bottomSheet = AddNewRoomBottomSheet()
            bottomSheet.show(parentFragmentManager, "AddNewRoomBottomSheet")
        }

        roomsAdapter = RoomsAdapter(ArrayList())
        binding.roomsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.roomsRecyclerView.adapter = roomsAdapter

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
        lifecycleScope.launch {
            roomsViewModel.getRooms(FirebaseAuth.getInstance().uid!!)
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}