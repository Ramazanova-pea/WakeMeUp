package com.example.wakemeup.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ItemFriendBinding
import com.example.wakemeup.domain.FriendModel
import java.text.SimpleDateFormat

class FriendsAdapter(val data: ArrayList<FriendModel>) :
    RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {
    class FriendsViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            ItemFriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.binding.apply {
            //TODO: "Set the profile pic"
            friendName.text = data[position].name
            when (data[position].isAwake) {
                true -> wakeUpStatus.setImageResource(R.drawable.open_eye)
                false -> wakeUpStatus.setImageResource(R.drawable.closed_eye)
            }
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            wakeupTime.text = simpleDateFormat.format(data[position].wakingTime.time)
        }
    }
}