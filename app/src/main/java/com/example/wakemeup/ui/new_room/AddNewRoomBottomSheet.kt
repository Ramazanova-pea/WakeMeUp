package com.example.wakemeup.ui.new_room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wakemeup.databinding.FragmentAddNewRoomBottomSheetBinding
import com.example.wakemeup.domain.CreateRoomState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddNewRoomBottomSheet : BottomSheetDialogFragment() {

    lateinit var _binding: FragmentAddNewRoomBottomSheetBinding
    val binding get() = _binding
    lateinit var viewModel: AddNewRoomBSViewModel

    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                viewModel.setRoomImage(requireContext(), imageUri!!)
                binding.roomAvatarImageView.setImageBitmap(viewModel.getRoomImage())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewRoomBottomSheetBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AddNewRoomBSViewModel::class.java]

        binding.roomAvatarImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageResultLauncher.launch(intent)
        }

        binding.confirmButton.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.addRoom(
                    binding.roomNameEditText.text.toString(),
                    binding.roomIdEditText.text.toString()
                ).collect { state ->
                    when (state) {
                        CreateRoomState.LOADING -> {
                            binding.confirmButton.isEnabled = false
                        }

                        CreateRoomState.SUCCESS -> {
                            binding.confirmButton.isEnabled = true
                            dismiss()
                        }

                        CreateRoomState.ROOM_ALREADY_EXISTS -> {
                            binding.roomIdTextInputLayout.error = "Room with this ID exists"
                            binding.confirmButton.isEnabled = true
                        }

                        CreateRoomState.ERROR -> {
                            binding.confirmButton.isEnabled = true
                            Snackbar.make(
                                binding.root,
                                "An error occurred. Try again later.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        return binding.root
    }

}