package com.example.wakemeup.ui.rooms

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wakemeup.databinding.ItemRoomBinding
import com.example.wakemeup.domain.RoomModel

class RoomsAdapter(val data: ArrayList<RoomModel>) : RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {

    var onRoomClickListener: ((RoomModel) -> Unit)? = null

    class RoomsViewHolder(val binding: ItemRoomBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        return RoomsViewHolder(
            ItemRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        holder.binding.apply {
            roomName.text = data[position].roomName
            roomImage.setImageBitmap(bytearrayToBitmap(data[position].image))
        }
        holder.itemView.setOnClickListener {
            onRoomClickListener?.invoke(data[position])
        }
    }

    private fun bytearrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}