package com.example.wakemeup.ui.join_room_dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wakemeup.databinding.FragmentJoinRoomDialogBinding
import com.example.wakemeup.domain.AddNewUserToRoomState
import com.example.wakemeup.ui.room_locked_dialog.LockedRoomDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class JoinRoomDialogFragment: DialogFragment() {

    interface OnDismissListener {
        fun onDismiss()
    }

    private var onDismissListener: OnDismissListener? = null

    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val binding = FragmentJoinRoomDialogBinding.inflate(layoutInflater)
        val viewModel = ViewModelProvider(this).get(JoinRoomDialogViewModel::class.java)
        builder.setView(binding.root)

        binding.joinRoomButton.setOnClickListener {
            // Check if the ID is valid
            val id = binding.idInput.text.toString()
            if (!id.matches(Regex("^[A-Za-z0-9_]{5,}$"))) {
                Snackbar.make(binding.root, "ID must be at least 5 characters long and contain only A-Z, a-z, 0-9, and _", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                lifecycleScope.launch {
                    viewModel.joinRoom(id).collect {
                        when (it) {
                            AddNewUserToRoomState.SUCCESS -> {
                                Snackbar.make(binding.root, "Successfully joined room", Snackbar.LENGTH_SHORT).show()
                                binding.blockingView.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                onDismissListener?.onDismiss()
                                dismiss()
                            }
                            AddNewUserToRoomState.LOADING -> {
                                isCancelable = false
                                binding.blockingView.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            AddNewUserToRoomState.ERROR -> {
                                Snackbar.make(binding.root, "Failed to join room. Try again later.", Snackbar.LENGTH_SHORT).show()
                                binding.blockingView.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                isCancelable = true
                            }
                            AddNewUserToRoomState.ROOM_IS_PRIVATE -> {
                                dismiss()
                                LockedRoomDialogFragment().show(parentFragmentManager, "locked_room_dialog")
                            }
                            AddNewUserToRoomState.ROOM_NOT_FOUND -> {
                                binding.idInputLayout.error = "Room not found"
                                binding.blockingView.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                isCancelable = true
                            }
                            AddNewUserToRoomState.USER_ALREADY_IN_ROOM -> {
                                binding.idInputLayout.error = "You are already in this room"
                                binding.blockingView.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                isCancelable = true
                            }
                        }
                    }
                }
            }
        }

        binding.idInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) < 5) {
                    binding.idInputLayout.error = "At least 5 characters long"
                    // s contains only A_Za-z 0-9 or _
                } else if (!s.toString().matches(Regex("^[A-Za-z0-9_]*$"))) {
                    binding.idInputLayout.error = "Only A-Z, a-z, 0-9, and _"
                } else {
                    binding.idInputLayout.error = null
                }
            }
        })
        return builder.create()
    }
}