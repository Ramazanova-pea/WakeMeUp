package com.example.wakemeup.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ItemFriendBinding

/**
 * Friends adapter
 *
 * @property data
 * @constructor Create empty Friends adapter
 */
class FriendsAdapter(val data: ArrayList<FriendModel>) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    /**
     * ViewHolder for each friend item in the RecyclerView.
     * This holder binds the data of a friend to the UI.
     *
     * @property binding The binding of the friend item layout.
     */
    class FriendsViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.binding.apply {
            //TODO: "Set the profile pic"
            friendName.text = data[position].name
            when(data[position].isAwake){
               true -> wakeUpStatus.setBackgroundResource(R.drawable.open_eye)
               false -> wakeUpStatus.setBackgroundResource(R.drawable.closed_eye)
            }
            wakeupTime.text = data[position].wakingTime.time.toString()
        }
    }
}
