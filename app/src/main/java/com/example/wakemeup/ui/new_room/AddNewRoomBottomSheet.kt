package com.example.wakemeup.ui.new_room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wakemeup.R
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

    interface OnDismissListener {
        fun onDismiss()
    }

    private var onDismissListener: OnDismissListener? = null

    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
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
            if (binding.roomNameEditText.text.toString().isEmpty()) {
                binding.roomNameTextInputLayout.error = "Room name cannot be empty"
                return@setOnClickListener
            } else binding.roomNameTextInputLayout.error = null

            if (binding.roomIdEditText.text.toString().isEmpty()) {
                binding.roomIdTextInputLayout.error = "Room ID cannot be empty"
                return@setOnClickListener
            } else if (binding.roomIdEditText.text.toString().length < 5) {
                binding.roomIdTextInputLayout.error = "At least 5 characters long"
                return@setOnClickListener
            } else if (!binding.roomIdEditText.text.toString().matches(Regex("^[a-zA-Z0-9_]*\$"))) {
                binding.roomIdTextInputLayout.error = "Only A-Za-z, 0-9 and _ are allowed"
                return@setOnClickListener
            } else binding.roomIdTextInputLayout.error = null

            if (viewModel.getRoomImage() == null) {
                Snackbar.make(
                    binding.root,
                    "Please select a room avatar",
                    Snackbar.LENGTH_SHORT
                ).show()
                val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                binding.roomAvatarImageView.startAnimation(shake)
                return@setOnClickListener
            }

            viewModel.viewModelScope.launch {
                viewModel.addRoom(
                    binding.roomNameEditText.text.toString(),
                    binding.roomIdEditText.text.toString(),
                    binding.isPrivateCheckbox.isChecked
                ).collect { state ->
                    when (state) {
                        CreateRoomState.LOADING -> {
                            isCancelable = false
                            binding.confirmButton.isEnabled = false
                        }

                        CreateRoomState.SUCCESS -> {
                            isCancelable = true
                            binding.confirmButton.isEnabled = true
                            dismiss()
                            onDismissListener?.onDismiss()
                        }

                        CreateRoomState.ROOM_ALREADY_EXISTS -> {
                            isCancelable = true
                            binding.roomIdTextInputLayout.error = "Room with this ID exists"
                            binding.confirmButton.isEnabled = true
                        }

                        CreateRoomState.ERROR -> {
                            isCancelable = true
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

        binding.roomIdEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) < 5) binding.roomIdTextInputLayout.error = "At least 5 characters long"
                // Check if id contains only english letters, numbers and underscores
                else if (!s.toString().matches(Regex("^[a-zA-Z0-9_]*\$"))) {
                    binding.roomIdTextInputLayout.error = "Only A-Za-z, 0-9 and _ are allowed"
                } else binding.roomIdTextInputLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return binding.root
    }

}