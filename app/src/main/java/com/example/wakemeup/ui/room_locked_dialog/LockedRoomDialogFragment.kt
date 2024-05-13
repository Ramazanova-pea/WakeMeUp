package com.example.wakemeup.ui.room_locked_dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.wakemeup.databinding.FragmentLockedRoomDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LockedRoomDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val binding = FragmentLockedRoomDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        binding.lockedRoomButton.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }

}